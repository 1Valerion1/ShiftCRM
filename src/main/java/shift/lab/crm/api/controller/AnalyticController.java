package shift.lab.crm.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shift.lab.crm.api.Dto.SellerPeakTransactionDto;
import shift.lab.crm.api.Dto.SellerTopDto;
import shift.lab.crm.core.service.AnalyticService;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("api/v1/analytic")
@RequiredArgsConstructor
@Tag(name = "AnalyticController", description = "Аналитика о системе")
public class AnalyticController {
    private final AnalyticService analyticService;

    @GetMapping
    @Operation(description = "Получаем самого успешного продавца в рамках дня, месяцы, квартала, года." +
            " Успешный - сумма транзакци больше всех.")
    public SellerTopDto getSuccessSeller(@Parameter(description = "Дата начала диапазона в формате 2024-10-12")
                                         @Schema(example = "2024-10-12")
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                         @Parameter(description = "Дата окончания в формате: 2024-10-12")
                                         @Schema(example = "2024-10-12")
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return analyticService.successSeller(startDate, endDate);
    }

    @GetMapping("/min")
    @Operation(description = "Получаем список продавцов с суммой меньше указанной. " +
            "Сумма всех транзакции за выбранный период меньше переданного параметра суммы")
    public List<SellerTopDto> getSellersMin(@Parameter(description = "Дата начала диапазона в формате 2024-10-12")
                                            @Schema(example = "2024-10-12")
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @Parameter(description = "Дата начала диапазона в формате 2024-10-12")
                                            @Schema(example = "2024-10-12")
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                            @Schema(example = "100")
                                            @RequestParam @Min(value = 100) Long SumMin) {

        return analyticService.sellersByAmountLessThan(startDate, endDate, SumMin);
    }
    @GetMapping("/peak")
    @Operation(description = "Получаем самое продуктивное время продавца, когда он получил больше всего транзакций.")
    public SellerPeakTransactionDto getPeak(@Schema(example = "2024-10-12")
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @Schema(example = "2024-10-12")
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                            @Schema(example = "1")
                                            @RequestParam @NotNull @Min(value = 1) Long seller_id) {
        return analyticService.getBestTransactionPeriod(seller_id, startDate, endDate);
    }

}
