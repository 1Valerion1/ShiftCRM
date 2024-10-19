package shift.lab.crm.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shift.lab.crm.core.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t.seller, SUM(t.amount) as totalSales " +
            "FROM Transaction t " +
            "WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.seller " +
            "ORDER BY totalSales DESC")
    List<Object[]> findTopSeller(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t.seller, SUM(t.amount) as totalSales " +
            "FROM Transaction t " +
            "WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.seller " +
            "HAVING SUM(t.amount) < :sumMin")
    List<Object[]> findSellerMin(LocalDateTime startDate, LocalDateTime endDate, Long sumMin);

    List<Transaction> findBySellerId(Long sellerId);

    List<Transaction> findBySellerIdAndTransactionDateBetween(Long sellerId,LocalDateTime startDate, LocalDateTime endDate);
}
