package shift.lab.crm.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.entity.SellerHistory;
import shift.lab.crm.core.entity.enums.Operation;
import shift.lab.crm.core.repository.SellerHistoryRepository;
import shift.lab.crm.core.service.SellerHistoryService;

@Log4j2
@Service
@RequiredArgsConstructor
public class SellerHistoryServiceImpl implements SellerHistoryService {

    private final SellerHistoryRepository sellerHistoryRepository;

    @Override
    public void recordHistory(Seller seller, Operation operation) {

        SellerHistory sellerHistory = SellerHistory.builder()
                .sellerId(seller.getId())
                .name(seller.getName())
                .contactInfo(seller.getContactInfo())
                .registrationDate(seller.getRegistrationDate())
                .operation(operation)
                .build();

        sellerHistoryRepository.save(sellerHistory);
        log.info("SellerHistory with ID: {} updated successfully", seller.getId());

    }
}