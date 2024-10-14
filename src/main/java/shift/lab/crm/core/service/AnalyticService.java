package shift.lab.crm.core.service;

import shift.lab.crm.api.Dto.SellerResponseDto;

import java.util.List;

public interface AnalyticService {
    SellerResponseDto successSeller();

    List<SellerResponseDto> SellersByAmountLessThan(Long min);

    SellerResponseDto PeakTransactionTimeForSeller(Long sellerId);

}
