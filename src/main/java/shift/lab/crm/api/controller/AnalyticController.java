package shift.lab.crm.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "AnalyticController", description = "Аналитика о системе")
public class AnalyticController {
    private final AnalyticService analyticService;

    @GetMapping
    @Operation(description = "Получаем самого успешного продавца")
    public SellerResponseDto getSuccessSeller() {

        return analyticService.successSeller();
    }

    @GetMapping("/min")
    @Operation(description = "Получаем список продавцов с суммой меньше указанной")
    public List<SellerResponseDto> getSellersMin(@RequestParam Long min) {

        return analyticService.SellersByAmountLessThan(min);
    }

    @GetMapping("/peak")
    @Operation(description = "Получаем самое продуктивное время продавца")
    public SellerResponseDto getPeak(Long seller_id) {

        return analyticService.PeakTransactionTimeForSeller(seller_id);
    }
}
