package shift.lab.crm.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import shift.lab.crm.core.entity.enums.Operation;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "seller_history")
public class SellerHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "seller_id")
    private Long sellerId;
    @Column(name = "name")
    private String name;
    @Column(name = "contact_info")
    private String contactInfo;
    @Column(name = "registration_date",updatable = false)
    private LocalDateTime registrationDate;
    @Enumerated(EnumType.STRING)
    private Operation operation;
    @Column(name = "change_data")
    @UpdateTimestamp
    private LocalDateTime changeData;

}
