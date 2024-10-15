package shift.lab.crm.core.service;

import shift.lab.crm.api.Dto.SellerPeakTransactionDto;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.api.Dto.TopSellerDto;

import java.time.Period;
import java.util.List;

public interface AnalyticService {
    TopSellerDto successSeller(String startDate, String endDate);

    List<TopSellerDto> sellersByAmountLessThan(String startDate, String endDate,Long min);
    SellerPeakTransactionDto peakTransactionTimeForSeller(Long sellerId,String period);

}
