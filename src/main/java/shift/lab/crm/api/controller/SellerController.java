package shift.lab.crm.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shift.lab.crm.api.Dto.SellerCreatetDto;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.api.Dto.SellerUpdateDto;
import shift.lab.crm.core.service.SellerService;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/seller")
@Tag(name = "SellerController", description = "Операции над продавцом")
public class SellerController {
    private final SellerService sellerService;

    @GetMapping("/all")
    @Operation(description = "Получаем список всех продавцов в системе")
    public List<SellerResponseDto> getAllSeller() {
        return  sellerService.listAllSeller();
    }

    @GetMapping("/{id}")
    @Operation(description = "Получаем информацию о конкретном продавце в системе.")
    public SellerResponseDto getInfoOneSeller(@RequestParam Long id) {
        return sellerService.infoSeller(id);
    }

    @PostMapping
    @Operation(description = "Создаем нового продавца")
    public SellerResponseDto createSeller(@RequestBody SellerCreatetDto sellerCreatetDto) {

        return sellerService.create(sellerCreatetDto);
    }

    @PutMapping
    @Operation(description = "Обновляем информацию о продавце(name или contactInfo) ")
    public SellerResponseDto updateSeller(@RequestBody SellerUpdateDto sellerUpdateDto) {
        return sellerService.update(sellerUpdateDto);
    }

    @DeleteMapping("delete/{id}")
    @Operation(description = "Удаляем нашего продавца")
    public void removeSeller(@RequestParam Long id) {
        sellerService.deleteSeller(id);
    }

}
