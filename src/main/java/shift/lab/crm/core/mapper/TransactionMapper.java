package shift.lab.crm.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.api.Dto.TransactionResponseDto;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.entity.Transaction;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "sellerId",source = "seller.id")
    TransactionResponseDto map(Transaction transaction);
    List<TransactionResponseDto> map(List<Transaction> transaction);

}
