package shift.lab.crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shift.lab.crm.api.Dto.SellerCreatetDto;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.api.Dto.SellerResponseUpdateDto;
import shift.lab.crm.api.Dto.SellerUpdateDto;
import shift.lab.crm.api.controller.SellerController;
import shift.lab.crm.core.exception.NotFoundException;
import shift.lab.crm.core.service.SellerService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SellerControllerTest {

    @InjectMocks
    private SellerController sellerController;
    @Mock
    private SellerService sellerService;

    @Test
    void testGetAllSellerSuccess() {
        List<SellerResponseDto> sellers = Arrays.asList(
                new SellerResponseDto(1L, "Voody", "+79234567890", LocalDateTime.now()),
                new SellerResponseDto(2L, "Alice", "user@example.com", LocalDateTime.now())
        );

        when(sellerService.listAllSeller()).thenReturn(sellers);

        List<SellerResponseDto> result = sellerController.getAllSeller();

        verify(sellerService, times(1)).listAllSeller();

        assertEquals(2, result.size());
        assertEquals("Voody", result.get(0).name());
        assertEquals("Alice", result.get(1).name());
        assertEquals("+79234567890", result.get(0).contactInfo());
        assertEquals("user@example.com", result.get(1).contactInfo());
    }

    @Test
    void testGetInfoOneSellerSuccess() {
        SellerResponseDto seller = new SellerResponseDto(
                1L,
                "Voody",
                "+79234567890",
                LocalDateTime.of(2024, 10, 14, 00, 00, 0)
        );

        when(sellerService.infoSeller(1L)).thenReturn(seller);

        SellerResponseDto result = sellerController.getInfoOneSeller(1L);

        verify(sellerService, times(1)).infoSeller(1L);

        assertEquals("Voody", result.name());
        assertEquals("+79234567890", result.contactInfo());
        assertEquals(LocalDateTime.of(2024, 10, 14, 00, 00, 0), result.registrationDate());

    }

    @Test
    void testCreateSellerSuccess() {
        SellerCreatetDto createDto = new SellerCreatetDto("Voody", "+79234567890");
        SellerResponseDto createdSeller = new SellerResponseDto(
                1L,
                "Voody",
                "+79234567890",
                LocalDateTime.of(2024, 10, 14, 00, 00, 0)
        );
        when(sellerService.create(createDto)).thenReturn(createdSeller);

        SellerResponseDto result = sellerController.createSeller(createDto);

        verify(sellerService, times(1)).create(createDto);

        assertEquals("Voody", result.name());
        assertEquals("+79234567890", result.contactInfo());
        assertEquals(LocalDateTime.of(2024, 10, 14, 00, 00, 0), result.registrationDate());
    }

    @Test
    void testUpdateSellerSuccess() {
        SellerUpdateDto updateDto = new SellerUpdateDto(1L, "VadimUp", "+79234567891");
        SellerResponseUpdateDto updatedSeller = new SellerResponseUpdateDto(
                1L,
                "VadimUp",
                "+79234567891");

        when(sellerService.update(updateDto)).thenReturn(updatedSeller);

        SellerResponseUpdateDto result = sellerController.updateSeller(updateDto);

        verify(sellerService, times(1)).update(updateDto);

        assertEquals("VadimUp", result.name());
        assertEquals("+79234567891", result.contactInfo());
    }

    @Test
    void testRemoveSellerSuccess() {
        Long sellerId = 1L;

        doNothing().when(sellerService).deleteSeller(sellerId);

        sellerController.removeSeller(sellerId);

        verify(sellerService, times(1)).deleteSeller(sellerId);
    }


    @Test
    void testGetInfoOneSellerNotFound() {
        Long nonExistentSellerId = 999L;

        when(sellerService.infoSeller(nonExistentSellerId)).thenThrow(new NotFoundException("Seller not found"));

        Exception exception = assertThrows(NotFoundException.class, () -> {
            sellerController.getInfoOneSeller(nonExistentSellerId);
        });

        assertEquals("Seller not found", exception.getMessage());
        verify(sellerService, times(1)).infoSeller(nonExistentSellerId);
    }


}
