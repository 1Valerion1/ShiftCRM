package shift.lab.crm.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shift.lab.crm.api.Dto.TransactionResponseDto;
import shift.lab.crm.core.entity.Transaction;
import shift.lab.crm.core.entity.TransactionHistory;
import shift.lab.crm.core.entity.enums.Operation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "sellerId", source = "seller.id")
    TransactionResponseDto map(Transaction transaction);

    List<TransactionResponseDto> map(List<Transaction> transaction);
    @Mapping(target = "transactionId", source = "transaction.id")
    @Mapping(target = "sellerId", source = "transaction.seller.id")
    @Mapping(target = "amount", source = "transaction.amount")
    @Mapping(target = "paymentType", source = "transaction.paymentType")
    @Mapping(target = "transactionDate", source = "transaction.transactionDate")
    @Mapping(target = "changeData", ignore = true)
    TransactionHistory map(Transaction transaction, Operation operation);

}
