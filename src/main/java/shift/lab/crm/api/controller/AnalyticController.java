package shift.lab.crm.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.core.service.AnalyticService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/analytic")
public class AnalyticController {
    private final AnalyticService analyticService;

    @GetMapping
    public SellerResponseDto getSuccessSeller() {

        return analyticService.successSeller();
    }

    @GetMapping("/min")
    public List<SellerResponseDto> getSellersMin(@RequestParam Long min) {

        return analyticService.SellersByAmountLessThan(min);
    }

    @GetMapping("/peak")
    public SellerResponseDto getPeak(Long seller_id) {

        return analyticService.PeakTransactionTimeForSeller(seller_id);
    }
}
