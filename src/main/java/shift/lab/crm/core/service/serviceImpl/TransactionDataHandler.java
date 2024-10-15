package shift.lab.crm.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shift.lab.crm.api.Dto.SellerPeakTransactionDto;
import shift.lab.crm.api.Dto.TopSellerDto;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.exception.NotFoundException;
import shift.lab.crm.core.mapper.SellerMapper;
import shift.lab.crm.core.repository.SellerRepository;
import shift.lab.crm.core.repository.TransactionRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionDataHandler {
    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;

    public TopSellerDto findTopSeller(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Object[]> results = transactionRepository.findTopSeller(startDateTime, endDateTime);
        if (results.isEmpty()) {
            throw new NotFoundException("Топ-продавец не найден");
        }

        Object[] topSellerData = results.get(0);
        Long topSellerId = (Long) topSellerData[0];
        Long sum = (Long) topSellerData[1];

        Seller seller = sellerRepository.findById(topSellerId)
                .orElseThrow(() -> new NotFoundException("Продавец не найден"));
        return sellerMapper.map(seller, sum);
    }

    public List<TopSellerDto> findSellersByAmountLessThan(LocalDateTime startDateTime, LocalDateTime endDateTime, Long min) {
        List<Object[]> results = transactionRepository.findSellerMin(startDateTime, endDateTime, min);
        if (results.isEmpty()) {
            return Collections.emptyList();
        }

        List<TopSellerDto> sellerResponses = new ArrayList<>();

        for (Object[] topSellerData : results) {
            Long topSellerId = (Long) topSellerData[0];
            Long sum = (Long) topSellerData[1];

            Seller seller = sellerRepository.findById(topSellerId)
                    .orElseThrow(() -> new NotFoundException("Продавец не найден"));

            TopSellerDto sellerSum = sellerMapper.map(seller, sum);
            sellerResponses.add(sellerSum);
        }

        return sellerResponses;
    }

    public SellerPeakTransactionDto findPeakTransactionTime(Long sellerId, String period) {
        List<Object[]> results = transactionRepository.findByPeakPeriod(sellerId, period);
        if (results.isEmpty()) {
            throw new NotFoundException("Данный период не найден.");
        }
        Object[] result = results.get(0);

        Timestamp timestamp = (Timestamp) result[0];
        LocalDateTime periodDate = timestamp.toLocalDateTime();
        Long count = ((Number) result[1]).longValue();

        return sellerMapper.map(periodDate, count);
    }
}