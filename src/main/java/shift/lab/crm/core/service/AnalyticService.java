package shift.lab.crm.core.service;

import shift.lab.crm.api.Dto.SellerPeakTransactionDto;
import shift.lab.crm.api.Dto.SellerTopDto;

import java.util.List;

public interface AnalyticService {
    SellerTopDto successSeller(String startDate, String endDate);

    List<SellerTopDto> sellersByAmountLessThan(String startDate, String endDate, Long min);

    SellerPeakTransactionDto peakTransactionTimeForSeller(Long sellerId, String period);

}
