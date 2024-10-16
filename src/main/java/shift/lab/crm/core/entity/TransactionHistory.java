package shift.lab.crm.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import shift.lab.crm.core.entity.enums.Operation;

import java.time.LocalDateTime;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
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
    private String paymentType;
    @Column(name = "transaction_date", updatable = false)
    private LocalDateTime transactionDate;
    @Enumerated(EnumType.STRING)
    private Operation operation;
    @Column(name = "change_data")
    @UpdateTimestamp
    private LocalDateTime changeData;
}
