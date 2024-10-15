package shift.lab.crm.core.service;

import shift.lab.crm.api.Dto.SellerCreatetDto;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.api.Dto.SellerUpdateDto;
import shift.lab.crm.api.Dto.TransactionCreateDto;
import shift.lab.crm.api.Dto.TransactionRequestDto;
import shift.lab.crm.api.Dto.TransactionResponseDto;
import shift.lab.crm.core.entity.Transaction;

import java.util.List;

public interface TransactionService {
    TransactionResponseDto create(TransactionCreateDto transactionCreateDto,Long sellerId);

    List<TransactionResponseDto> listAllTransaction();

    List<TransactionResponseDto> listAllTransactionSeller(Long id);


    TransactionResponseDto infoTransaction(Long id);

//    TransactionResponseDto update(TransactionRequestDto sellerUpdateDto);

//    void deleteSeller(Long id);
}
