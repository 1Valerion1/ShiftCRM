package shift.lab.crm.api.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record TransactionCreateDto(
        @NotBlank
        @Pattern(regexp = "^(CASH|TRANSFER|CARD|cash|transfer|card)$")
        @Schema(example = "CASH", description = "Три вида: CASH,CARD,TRANSFER")
        String paymentType,
        @NotNull
        @Min(value = 100, message = "amount должен быть положительным числом больше 100 ")
        @Schema(example = "100")
        Long amount
) {
}
