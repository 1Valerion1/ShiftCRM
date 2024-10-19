package shift.lab.crm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shift.lab.crm.api.Dto.SellerPeakTransactionDto;
import shift.lab.crm.api.Dto.SellerTopDto;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.entity.Transaction;
import shift.lab.crm.core.exception.NotFoundException;
import shift.lab.crm.core.mapper.SellerMapper;
import shift.lab.crm.core.repository.TransactionRepository;
import shift.lab.crm.core.service.serviceImpl.AnalyticServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticServiceTest {
    @InjectMocks
    private AnalyticServiceImpl analyticService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private SellerMapper sellerMapper;

    @Test
    public void testSuccessSellerWhenTopSellerFound() {
        Seller seller = Seller.builder()
                .id(1l)
                .name("Vook")
                .contactInfo("+79823242124")
                .registrationDate(LocalDateTime.of(2024, 10, 12, 00, 00))
                .build();

        Long totalAmount = 1000L;

        SellerTopDto expectedDto = new SellerTopDto(seller.getId(), seller.getName(), seller.getContactInfo(), totalAmount);

        LocalDate startDate = LocalDate.of(2024, 10, 12);
        LocalDate endDate = LocalDate.of(2024, 10, 12);
        Object[] result = new Object[]{seller, totalAmount};

        when(transactionRepository.findTopSeller(startDate.atStartOfDay(), endDate.atTime(23, 59, 59))).thenReturn(List.of(result, result));
        when(sellerMapper.map(seller, totalAmount)).thenReturn(expectedDto);


        SellerTopDto actualDto = analyticService.successSeller(startDate, endDate);

        assertEquals(expectedDto, actualDto, "Должны вернуть ожидаемого продавца с данными");
        assertEquals(expectedDto.id(), actualDto.id());
        assertEquals(expectedDto.name(), actualDto.name());
        assertEquals(expectedDto.contactInfo(), actualDto.contactInfo());

    }

    @Test
    public void testSuccessSellerWhenNoTopSellerFound() {

        LocalDate startDate = LocalDate.of(2024, 10, 12);
        LocalDate endDate = LocalDate.of(2024, 10, 12);
        when(transactionRepository.findTopSeller(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)))
                .thenReturn(Collections.emptyList());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            analyticService.successSeller(startDate, endDate);
        });
        assertEquals("Top seller not found for the period from 2024-10-12 to 2024-10-12", thrown.getMessage());
    }

    @Test
    public void testSellersByAmountLessThanWhenSellersFound() {

        Seller seller1 = Seller.builder()
                .id(1l)
                .name("Anubis")
                .contactInfo("+79823242124")
                .registrationDate(LocalDateTime.of(2024, 10, 12, 00, 00))
                .build();
        Seller seller2 = Seller.builder()
                .id(1l)
                .name("Ozon")
                .contactInfo("ozn@gmail.com")
                .registrationDate(LocalDateTime.of(2024, 10, 12, 00, 00))
                .build();
        Long totalAmount1 = 500L;
        Long totalAmount2 = 300L;
        LocalDate startDate = LocalDate.of(2024, 10, 12);
        LocalDate endDate = LocalDate.of(2024, 10, 12);

        Object[] result1 = new Object[]{seller1, totalAmount1};
        Object[] result2 = new Object[]{seller2, totalAmount2};

        SellerTopDto expectedDto1 = new SellerTopDto(seller1.getId(),
                seller1.getName(),
                seller1.getContactInfo(),
                totalAmount1);
        SellerTopDto expectedDto2 = new SellerTopDto(seller2.getId(),
                seller2.getName(),
                seller2.getContactInfo(),
                totalAmount2);

        when(transactionRepository.findSellerMin(startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59),
                700l))
                .thenReturn(List.of(result1, result2));
        when(sellerMapper.map(seller1, totalAmount1)).thenReturn(expectedDto1);
        when(sellerMapper.map(seller2, totalAmount2)).thenReturn(expectedDto2);

        List<SellerTopDto> actualDtos = analyticService.sellersByAmountLessThan(startDate, endDate, 700L);

        assertEquals(2, actualDtos.size(), "Должно вернуть 2 продавцов");
        assertTrue(actualDtos.contains(expectedDto1));
        assertTrue(actualDtos.contains(expectedDto2));
    }

    @Test
    public void testSellersByAmountLessThanWhenNoSellersFound() {
        LocalDate startDate = LocalDate.of(2024, 10, 12);
        LocalDate endDate = LocalDate.of(2024, 10, 12);

        when(transactionRepository.findSellerMin(startDate.atStartOfDay(), endDate.atTime(23, 59, 59), 400L))
                .thenReturn(Collections.emptyList());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            analyticService.sellersByAmountLessThan(startDate, endDate, 400L);
        });
        assertEquals("No sellers found with amount less than 400 in the period from 2024-10-12 to 2024-10-12", thrown.getMessage());
    }

    @Test
    public void testGetBestTransactionPeriodWhenTransactionsFound() {
        Long sellerId = 1L;
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 7);

        Transaction transaction1 = new Transaction();
        transaction1.setTransactionDate(LocalDateTime.of(2024, 10, 5, 10, 0));

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionDate(LocalDateTime.of(2024, 10, 5, 12, 0));

        Transaction transaction3 = new Transaction();
        transaction3.setTransactionDate(LocalDateTime.of(2024, 10, 6, 14, 0));

        when(transactionRepository.findBySellerIdAndTransactionDateBetween(eq(sellerId),
                eq(LocalDateTime.of(2024, 10, 1, 0, 0)),
                eq(LocalDateTime.of(2024, 10, 7, 23, 59, 59))))
                .thenReturn(List.of(transaction1, transaction2, transaction3));

        SellerPeakTransactionDto actualDto = analyticService.getBestTransactionPeriod(sellerId, startDate, endDate);

        assertNotNull(actualDto, "Должен вернуть объект SellerPeakTransactionDto");
        assertEquals(LocalDateTime.of(2024, 10, 5, 11, 0), actualDto.periodDate(), "Должен вернуть пик транзакций на 5 октября");
        assertEquals(2L, actualDto.count());
    }

    @Test
    public void testGetBestTransactionPeriodWhenNoTransactionsFound() {
        Long sellerId = 1L;
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 31);

        when(transactionRepository.findBySellerIdAndTransactionDateBetween(eq(sellerId),
                eq(LocalDateTime.of(2024, 10, 1, 0, 0)),
                eq(LocalDateTime.of(2024, 10, 31, 23, 59, 59))))
                .thenReturn(Collections.emptyList());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            analyticService.getBestTransactionPeriod(sellerId, startDate, endDate);
        });

        assertEquals("No transactions found for seller within the period", thrown.getMessage(), "Должно вернуть корректное сообщение об ошибке");
    }

}
