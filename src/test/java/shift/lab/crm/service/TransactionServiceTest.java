package shift.lab.crm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import shift.lab.crm.core.service.serviceImpl.TransactionServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private SellerService sellerService;

    @Test
    void testCreateTransactionSuccess() {
        Long sellerId = 1L;
        TransactionCreateDto createDto = new TransactionCreateDto("CASH", 150L);
        Seller seller = Seller.builder().id(sellerId).build();
        Transaction transaction = Transaction.builder()
                .seller(seller)
                .paymentType(createDto.paymentType())
                .amount(createDto.amount())
                .build();
        Transaction savedTransaction = Transaction.builder()
                .id(1L)
                .seller(seller)
                .paymentType(createDto.paymentType())
                .amount(createDto.amount())
                .build();
        TransactionHistory transactionHistory = new TransactionHistory();
        TransactionResponseDto responseDto = new TransactionResponseDto(savedTransaction.getId(), sellerId.toString(), savedTransaction.getPaymentType(), savedTransaction.getAmount(), savedTransaction.getTransactionDate());

        when(sellerService.findSellerById(sellerId)).thenReturn(seller);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);
        when(transactionMapper.map(savedTransaction, Operation.CREATE)).thenReturn(transactionHistory);
        when(transactionMapper.map(savedTransaction)).thenReturn(responseDto);

        TransactionResponseDto result = transactionService.create(createDto, sellerId);

        assertEquals(responseDto, result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(transactionHistoryRepository, times(1)).save(any(TransactionHistory.class));
    }

    @Test
    void testCreateTransactionSellerNotFound() {
        Long sellerId = 1L;
        TransactionCreateDto createDto = new TransactionCreateDto("CASH", 150L);

        when(sellerService.findSellerById(sellerId)).thenThrow(new NotFoundException("Seller not found"));

        assertThrows(NotFoundException.class, () -> transactionService.create(createDto, sellerId));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }


    @Test
    void testListAllTransactionSuccess() {
        List<Transaction> transactions = Arrays.asList(
                Transaction.builder()
                        .id(1L)
                        .build(),
                Transaction.builder()
                        .id(2L)
                        .build()
        );
        List<TransactionResponseDto> responseDtos = Arrays.asList(
                new TransactionResponseDto(1L, "1", "CASH", 100L, LocalDateTime.now()),
                new TransactionResponseDto(2L, "2", "CARD", 200L, LocalDateTime.now())
        );

        when(transactionRepository.findAll()).thenReturn(transactions);
        when(transactionMapper.map(transactions)).thenReturn(responseDtos);

        List<TransactionResponseDto> result = transactionService.listAllTransaction();

        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testListAllTransactionSellerSuccess() {
        Long sellerId = 1L;
        List<Transaction> transactions = Arrays.asList(
                Transaction.builder()
                        .id(1L)
                        .build(),
                Transaction.builder()
                        .id(2L)
                        .build()
        );
        List<TransactionResponseDto> responseDtos = Arrays.asList(
                new TransactionResponseDto(1L, "1", "CASH", 100L, LocalDateTime.now()),
                new TransactionResponseDto(2L, "2", "CARD", 200L, LocalDateTime.now())
        );

        when(transactionRepository.findBySellerId(sellerId)).thenReturn(transactions);
        when(transactionMapper.map(transactions)).thenReturn(responseDtos);

        List<TransactionResponseDto> result = transactionService.listAllTransactionSeller(sellerId);

        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findBySellerId(sellerId);
    }

    @Test
    void testListAllTransactionSellerNotFound() {
        Long sellerId = 1L;
        when(transactionRepository.findBySellerId(sellerId)).thenReturn(Collections.emptyList());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> transactionService.listAllTransactionSeller(sellerId));
        assertEquals("No transactions found for seller with ID = " + sellerId, exception.getMessage());
        verify(transactionRepository, times(1)).findBySellerId(sellerId);
    }

    @Test
    void testInfoTransactionSuccess() {
        Long transactionId = 1L;
        Transaction transaction = Transaction.builder()
                .id(transactionId)
                .build();
        TransactionResponseDto responseDto = new TransactionResponseDto(transactionId, "1", "CASH", 100L, LocalDateTime.now());

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionMapper.map(transaction)).thenReturn(responseDto);

        TransactionResponseDto result = transactionService.infoTransaction(transactionId);

        assertEquals(responseDto, result);
        verify(transactionRepository, times(1)).findById(transactionId);
    }

    @Test
    void testInfoTransactionNotFound() {
        Long transactionId = 1L;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> transactionService.infoTransaction(transactionId));
        assertEquals("Transaction with ID = " + transactionId + " not found", exception.getMessage());
        verify(transactionRepository, times(1)).findById(transactionId);
    }

}
