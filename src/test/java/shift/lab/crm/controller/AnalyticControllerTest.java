package shift.lab.crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shift.lab.crm.api.Dto.SellerPeakTransactionDto;
import shift.lab.crm.api.Dto.SellerTopDto;
import shift.lab.crm.api.controller.AnalyticController;
import shift.lab.crm.core.service.AnalyticService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticControllerTest {
    @InjectMocks
    private AnalyticController analyticController;
    @Mock
    private AnalyticService analyticService;

    @Test
    public void testGetSuccessSeller_Positive() {
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 31);
        SellerTopDto mockedSeller = new SellerTopDto(1L, "Voody", "+79234567890", 12000L);

        when(analyticService.successSeller(startDate, endDate)).thenReturn(mockedSeller);

        SellerTopDto result = analyticController.getSuccessSeller(startDate, endDate);

        assertNotNull(result);
        assertEquals("Voody", result.name());
        assertEquals(12000L, result.sumAmount());
    }

    @Test
    public void testGetSuccessSeller_Negative() {
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 31);

        when(analyticService.successSeller(startDate, endDate)).thenReturn(null);

        SellerTopDto result = analyticController.getSuccessSeller(startDate, endDate);

        assertNull(result);
    }

    @Test
    public void testGetSellersMin_Positive() {
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 31);
        long sumMin = 1000;
        List<SellerTopDto> sellers = List.of(new SellerTopDto(2L, "JohnDoe", "john@example.com", 900L));

        when(analyticService.sellersByAmountLessThan(startDate, endDate, sumMin)).thenReturn(sellers);

        List<SellerTopDto> result = analyticController.getSellersMin(startDate, endDate, sumMin);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("JohnDoe", result.get(0).name());
    }

    @Test
    public void testGetSellersMin_Negative() {
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 31);
        long sumMin = 1000;

        when(analyticService.sellersByAmountLessThan(startDate, endDate, sumMin)).thenReturn(Collections.emptyList());

        List<SellerTopDto> result = analyticController.getSellersMin(startDate, endDate, sumMin);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetPeak_Positive() {
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 31);
        long sellerId = 1L;
        SellerPeakTransactionDto peakData = new SellerPeakTransactionDto(LocalDateTime.of(2024, 10, 14, 0, 0), 150L);

        when(analyticService.getBestTransactionPeriod(sellerId, startDate, endDate)).thenReturn(peakData);

        SellerPeakTransactionDto result = analyticController.getPeak(startDate, endDate, sellerId);

        assertNotNull(result);
        assertEquals(150L, result.count());
    }

    @Test
    public void testGetPeak_Negative() {
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 31);
        long sellerId = 99L;

        when(analyticService.getBestTransactionPeriod(sellerId, startDate, endDate)).thenReturn(null);

        SellerPeakTransactionDto result = analyticController.getPeak(startDate, endDate, sellerId);

        assertNull(result);
    }

}

