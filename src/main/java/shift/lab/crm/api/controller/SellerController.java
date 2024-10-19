package shift.lab.crm.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shift.lab.crm.api.Dto.SellerCreatetDto;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.api.Dto.SellerResponseUpdateDto;
import shift.lab.crm.api.Dto.SellerUpdateDto;
import shift.lab.crm.core.service.SellerService;

import java.util.List;

@Validated
@RestController
@RequestMapping("api/v1/seller")
@RequiredArgsConstructor
@Tag(name = "SellerController", description = "Операции над продавцом")
public class SellerController {
    private final SellerService sellerService;

    @GetMapping("/all")
    @Operation(description = "Получаем список всех продавцов в системе")
    public List<SellerResponseDto> getAllSeller() {
        return sellerService.listAllSeller();
    }

    @GetMapping("/{id}")
    @Operation(description = "Получаем информацию о конкретном продавце в системе.")
    public SellerResponseDto getInfoOneSeller(@Schema(example = "1")
                                              @RequestParam @Min(value = 1) Long id) {
        return sellerService.infoSeller(id);
    }

    @PostMapping
    @Operation(description = "Создаем нового продавца")
    public SellerResponseDto createSeller(@Validated @RequestBody SellerCreatetDto sellerCreatetDto) {

        return sellerService.create(sellerCreatetDto);
    }

    @PatchMapping
    @Operation(description = "Обновляем информацию о продавце(name или contactInfo) ")
    public SellerResponseUpdateDto updateSeller(@Validated @RequestBody SellerUpdateDto sellerUpdateDto) {
        return sellerService.update(sellerUpdateDto);
    }

    @DeleteMapping("delete/{id}")
    @Operation(description = "Удаляем нашего продавца")
    public void removeSeller(@Schema(example = "1")
                             @RequestParam @Min(value = 1) Long id) {
        sellerService.deleteSeller(id);
    }

}
