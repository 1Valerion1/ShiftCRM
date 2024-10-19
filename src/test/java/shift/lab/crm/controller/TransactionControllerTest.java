package shift.lab.crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import shift.lab.crm.api.Dto.TransactionCreateDto;
import shift.lab.crm.api.Dto.TransactionResponseDto;
import shift.lab.crm.api.controller.TransactionController;
import shift.lab.crm.core.service.TransactionService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {
    @InjectMocks
    private TransactionController transactionController;
    @Mock
    private TransactionService transactionService;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        this.validator = localValidatorFactoryBean;
    }

    @Test
    public void testCreate_Positive() {
        Long sellerId = 1L;
        TransactionCreateDto createDto = new TransactionCreateDto("CASH", 150L);
        TransactionResponseDto responseDto = new TransactionResponseDto(1L, "1", "CASH", 150L, LocalDateTime.now());

        when(transactionService.create(createDto, sellerId)).thenReturn(responseDto);

        TransactionResponseDto result = transactionController.create(sellerId, createDto);

        assertNotNull(result);
        assertEquals("CASH", result.paymentType());
        assertEquals(150L, result.amount());
    }

    @Test
    public void testCreate_Negative() {
        TransactionCreateDto createDto = new TransactionCreateDto("INVALID", 150L);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(createDto, "createDto");

        validator.validate(createDto, bindingResult);

        assertFalse(bindingResult.getAllErrors().isEmpty());
        assertNotNull(bindingResult.getFieldError("paymentType"));
    }

    @Test
    public void testGetListTransaction_Positive() {
        TransactionResponseDto transaction = new TransactionResponseDto(1L, "1", "CASH", 150L, LocalDateTime.now());
        List<TransactionResponseDto> transactions = List.of(transaction);

        when(transactionService.listAllTransaction()).thenReturn(transactions);

        List<TransactionResponseDto> result = transactionController.getListTransaction();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("CASH", result.get(0).paymentType());
    }

    @Test
    public void testGetListTransaction_Negative() {
        when(transactionService.listAllTransaction()).thenReturn(Collections.emptyList());

        List<TransactionResponseDto> result = transactionController.getListTransaction();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetListTransactionSeller_Positive() {
        Long sellerId = 1L;
        TransactionResponseDto transaction = new TransactionResponseDto(1L, "1", "CASH", 150L, LocalDateTime.now());
        List<TransactionResponseDto> transactions = List.of(transaction);

        when(transactionService.listAllTransactionSeller(sellerId)).thenReturn(transactions);

        List<TransactionResponseDto> result = transactionController.getListTransactionSelller(sellerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("CASH", result.get(0).paymentType());
    }

    @Test
    public void testGetListTransactionSeller_Negative() {
        Long sellerId = 999L;

        when(transactionService.listAllTransactionSeller(sellerId)).thenReturn(Collections.emptyList());

        List<TransactionResponseDto> result = transactionController.getListTransactionSelller(sellerId);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTransaction_Positive() {
        Long transId = 1L;
        TransactionResponseDto transaction = new TransactionResponseDto(transId, "1", "CASH", 150L, LocalDateTime.now());

        when(transactionService.infoTransaction(transId)).thenReturn(transaction);

        TransactionResponseDto result = transactionController.getTransaction(transId);

        assertNotNull(result);
        assertEquals("CASH", result.paymentType());
    }

    @Test
    public void testGetTransaction_Negative() {
        Long transId = 999L;

        when(transactionService.infoTransaction(transId)).thenReturn(null);

        TransactionResponseDto result = transactionController.getTransaction(transId);

        assertNull(result);
    }
}

