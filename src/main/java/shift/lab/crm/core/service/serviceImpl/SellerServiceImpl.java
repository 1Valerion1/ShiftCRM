package shift.lab.crm.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shift.lab.crm.api.Dto.SellerCreatetDto;
import shift.lab.crm.api.Dto.SellerResponseDto;
import shift.lab.crm.api.Dto.SellerUpdateDto;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.entity.enums.Operation;
import shift.lab.crm.core.exception.NotFoundException;
import shift.lab.crm.core.mapper.SellerMapper;
import shift.lab.crm.core.repository.SellerRepository;
import shift.lab.crm.core.service.SellerService;

import java.util.List;


@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;
    private final SellerHistoryService sellerHistoryService;


    @Override
    public SellerResponseDto create(SellerCreatetDto sellerCreatetDto) {
        //  добавить проверки что не тоже имя или номер/почта
        Seller savedSeller = saveSeller(sellerCreatetDto);
        // Сохранение истории
        sellerHistoryService.recordHistory(savedSeller, Operation.CREATE);

        return sellerMapper.map(savedSeller);
    }

    private Seller saveSeller(SellerCreatetDto sellerCreatetDto) {
        Seller seller = Seller.builder()
                .name(sellerCreatetDto.name())
                .contactInfo(sellerCreatetDto.contactInfo())
                .build();

        return sellerRepository.save(seller);
    }


    @Override
    public List<SellerResponseDto> listAllSeller() {
        return sellerMapper.map(sellerRepository.findAll());
    }

    @Override
    public SellerResponseDto infoSeller(Long id) {
        return sellerRepository.findById(id)
                .map(sellerMapper::map)
                .orElseThrow(() -> new NotFoundException("Продавец с id " + id + " не найден"));
    }

    @Override
    public SellerResponseDto update(SellerUpdateDto sellerUp) {
        if (sellerUp.id() == null) {
            throw new NotFoundException("Продавец с id = null");
        }
        // Получаем существующую сущность продавца по ID
        Seller seller = getSellerById(sellerUp.id());

        // Обновление полей сущности
        if (sellerUp.name() != null) {
            seller.setName(sellerUp.name());
        }
        if (sellerUp.contactInfo() != null) {
            seller.setContactInfo(sellerUp.contactInfo());
        }
        Seller savedSeller = sellerRepository.save(seller);
        sellerHistoryService.recordHistory(savedSeller, Operation.UPDATE);

        return sellerMapper.map(savedSeller);

    }


    private Seller getSellerById(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Продавец с id " + id + " не найден"));
    }

    @Override
    public void deleteSeller(Long id) {
        if (id == null || !sellerRepository.existsById(id)) {
            throw new NotFoundException("Продавец с" + id + " не существует");
        }
        // Проверяем что такой продаве есть
        if (!sellerRepository.existsById(id)) {
            throw new NotFoundException("Данынй пользователь не найден");
        }
        Seller seller = getSellerById(id);

        sellerRepository.deleteById(id);
        sellerHistoryService.recordHistory(seller, Operation.DELETE);
    }
}
