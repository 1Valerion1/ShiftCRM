package shift.lab.crm.api.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record TransactionCreateDto(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z]{4,8}$", message = "должен содержать от 4 до 8 английских букв CASH, TRANSFER ")
        @Schema(example = "CASH",description = "Три вида: CASH,CARD,TRANSFER")
        String paymentType,
        @NotEmpty
        @Min(value = 100, message = "amount должен быть положительным числом больше 100 ")
        @Schema(example = "100")
        Long amount
) {
}
