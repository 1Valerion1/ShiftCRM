package shift.lab.crm.api.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record SellerCreatetDto(
        @Pattern(regexp = "^[А-Я][а-я]{0,20}$")
        @Schema(description = "Имя продавца", example = "Voody")
        String name,
        @Pattern(regexp = "^(\\+7\\d{10}|[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,})$")
        @Schema(description = "Контактная информация (телефон или адрес электронной почты)",
                example = "+79234567890")
        String contactInfo

) {
}
