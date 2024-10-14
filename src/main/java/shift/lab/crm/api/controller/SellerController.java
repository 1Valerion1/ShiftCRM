package shift.lab.crm.api.controller;

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
import shift.lab.crm.api.Dto.SellerUpdateDto;
import shift.lab.crm.core.service.SellerService;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/seller")
public class SellerController {
    private final SellerService sellerService;

    @GetMapping("/all")
    public List<SellerResponseDto> getAllSeller() {
        return  sellerService.listAllSeller();
    }

    @GetMapping("/{id}")
    public SellerResponseDto getInfoOneSeller(@RequestParam Long id) {
        return sellerService.infoSeller(id);
    }

    @PostMapping
    public SellerResponseDto createSeller(@RequestBody SellerCreatetDto sellerCreatetDto) {

        return sellerService.create(sellerCreatetDto);
    }

    @PatchMapping
    public SellerResponseDto updateSeller(@RequestBody SellerUpdateDto sellerUpdateDto) {
        return sellerService.update(sellerUpdateDto);
    }

    @DeleteMapping("delete/{id}")
    public void removeSeller(@RequestParam Long id) {
        sellerService.deleteSeller(id);
    }

}
