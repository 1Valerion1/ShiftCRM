package shift.lab.crm.core.mapper;

import org.mapstruct.Mapper;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.core.entity.Seller;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    SellerResponseDto map(Seller seller);
    List<SellerResponseDto> map(List<Seller> seller);

}
