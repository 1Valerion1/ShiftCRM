package shift.lab.crm.api.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record SellerUpdateDto (
        @NotNull
        @Schema(description = "Id в системе", example = "1")
        @Min(value = 1)
        Long id,
        @Pattern(regexp = "^[a-zA-ZА-Яа-я0-9]{0,20}$")
        @Schema(description = "Имя продавца", example = "Voody")
        String name,
        @Pattern(regexp = "^(\\+7\\d{10}|[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z0-9]{2,50})$")
        @Schema(description = "Контактная информация (телефон или адрес электронной почты)",
                example = "+71234567890 или user@example.com")
        String contactInfo

) {
}