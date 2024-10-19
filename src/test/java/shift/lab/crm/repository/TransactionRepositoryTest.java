package shift.lab.crm.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.entity.Transaction;
import shift.lab.crm.core.repository.SellerRepository;
import shift.lab.crm.core.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private SellerRepository sellerRepository;


    @Test
    void findTopSeller() {
        Seller seller1 = Seller.builder().name("Seller 1").build();
        Seller seller2 = Seller.builder().name("Seller 2").build();
        Seller sellerUpdate1 = sellerRepository.save(seller1);
        Seller sellerUpdate2 = sellerRepository.save(seller2);


        Transaction transaction1 = Transaction.builder().seller(sellerUpdate1).amount(100L).build();
        Transaction transaction2 = Transaction.builder().seller(sellerUpdate1).amount(200L).build();
        Transaction transaction3 = Transaction.builder().seller(sellerUpdate2).amount(150L).build();
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);

        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        List<Object[]> topSellers = transactionRepository.findTopSeller(startDate, endDate);

        assertEquals(2, topSellers.size());
        assertEquals("Seller 1", ((Seller) topSellers.get(0)[0]).getName());
        assertEquals(300L, topSellers.get(0)[1]);
        assertEquals("Seller 2", ((Seller) topSellers.get(1)[0]).getName());
        assertEquals(150L, topSellers.get(1)[1]);
    }

    @Test
    void findSellerMin() {
        Seller seller1 = Seller.builder().name("Seller 1").build();
        Seller seller2 = Seller.builder().name("Seller 2").build();
        seller1 = sellerRepository.save(seller1);
        seller2 = sellerRepository.save(seller2);

        Transaction transaction1 = Transaction.builder().seller(seller1).amount(100L).transactionDate(LocalDateTime.now()).build();
        Transaction transaction2 = Transaction.builder().seller(seller1).amount(50L).transactionDate(LocalDateTime.now()).build();
        Transaction transaction3 = Transaction.builder().seller(seller2).amount(200L).transactionDate(LocalDateTime.now()).build();
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        transactionRepository.save(transaction3);

        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        Long sumMin = 200L;

        List<Object[]> sellers = transactionRepository.findSellerMin(startDate, endDate, sumMin);

        assertEquals(1, sellers.size());
        assertEquals("Seller 1", ((Seller) sellers.get(0)[0]).getName());
        assertEquals(150L, sellers.get(0)[1]);
    }

    @Test
    void findBySellerId() {
        Seller seller = Seller.builder().name("Seller 1").build();
        seller = sellerRepository.save(seller);

        Transaction transaction1 = Transaction.builder().seller(seller).amount(100L).transactionDate(LocalDateTime.now()).build();
        Transaction transaction2 = Transaction.builder().seller(seller).amount(200L).transactionDate(LocalDateTime.now()).build();
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        List<Transaction> transactions = transactionRepository.findBySellerId(seller.getId());

        assertEquals(2, transactions.size());
        assertEquals(100L, transactions.get(0).getAmount());
        assertEquals(200L, transactions.get(1).getAmount());
    }

}
