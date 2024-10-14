package shift.lab.crm.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import shift.lab.crm.core.entity.enums.Payment;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Transactions", schema = "crm")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    @ToString.Exclude
    private Seller seller;
    @Column(name = "amount")
    private Long amount;
    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private Payment paymentType;
    @Column(name = "transaction_date")
    @CreationTimestamp
    private LocalDateTime transactionDate;
}
