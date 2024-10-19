package shift.lab.crm.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import shift.lab.crm.api.Dto.TransactionCreateDto;
import shift.lab.crm.api.Dto.TransactionResponseDto;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.entity.Transaction;
import shift.lab.crm.core.entity.TransactionHistory;
import shift.lab.crm.core.entity.enums.Operation;
import shift.lab.crm.core.exception.NotFoundException;
import shift.lab.crm.core.mapper.TransactionMapper;
import shift.lab.crm.core.repository.TransactionHistoryRepository;
import shift.lab.crm.core.repository.TransactionRepository;
import shift.lab.crm.core.service.SellerService;
import shift.lab.crm.core.service.TransactionService;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TransactionMapper transactionMapper;
    private final SellerService sellerService;

    @Override
    public TransactionResponseDto create(TransactionCreateDto transactionCreateDto, Long sellerId) {
        log.info("Attempting to create transaction for sellerId: {}", sellerId);

        Seller seller = sellerService.findSellerById(sellerId);

        Transaction transaction = Transaction.builder()
                .seller(seller)
                .paymentType(transactionCreateDto.paymentType())
                .amount(transactionCreateDto.amount())
                .build();

        Transaction transactionSaved = transactionRepository.save(transaction);
        log.info("Transaction created successfully with ID: {}", transactionSaved.getId());

        TransactionHistory transactionHistory = transactionMapper.map(transactionSaved, Operation.CREATE);
        transactionHistoryRepository.save(transactionHistory);
        log.info("Transaction history created for transaction ID: {}", transactionSaved.getId());

        return transactionMapper.map(transactionSaved);
    }

    @Override
    public List<TransactionResponseDto> listAllTransaction() {
        log.info("Retrieving all transactions");

        List<Transaction> transactions = transactionRepository.findAll();
        log.info("Found {} transactions", transactions.size());

        return transactionMapper.map(transactions);
    }

    @Override
    public List<TransactionResponseDto> listAllTransactionSeller(Long id) {
        log.info("Retrieving transactions for seller with ID: {}", id);

        List<Transaction> transactions = transactionRepository.findBySellerId(id);

        if (transactions.isEmpty()) {
            log.warn("No transactions found for seller with ID: {}", id);
            throw new NotFoundException("No transactions found for seller with ID = " + id);
        }

        log.info("Found {} transactions for seller with ID: {}", transactions.size(), id);
        return transactionMapper.map(transactions);
    }

    @Override
    public TransactionResponseDto infoTransaction(Long id) {
        log.info("Retrieving information for transaction ID: {}", id);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Transaction with ID {} not found", id);
                    return new NotFoundException("Transaction with ID = " + id + " not found");
                });

        log.info("Found transaction with ID: {}", id);
        return transactionMapper.map(transaction);
    }
}
