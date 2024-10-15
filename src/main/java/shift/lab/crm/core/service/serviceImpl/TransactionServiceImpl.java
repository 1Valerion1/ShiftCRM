package shift.lab.crm.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shift.lab.crm.api.Dto.TransactionCreateDto;
import shift.lab.crm.api.Dto.TransactionResponseDto;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.entity.Transaction;
import shift.lab.crm.core.entity.TransactionHistory;
import shift.lab.crm.core.entity.enums.Operation;
import shift.lab.crm.core.exception.NotFoundException;
import shift.lab.crm.core.mapper.TransactionMapper;
import shift.lab.crm.core.repository.SellerRepository;
import shift.lab.crm.core.repository.TransactionHistoryRepository;
import shift.lab.crm.core.repository.TransactionRepository;
import shift.lab.crm.core.service.TransactionService;

import java.util.List;


@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TransactionMapper transactionMapper;
    private final SellerRepository sellerRepository;

    @Override
    public TransactionResponseDto create(TransactionCreateDto transactionCreateDto, Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new NotFoundException("Продавец с id " + sellerId + " не найден"));

        Transaction transaction = Transaction.builder()
                .seller(seller)
                .paymentType(transactionCreateDto.paymentType())
                .amount(transactionCreateDto.amount()).build();

        Transaction transactionSaved = transactionRepository.save(transaction);
        saveTransactionHistory(transactionSaved, Operation.CREATE);

        return transactionMapper.map(transactionSaved);
    }

    private void saveTransactionHistory(Transaction transaction, Operation operation) {
        TransactionHistory transactionHistory = TransactionHistory.builder()
                .transactionId(transaction.getId())
                .sellerId(transaction.getSeller().getId())
                .amount(transaction.getAmount())
                .paymentType(transaction.getPaymentType())
                .transactionDate(transaction.getTransactionDate())
                .operation(operation)
                .build();

        transactionHistoryRepository.save(transactionHistory);
    }

    @Override
    public List<TransactionResponseDto> listAllTransaction() {
        List<Transaction> transactionSuccesse = transactionRepository.findAll();
        return transactionMapper.map(transactionSuccesse);
    }

    @Override
    public List<TransactionResponseDto> listAllTransactionSeller(Long id) {

        List<Transaction> transactionSuccesse = transactionRepository.findBySellerId(id);
        if (transactionSuccesse.isEmpty()) {
            throw new NotFoundException("Продавец с таким ID = " + id + " не существует.");
        }
        return transactionMapper.map(transactionSuccesse);
    }

    @Override
    public TransactionResponseDto infoTransaction(Long id) {

        Transaction transactionSuccesse = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Такой транзакции не существует"));
        return transactionMapper.map(transactionSuccesse);
    }
}
