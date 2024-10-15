package shift.lab.crm.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shift.lab.crm.api.Dto.SellerPeakTransactionDto;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.api.Dto.TopSellerDto;
import shift.lab.crm.core.service.AnalyticService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/analytic")
@Tag(name = "AnalyticController", description = "Аналитика о системе")
public class AnalyticController {
    private final AnalyticService analyticService;

    //2024-10-15
    //2024-01-01
    @GetMapping
    @Operation(description = "Получаем самого успешного продавца в рамках дня, месяцы, квартала, года." +
            " Успешный - сумма транзакци больше всех")
    public TopSellerDto getSuccessSeller(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        return analyticService.successSeller(startDate, endDate);
    }

    @GetMapping("/min")
    @Operation(description = "Получаем список продавцов с суммой меньше указанной. " +
            "Сумма всех транзакции за выбранный период меньше переданного параметра суммы")
    public List<TopSellerDto> getSellersMin(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate,
                                                 @RequestParam Long min) {

        return analyticService.sellersByAmountLessThan(startDate,endDate,min);
    }

    @GetMapping("/peak")
    @Operation(description = "Получаем самое продуктивное время продавца, когда он получил больше всего транзакций. " +
            "Период: day, week, month, year. Используйте соответствующие значения для параметра period.")
    public SellerPeakTransactionDto getPeak(@RequestParam String period, @RequestParam Long seller_id) {

        return analyticService.peakTransactionTimeForSeller(seller_id,period);
    }
}
