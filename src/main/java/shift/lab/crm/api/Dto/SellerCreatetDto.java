package shift.lab.crm.api.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record SellerCreatetDto(
        @NotBlank
        @Pattern(regexp = "^[a-zA-ZА-Яа-я0-9]{0,20}$")
        @Schema(description = "Имя продавца", example = "Voody")
        String name,
        @NotBlank
        @Pattern(regexp = "^(\\+7\\d{10}|[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z0-9]{2,50})$")
        @Schema(description = "Контактная информация (телефон или адрес электронной почты)",
                example = "+79234567890")
        String contactInfo

) {
}
