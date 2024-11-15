package shift.lab.crm.api.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Schema(description = "Выводится информация о продавце и сумме его продаж")
public record SellerPeakTransactionDto(
        @NotBlank
        @Schema(description = "Промежуток когда больше всего запросов", example = "2024-10-14 00:00:00")
        LocalDateTime periodDate,
        @NotNull
        @Schema(description = "Кол-во запросов", example = "150")
        Long count
) {
}