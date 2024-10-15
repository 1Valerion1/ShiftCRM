package shift.lab.crm.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shift.lab.crm.api.Dto.SellerPeakTransactionDto;
import shift.lab.crm.api.Dto.TopSellerDto;
import shift.lab.crm.core.service.AnalyticService;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnalyticServiceImpl implements AnalyticService {
    private final DateTimeService dateTimeService;
    private final TransactionDataHandler transactionDataHandler;

    @Override
    public TopSellerDto successSeller(String startDate, String endDate) {
        LocalDateTime startDateTime = dateTimeService.parseToLocalDateTime(startDate, true);
        LocalDateTime endDateTime = dateTimeService.parseToLocalDateTime(endDate, false);

        return transactionDataHandler.findTopSeller(startDateTime, endDateTime);
    }

    @Override
    public List<TopSellerDto> sellersByAmountLessThan(String startDate, String endDate, Long min) {
        LocalDateTime startDateTime = dateTimeService.parseToLocalDateTime(startDate, true);
        LocalDateTime endDateTime = dateTimeService.parseToLocalDateTime(endDate, false);

        return transactionDataHandler.findSellersByAmountLessThan(startDateTime, endDateTime, min);
    }


    @Override
    public SellerPeakTransactionDto peakTransactionTimeForSeller(Long sellerId, String period) {

        return transactionDataHandler.findPeakTransactionTime(sellerId, period);

    }
}
