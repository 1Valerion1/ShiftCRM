package shift.lab.crm.core.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shift.lab.crm.api.Dto.TransactionCreateDto;
import shift.lab.crm.api.Dto.TransactionResponseDto;
import shift.lab.crm.core.repository.TransactionRepository;
import shift.lab.crm.core.service.TransactionService;

import java.util.List;


@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    @Override
    public TransactionResponseDto create(TransactionCreateDto transactionCreateDto) {
        return null;
    }

    @Override
    public List<TransactionResponseDto> listAllTransaction() {
        return null;
    }

    @Override
    public List<TransactionResponseDto> listAllTransactionSeller(Long id) {
        return null;
    }

    @Override
    public TransactionResponseDto infoTransaction(Long id) {
        return null;
    }
}
