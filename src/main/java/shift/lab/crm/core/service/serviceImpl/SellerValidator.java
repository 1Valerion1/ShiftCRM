package shift.lab.crm.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import shift.lab.crm.api.Dto.SellerCreatetDto;
import shift.lab.crm.core.exception.ConflictException;
import shift.lab.crm.core.exception.NotFoundException;
import shift.lab.crm.core.repository.SellerRepository;

@Log4j2
@RequiredArgsConstructor
@Component
public class SellerValidator {

    private final SellerRepository sellerRepository;

    public void validateUniqueness(SellerCreatetDto sellerCreateDto) {
        if (sellerRepository.existsByName(sellerCreateDto.name())) {
            log.warn("Attempt to create seller with existing name: {}", sellerCreateDto.name());
            throw new ConflictException("Seller with name already exists.");
        }
        if (sellerRepository.existsByContactInfo(sellerCreateDto.contactInfo())) {
            log.warn("Attempt to create seller with existing contact info: {}", sellerCreateDto.contactInfo());
            throw new ConflictException("Seller with contact info already exists.");
        }
    }

    public void validateExistence(Long id) {
        if (!sellerRepository.existsById(id)) {
            log.error("Seller with ID: {} not found", id);
            throw new NotFoundException("Seller with ID " + id + " not found");
        }
    }
}