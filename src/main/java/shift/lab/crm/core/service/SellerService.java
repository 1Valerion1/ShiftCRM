package shift.lab.crm.core.service;

import shift.lab.crm.api.Dto.SellerCreatetDto;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.api.Dto.SellerResponseUpdateDto;
import shift.lab.crm.api.Dto.SellerUpdateDto;
import shift.lab.crm.core.entity.Seller;

import java.util.List;

public interface SellerService {
    SellerResponseDto create(SellerCreatetDto sellerCreatetDto);

    List<SellerResponseDto> listAllSeller();

    SellerResponseDto infoSeller(Long id);


    SellerResponseUpdateDto update(SellerUpdateDto sellerUpdateDto);

    void deleteSeller(Long id);

    Seller findSellerById(Long id);


}
