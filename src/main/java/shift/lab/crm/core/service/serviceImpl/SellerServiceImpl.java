package shift.lab.crm.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shift.lab.crm.api.Dto.SellerCreatetDto;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.api.Dto.SellerUpdateDto;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.entity.SellerHistory;
import shift.lab.crm.core.entity.enums.Operation;
import shift.lab.crm.core.exception.SellerNotFoundException;
import shift.lab.crm.core.mapper.SellerMapper;
import shift.lab.crm.core.repository.SellerHistoryRepository;
import shift.lab.crm.core.repository.SellerRepository;
import shift.lab.crm.core.service.SellerService;

import java.util.List;


@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final SellerHistoryRepository sellerHistoryRepository;
    private final SellerMapper sellerMapper;

    @Override
    public SellerResponseDto create(SellerCreatetDto sellerCreatetDto) {
        //  добавить проверки что не тоже имя или номер/почта

        Seller savedSeller = saveSeller(sellerCreatetDto);

        // Сохранение истории
        saveSellerHistory(savedSeller, Operation.CREATE);

        return sellerMapper.map(savedSeller);
    }

    private Seller saveSeller(SellerCreatetDto sellerCreatetDto) {
        Seller seller = Seller.builder()
                .name(sellerCreatetDto.name())
                .contactInfo(sellerCreatetDto.contactInfo())
                .build();

        return sellerRepository.save(seller);
    }

    private void saveSellerHistory(Seller savedSeller, Operation operation) {
        SellerHistory sellerHistory = SellerHistory.builder()
                .sellerId(savedSeller.getId())
                .name(savedSeller.getName())
                .contactInfo(savedSeller.getContactInfo())
                .registrationDate(savedSeller.getRegistrationDate())
                .operation(operation)
                .build();

        sellerHistoryRepository.save(sellerHistory);
    }

    @Override
    public List<SellerResponseDto> listAllSeller() {
        return sellerMapper.map(sellerRepository.findAll());
    }

    @Override
    public SellerResponseDto infoSeller(Long id) {
        return sellerRepository.findById(id)
                .map(sellerMapper::map)
                .orElseThrow(() -> new SellerNotFoundException("Продавец с id " + id + " не найден"));    }

    @Override
    public SellerResponseDto update(SellerUpdateDto sellerUpdateDto) {
        return null;
    }

    @Override
    public void deleteSeller(Long id) {

    }
}
