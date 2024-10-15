package shift.lab.crm.core.service.serviceImpl;

import org.springframework.data.jpa.domain.Specification;
import shift.lab.crm.core.entity.Seller;

public class SellerSpecification {

    public static Specification<Seller> withId(Long id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Seller> withName(String name) {
        return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<Seller> withContactInfo(String contactInfo) {
        return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("contactInfo"), contactInfo);
    }
}