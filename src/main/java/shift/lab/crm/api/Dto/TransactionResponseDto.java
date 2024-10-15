package shift.lab.crm.api.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import shift.lab.crm.core.entity.Seller;

import java.time.LocalDateTime;

public record TransactionResponseDto (
        @NotEmpty
        @Schema(example = "1")
        Long id,
        @NotEmpty
        @Schema(example = "1")
        String seller_id,
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z]{4,8}$", message = "должен содержать от 4 до 8 английских букв CASH, TRANSFER ")
        @Schema(example = "CASH")
        String paymentType,
        @NotEmpty
        @Min(value = 100, message = "amount должен быть положительным числом больше 100 ")
        @Schema(example = "100")
        Long amount,
        LocalDateTime transactionDate




){
}