package shift.lab.crm.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import shift.lab.crm.api.Dto.SellerPeakTransactionDto;
import shift.lab.crm.api.Dto.SellerTopDto;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.exception.NotFoundException;
import shift.lab.crm.core.mapper.SellerMapper;
import shift.lab.crm.core.repository.TransactionRepository;
import shift.lab.crm.core.service.AnalyticService;
import shift.lab.crm.core.service.DateTimeService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class AnalyticServiceImpl implements AnalyticService {
    private final DateTimeService dateTimeService;
    private final TransactionRepository transactionRepository;
    private final SellerMapper sellerMapper;

    @Override
    public SellerTopDto successSeller(String startDate, String endDate) {
        log.info("Retrieving top seller from {} to {}", startDate, endDate);

        LocalDateTime startDateTime = dateTimeService.parseToLocalDateTime(startDate, true);
        LocalDateTime endDateTime = dateTimeService.parseToLocalDateTime(endDate, false);
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
    public List<SellerTopDto> sellersByAmountLessThan(String startDate, String endDate, Long min) {
        log.info("Retrieving sellers with total amount less than {} from {} to {}", min, startDate, endDate);

        LocalDateTime startDateTime = dateTimeService.parseToLocalDateTime(startDate, true);
        LocalDateTime endDateTime = dateTimeService.parseToLocalDateTime(endDate, false);
        log.debug("Parsed dates: startDateTime={}, endDateTime={}", startDateTime, endDateTime);

        List<Object[]> results = transactionRepository.findSellerMin(startDateTime, endDateTime, min);

        ensureResultsNotEmpty(results, "No sellers found with amount less than " + min + " in the period from " + startDate + " to " + endDate);

        List<SellerTopDto> sellerList = results.stream()
                .map(topSellerData -> sellerMapper.map((Seller) topSellerData[0], (Long) topSellerData[1]))
                .collect(Collectors.toList());

        log.info("Found {} sellers with amount less than {}", sellerList.size(), min);
        return sellerList;
    }

    @Override
    public SellerPeakTransactionDto peakTransactionTimeForSeller(Long sellerId, String period) {
        log.info("Retrieving peak transaction time for sellerId={} during the period {}", sellerId, period);

        List<Object[]> results = transactionRepository.findByPeakPeriod(sellerId, period);

        ensureResultsNotEmpty(results, "Peak transaction period not found for sellerId=" + sellerId + " and period=" + period);

        Object[] result = results.get(0);
        LocalDateTime periodDate = ((Timestamp) result[0]).toLocalDateTime();
        Long count = ((Number) result[1]).longValue();

        log.info("Peak transaction time: date={}, count={}", periodDate, count);
        return sellerMapper.map(periodDate, count);
    }
}
