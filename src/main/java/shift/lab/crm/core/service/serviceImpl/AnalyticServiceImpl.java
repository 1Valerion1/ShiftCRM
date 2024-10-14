package shift.lab.crm.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.core.service.AnalyticService;

import java.util.List;


@Service
public class AnalyticServiceImpl implements AnalyticService {
    @Override
    public SellerResponseDto successSeller() {
        return null;
    }

    @Override
    public List<SellerResponseDto> SellersByAmountLessThan(Long min) {
        return null;
    }


    @Override
    public SellerResponseDto PeakTransactionTimeForSeller(Long sellerId) {
        return null;
    }
}
