package shift.lab.crm.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import shift.lab.crm.api.Dto.SellerPeakTransactionDto;
import shift.lab.crm.api.Dto.SellerTopDto;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.entity.Transaction;
import shift.lab.crm.core.exception.BadRequestException;
import shift.lab.crm.core.exception.NotFoundException;
import shift.lab.crm.core.mapper.SellerMapper;
import shift.lab.crm.core.repository.TransactionRepository;
import shift.lab.crm.core.service.AnalyticService;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class AnalyticServiceImpl implements AnalyticService {
    private final TransactionRepository transactionRepository;
    private final SellerMapper sellerMapper;

    public LocalDateTime parseToLocalDateTime(LocalDate localDate, boolean startOfDay) {
        return startOfDay ? localDate.atStartOfDay() : localDate.atTime(23, 59, 59);
    }
    @Override
    public SellerTopDto successSeller(LocalDate startDate, LocalDate endDate) {
        log.info("Retrieving top seller from {} to {}", startDate, endDate);

        LocalDateTime startDateTime = parseToLocalDateTime(startDate, true);
        LocalDateTime endDateTime = parseToLocalDateTime(endDate, false);
        log.debug("Parsed dates: startDateTime={}, endDateTime={}", startDateTime, endDateTime);

        List<Object[]> results = transactionRepository.findTopSeller(startDateTime, endDateTime);

        ensureResultsNotEmpty(results, "Top seller not found for the period from " + startDate + " to " + endDate);

        Object[] topSellerData = results.get(0);
        Seller seller = (Seller) topSellerData[0];
        Long sum = (Long) topSellerData[1];

        log.info("Top seller found: sellerId={}, totalAmount={}", seller.getId(), sum);
        return sellerMapper.map(seller, sum);
    }

    private void ensureResultsNotEmpty(List<Object[]> results, String errorMessage) {
        if (results.isEmpty()) {
            log.error("Error: {}", errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public List<SellerTopDto> sellersByAmountLessThan(LocalDate startDate, LocalDate endDate, Long min) {
        log.info("Retrieving sellers with total amount less than {} from {} to {}", min, startDate, endDate);

        LocalDateTime startDateTime = parseToLocalDateTime(startDate, true);
        LocalDateTime endDateTime = parseToLocalDateTime(endDate, false);
        log.debug("Parsed dates: startDateTime={}, endDateTime={}", startDateTime, endDateTime);

        List<Object[]> results = transactionRepository.findSellerMin(startDateTime, endDateTime, min);

        ensureResultsNotEmpty(results, "No sellers found with amount less than " + min + " in the period from " + startDate + " to " + endDate);

        List<SellerTopDto> sellerList = results.stream().map(topSellerData -> sellerMapper.map((Seller) topSellerData[0], (Long) topSellerData[1])).collect(Collectors.toList());

        log.info("Found {} sellers with amount less than {}", sellerList.size(), min);
        return sellerList;
    }

    @Override
    public SellerPeakTransactionDto getBestTransactionPeriod(Long sellerId, LocalDate startDate, LocalDate endDate) {
        log.info("Retrieving transactions for seller {} from {} to {}", sellerId, startDate, endDate);

        LocalDateTime startDateTime = parseToLocalDateTime(startDate, true);
        LocalDateTime endDateTime = parseToLocalDateTime(endDate, false);
        log.debug("Parsed dates: startDateTime={}, endDateTime={}", startDateTime, endDateTime);

        List<Transaction> transactions = transactionRepository.findBySellerIdAndTransactionDateBetween(sellerId, startDateTime, endDateTime);

        if (transactions.isEmpty()) {
            throw new NotFoundException("No transactions found for seller within the period");
        }

        Map<LocalDateTime, Long> transactionsGrouped = groupTransactionsByPeriod(transactions, startDate, endDate);

        Map.Entry<LocalDateTime, Long> peakPeriod = transactionsGrouped.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new NotFoundException("No transactions found for seller within the period"));

        log.info("Peak transaction period for seller {}: {} with {} transactions", sellerId, peakPeriod.getKey(), peakPeriod.getValue());

        return new SellerPeakTransactionDto(peakPeriod.getKey(), peakPeriod.getValue());
    }

    private Map<LocalDateTime, Long> groupTransactionsByPeriod(List<Transaction> transactions, LocalDate startDate, LocalDate endDate) {
        long totalDays = startDate.until(endDate).getDays();

        if (totalDays <= 7) {
            return transactions.stream()
                    .collect(Collectors.groupingBy(
                            t -> calculateAverageTime(t.getTransactionDate().toLocalDate(), transactions),
                            Collectors.counting()
                    ));

        } else if (totalDays <= 30) {
            return transactions.stream()
                    .collect(Collectors.groupingBy(
                            t -> {
                                LocalDate date = t.getTransactionDate().toLocalDate();
                                int weekOfYear = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
                                LocalDateTime startOfWeek = date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1).atStartOfDay();
                                return calculateAverageTime(startOfWeek.toLocalDate(), transactions);
                            },
                            Collectors.counting()
                    ));
        } else {

            return transactions.stream()
                    .collect(Collectors.groupingBy(
                            t -> {
                                LocalDateTime startOfMonth = t.getTransactionDate().withDayOfMonth(1).toLocalDate().atStartOfDay();
                                return calculateAverageTime(startOfMonth.toLocalDate(), transactions);
                            },
                            Collectors.counting()
                    ));
        }
    }

    private LocalDateTime calculateAverageTime(LocalDate date, List<Transaction> transactions) {
        List<LocalDateTime> times = transactions.stream()
                .filter(t -> t.getTransactionDate().toLocalDate().equals(date))
                .map(Transaction::getTransactionDate)
                .collect(Collectors.toList());

        long averageSeconds = (long) times.stream()
                .mapToLong(t -> t.toLocalTime().toSecondOfDay())
                .average()
                .orElse(0);

        return date.atTime((int) (averageSeconds / 3600), (int) ((averageSeconds % 3600) / 60), (int) (averageSeconds % 60));
    }


}
