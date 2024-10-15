package shift.lab.crm.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "TransactionController",description = "Операции над транзакциями")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    @Operation(description = "Создает транзакцию")
    public TransactionResponseDto create(@RequestBody TransactionCreateDto createDto) {
        return transactionService.create(createDto);
    }

    @GetMapping("/all")
    @Operation(description = "Получаем список всех транзакций, которые есть")
    public List<TransactionResponseDto> getListTransaction() {
        return transactionService.listAllTransaction();
    }

    @GetMapping("/all/{seller_id}")
    @Operation(description = "Получаем список всех транзакций конткретного продавца")
    public List<TransactionResponseDto> getListTransactionSelller(@RequestParam Long seller_id) {
        return transactionService.listAllTransactionSeller(seller_id);
    }

    @GetMapping("/{trans_id}")
    @Operation(description = "Получаем 1 конкретную транзакцию")
    public TransactionResponseDto getTransaction(@RequestParam Long trans_id) {
        return transactionService.infoTransaction(trans_id);
    }


}
