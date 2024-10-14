package shift.lab.crm.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import shift.lab.crm.core.entity.enums.Operation;
import shift.lab.crm.core.entity.enums.Payment;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Transactions_history")
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "transaction_id")
    private Long transactionId;
    @Column(name = "seller_id")
    private Long sellerId;
    @Column(name = "amount")
    private Long amount;
    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private Payment paymentType;
    @Column(name = "transaction_date")
    @CreationTimestamp
    private LocalDateTime transactionDate;
    @Enumerated(EnumType.STRING)
    private Operation operation;
    @Column(name = "change_data")
    @UpdateTimestamp
    private LocalDateTime changeData;
}
