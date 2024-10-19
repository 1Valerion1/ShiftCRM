package shift.lab.crm.core.service;

import shift.lab.crm.api.Dto.SellerPeakTransactionDto;
import shift.lab.crm.api.Dto.SellerTopDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AnalyticService {
    SellerTopDto successSeller(LocalDate startDate, LocalDate endDate);

    List<SellerTopDto> sellersByAmountLessThan(LocalDate startDate, LocalDate endDate, Long min);
    SellerPeakTransactionDto getBestTransactionPeriod(Long sellerId,LocalDate startDate, LocalDate endDate);

}
