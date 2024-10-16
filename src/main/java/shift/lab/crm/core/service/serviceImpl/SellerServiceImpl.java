package shift.lab.crm.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.lab.crm.api.Dto.SellerCreatetDto;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.api.Dto.SellerUpdateDto;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.entity.enums.Operation;
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

    @Override
    public SellerResponseDto create(SellerCreatetDto sellerCreatetDto) {
        log.info("Creating seller with name: {}", sellerCreatetDto.name());

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
        Seller seller = getSellerById(id);
        return sellerMapper.map(seller);
    }

    @Override
    @Transactional
    public SellerResponseDto update(SellerUpdateDto sellerUp) {
        log.info("Updating seller with ID: {}", sellerUp.id());
        Seller seller = getSellerById(sellerUp.id());

        if (sellerUp.name() != null) {
            seller.setName(sellerUp.name());
            log.debug("Updated seller name to: {}", sellerUp.name());
        }
        if (sellerUp.contactInfo() != null) {
            seller.setContactInfo(sellerUp.contactInfo());
            log.debug("Updated seller contactInfo to: {}", sellerUp.contactInfo());
        }

        Seller savedSeller = sellerRepository.save(seller);
        sellerHistoryService.recordHistory(savedSeller, Operation.UPDATE);
        log.info("Seller with ID: {} updated successfully", savedSeller.getId());

        return sellerMapper.map(savedSeller);
    }

    public Seller getSellerById(Long id) {
        if (id == null) {
            log.error("Seller ID is null");
            throw new NotFoundException("Seller with ID " + id + " does not exist");
        }

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
        Seller seller = getSellerById(id);
        sellerRepository.deleteById(id);
        sellerHistoryService.recordHistory(seller, Operation.DELETE);
        log.info("Seller with ID: {} deleted successfully", id);
    }
}
