package shift.lab.crm.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import shift.lab.crm.api.Dto.SellerCreatetDto;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.api.Dto.SellerResponseUpdateDto;
import shift.lab.crm.api.Dto.SellerUpdateDto;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.entity.enums.Operation;
import shift.lab.crm.core.exception.BadRequestException;
import shift.lab.crm.core.mapper.SellerMapper;
import shift.lab.crm.core.repository.SellerRepository;
import shift.lab.crm.core.service.SellerHistoryService;
import shift.lab.crm.core.service.serviceImpl.SellerServiceImpl;
import shift.lab.crm.core.service.serviceImpl.SellerValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @InjectMocks
    private SellerServiceImpl sellerService;
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private SellerHistoryService sellerHistoryService;
    @Mock
    private SellerMapper sellerMapper;
    @Mock
    private SellerValidator sellerValidator;

    @Test
    void testCreateSellerSuccess() {
        SellerCreatetDto sellerCreatetDto = new SellerCreatetDto("Voody", "+79234567890");
        Seller savedSeller = Seller.builder()
                .id(1L)
                .name("Voody")
                .contactInfo("+79234567890")
                .build();
        SellerResponseDto expectedResponse = new SellerResponseDto(1L, "Voody", "+79234567890", LocalDateTime.now());

        doNothing().when(sellerValidator).validateUniqueness(sellerCreatetDto);
        when(sellerRepository.save(any(Seller.class))).thenReturn(savedSeller);
        when(sellerMapper.map(any(Seller.class))).thenReturn(expectedResponse);

        SellerResponseDto responseDto = sellerService.create(sellerCreatetDto);

        assertNotNull(responseDto);
        assertEquals("Voody", responseDto.name());
        assertEquals("+79234567890", responseDto.contactInfo());
        verify(sellerHistoryService).recordHistory(savedSeller, Operation.CREATE);
    }

    @Test
    void testListAllSellers() {
        Seller seller1 = Seller.builder()
                .id(1L)
                .name("Seller1")
                .contactInfo("+79123456789")
                .build();
        Seller seller2 = Seller.builder()
                .id(2L)
                .name("Seller2")
                .contactInfo("+79234567890")
                .build();
        List<Seller> sellers = List.of(seller1, seller2);
        when(sellerRepository.findAll()).thenReturn(sellers);
        when(sellerMapper.map(anyList())).thenReturn(List.of(
                new SellerResponseDto(1L, "Seller1", "+79123456789", LocalDateTime.now()),
                new SellerResponseDto(2L, "Seller2", "+79234567890", LocalDateTime.now())
        ));

        List<SellerResponseDto> responseDtos = sellerService.listAllSeller();

        assertEquals(2, responseDtos.size());
        verify(sellerRepository).findAll();
    }

    @Test
    void testInfoSellerSuccess() {
        Seller seller = Seller.builder()
                .id(1L)
                .name("Voody")
                .contactInfo("+71234567890")
                .registrationDate(LocalDateTime.of(2024, 10, 10, 12, 12, 12))
                .build();

        SellerResponseDto expectedResponse = new SellerResponseDto(1L,
                "Voody",
                "+71234567890",
                LocalDateTime.of(2024, 10, 10, 12, 12, 12));

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(sellerMapper.map(seller)).thenReturn(expectedResponse);

        SellerResponseDto responseDto = sellerService.infoSeller(1L);

        assertNotNull(responseDto);
        assertEquals("Voody", responseDto.name());
        assertEquals("+71234567890", responseDto.contactInfo());
    }


    @Test
    void testUpdateSellerSuccess() {
        SellerUpdateDto sellerUpdateDto = new SellerUpdateDto(1L, "NewName", "+79876543210");
        Seller existingSeller = Seller.builder()
                .id(1L)
                .name("OldName")
                .contactInfo("+79234567890")
                .build();
        SellerResponseUpdateDto expectedResponse = new SellerResponseUpdateDto(1L, "NewName", "+79876543210");

        doNothing().when(sellerValidator).validateExistence(sellerUpdateDto.id());
        when(sellerRepository.getReferenceById(sellerUpdateDto.id())).thenReturn(existingSeller);
        when(sellerMapper.map(sellerUpdateDto)).thenReturn(expectedResponse);

        SellerResponseUpdateDto responseDto = sellerService.update(sellerUpdateDto);

        assertNotNull(responseDto);
        assertEquals(expectedResponse, responseDto);
        verify(sellerHistoryService).recordHistory(existingSeller, Operation.UPDATE);
        verify(sellerRepository).updateSellerNameAndContactInfo(1L, "NewName", "+79876543210");
    }

    @Test
    void testUpdateSellerWithOnlyName() {
        SellerUpdateDto sellerUpdateDto = new SellerUpdateDto(1L, "NewName", null);
        Seller existingSeller = Seller.builder()
                .id(1L)
                .name("OldName")
                .contactInfo("+79234567890")
                .build();
        SellerResponseUpdateDto expectedResponse = new SellerResponseUpdateDto(1L, "NewName", "+79234567890");

        doNothing().when(sellerValidator).validateExistence(sellerUpdateDto.id());
        when(sellerRepository.getReferenceById(sellerUpdateDto.id())).thenReturn(existingSeller);
        when(sellerMapper.map(sellerUpdateDto)).thenReturn(expectedResponse);

        SellerResponseUpdateDto responseDto = sellerService.update(sellerUpdateDto);

        assertNotNull(responseDto);
        assertEquals(expectedResponse, responseDto);
        verify(sellerHistoryService).recordHistory(existingSeller, Operation.UPDATE);
        verify(sellerRepository).updateSellerName(1L, "NewName");
    }

    @Test
    void testUpdateSellerWithOnlyContactInfo() {
        // Given
        SellerUpdateDto sellerUpdateDto = new SellerUpdateDto(1L, null, "+79876543210");
        Seller existingSeller = Seller.builder()
                .id(1L)
                .name("OldName")
                .contactInfo("+79234567890")
                .build();
        SellerResponseUpdateDto expectedResponse = new SellerResponseUpdateDto(1L, "OldName", "+79876543210");

        doNothing().when(sellerValidator).validateExistence(sellerUpdateDto.id());
        when(sellerRepository.getReferenceById(sellerUpdateDto.id())).thenReturn(existingSeller);
        when(sellerMapper.map(sellerUpdateDto)).thenReturn(expectedResponse);

        SellerResponseUpdateDto responseDto = sellerService.update(sellerUpdateDto);

        assertNotNull(responseDto);
        assertEquals(expectedResponse, responseDto);
        verify(sellerHistoryService).recordHistory(existingSeller, Operation.UPDATE);
        verify(sellerRepository).updateSellerContactInfo(1L, "+79876543210");
    }

    @Test
    void testUpdateSellerWithNoFields() {
        SellerUpdateDto sellerUpdateDto = new SellerUpdateDto(1L, null, null);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            sellerService.update(sellerUpdateDto);
        });

        assertEquals("You need to pass at least one field for updating.", exception.getMessage());
    }
    @Test
    void testDeleteSellerSuccess() {
        Long sellerId = 1L;
        Seller existingSeller = Seller.builder()
                .id(sellerId)
                .name("Voody")
                .contactInfo("+79234567890")
                .build();

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(existingSeller));

        sellerService.deleteSeller(sellerId);

        verify(sellerRepository).deleteById(sellerId);
        verify(sellerHistoryService).recordHistory(existingSeller, Operation.DELETE);
    }
}