package shift.lab.crm.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.lab.crm.api.Dto.SellerCreatetDto;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.api.Dto.SellerResponseUpdateDto;
import shift.lab.crm.api.Dto.SellerUpdateDto;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.entity.SellerHistory;
import shift.lab.crm.core.entity.TransactionHistory;
import shift.lab.crm.core.entity.enums.Operation;
import shift.lab.crm.core.exception.BadRequestException;
import shift.lab.crm.core.exception.NotFoundException;
import shift.lab.crm.core.mapper.SellerMapper;
import shift.lab.crm.core.repository.SellerRepository;
import shift.lab.crm.core.service.SellerHistoryService;
import shift.lab.crm.core.service.SellerService;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;
    private final SellerHistoryService sellerHistoryService;
    private final SellerValidator sellerValidator;

    @Override
    public SellerResponseDto create(SellerCreatetDto sellerCreatetDto) {
        log.info("Creating seller with name: {}", sellerCreatetDto.name());

        sellerValidator.validateUniqueness(sellerCreatetDto);

        Seller seller = Seller.builder()
                .name(sellerCreatetDto.name())
                .contactInfo(sellerCreatetDto.contactInfo())
                .build();

        Seller savedSeller = sellerRepository.save(seller);

        sellerHistoryService.recordHistory(savedSeller, Operation.CREATE);
        log.info("Seller created successfully with ID: {}", savedSeller.getId());
        return sellerMapper.map(savedSeller);

    }

    @Override
    public List<SellerResponseDto> listAllSeller() {
        log.info("Retrieving all sellers");
        List<Seller> sellers = sellerRepository.findAll();
        log.info("Found {} sellers", sellers.size());
        return sellerMapper.map(sellers);
    }

    @Override
    public SellerResponseDto infoSeller(Long id) {
        log.info("Retrieving seller with ID: {}", id);
        Seller seller = findSellerById(id);
        return sellerMapper.map(seller);
    }

    @Transactional
    @Override
    public SellerResponseUpdateDto update(SellerUpdateDto sellerUpdateDto) {
        log.info("Updating seller with ID: {}", sellerUpdateDto.id());

        sellerValidator.validateExistence(sellerUpdateDto.id());

        updateSellerData(sellerUpdateDto);

        Seller sellerUpdate = sellerRepository.getReferenceById(sellerUpdateDto.id());
        sellerHistoryService.recordHistory(sellerUpdate, Operation.UPDATE);
        log.info("Seller with ID: {} updated successfully", sellerUpdateDto.id());

        return sellerMapper.map(sellerUpdateDto);
    }


    private void updateSellerData(SellerUpdateDto sellerUpdateDto) {
        if (sellerUpdateDto.name() != null && sellerUpdateDto.contactInfo() != null) {
            sellerRepository.updateSellerNameAndContactInfo(sellerUpdateDto.id(), sellerUpdateDto.name(), sellerUpdateDto.contactInfo());
        } else if (sellerUpdateDto.name() != null) {
            sellerRepository.updateSellerName(sellerUpdateDto.id(), sellerUpdateDto.name());
        } else if (sellerUpdateDto.contactInfo() != null) {
            sellerRepository.updateSellerContactInfo(sellerUpdateDto.id(), sellerUpdateDto.contactInfo());
        } else {
            throw new BadRequestException("You need to pass at least one field for updating.");
        }
    }

    @Override
    public Seller findSellerById(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Seller with ID: {} not found", id);
                    return new NotFoundException("Seller with ID " + id + " not found");
                });
    }

    @Override
    @Transactional
    public void deleteSeller(Long id) {
        log.info("Attempting to delete seller with ID: {}", id);
        Seller seller = findSellerById(id);
        sellerRepository.deleteById(id);
        sellerHistoryService.recordHistory(seller, Operation.DELETE);
        log.info("Seller with ID: {} deleted successfully", id);
    }


}
