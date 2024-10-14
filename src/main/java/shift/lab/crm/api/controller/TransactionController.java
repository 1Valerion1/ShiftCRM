package shift.lab.crm.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shift.lab.crm.api.Dto.TransactionCreateDto;
import shift.lab.crm.api.Dto.TransactionResponseDto;
import shift.lab.crm.core.service.TransactionService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public TransactionResponseDto create(@RequestBody TransactionCreateDto createDto) {
        return transactionService.create(createDto);
    }

    @GetMapping("/all")
    public List<TransactionResponseDto> getListTransaction() {
        return transactionService.listAllTransaction();
    }

    @GetMapping("/all/{seller_id}")
    public List<TransactionResponseDto> getListTransactionSelller(@RequestParam Long seller_id) {
        return transactionService.listAllTransactionSeller(seller_id);
    }

    @GetMapping("/{trans_id}")
    public TransactionResponseDto getTransaction(@RequestParam Long trans_id) {
        return transactionService.infoTransaction(trans_id);
    }


}
