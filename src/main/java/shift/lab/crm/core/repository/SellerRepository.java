package shift.lab.crm.core.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shift.lab.crm.core.entity.Seller;
@Repository
public interface SellerRepository extends JpaRepository<Seller, Long>{

    boolean existsById(Long id);
    boolean existsByName(String name);
    boolean existsByContactInfo(String contactInfo);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Seller s SET s.name = :name WHERE s.id = :id")
    int updateSellerName(@Param("id") Long id, @Param("name") String name);

    @Transactional
    @Modifying(clearAutomatically = true)

    @Query("UPDATE Seller s SET s.contactInfo = :contactInfo WHERE s.id = :id")
    int updateSellerContactInfo(@Param("id") Long id, @Param("contactInfo") String contactInfo);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Seller s SET s.name = :name, s.contactInfo = :contactInfo WHERE s.id = :id")
    int updateSellerNameAndContactInfo(@Param("id") Long id, @Param("name") String name, @Param("contactInfo") String contactInfo);

}
