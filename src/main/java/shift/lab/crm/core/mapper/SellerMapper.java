package shift.lab.crm.core.mapper;

import org.mapstruct.Mapper;
import shift.lab.crm.api.Dto.SellerPeakTransactionDto;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.api.Dto.SellerTopDto;
import shift.lab.crm.core.entity.Seller;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    SellerResponseDto map(Seller seller);

    SellerTopDto map(Seller seller, Long sumAmount);

    SellerPeakTransactionDto map(LocalDateTime periodDate, Long count);

    List<SellerResponseDto> map(List<Seller> seller);
}
