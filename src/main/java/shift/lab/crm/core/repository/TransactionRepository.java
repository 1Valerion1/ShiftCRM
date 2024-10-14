package shift.lab.crm.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shift.lab.crm.core.entity.Transaction;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
