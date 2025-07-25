package org.example.jpqlnativequery.repository.specificationapi;

import jakarta.persistence.criteria.JoinType;
import org.example.jpqlnativequery.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public Specification<User> hasUserName(String userName) {
        return ((root, query, criteriaBuilder) -> {
            if (userName == null || userName.isEmpty()) {
                return criteriaBuilder.conjunction(); // No condition if userName is null or empty
            }
            return criteriaBuilder.equal(root.get("userName"), userName);
        });
    }

    public Specification<User> hasEmail(String email) {
        return ((root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.conjunction(); // No condition if email is null or empty
            }
            return criteriaBuilder.equal(root.get("email"), email);
        });
    }

    public Specification<User> joinAddress() {
        return (((root, query, criteriaBuilder) -> {
            root.join("address", JoinType.LEFT);
            return criteriaBuilder.conjunction();
        }));
    }

    public Specification<User> hasStreet(String street) {
        return ((root, query, criteriaBuilder) -> {
            if (street == null || street.isEmpty()) {
                return criteriaBuilder.conjunction(); // No condition if street is null or empty
            }
            return criteriaBuilder.equal(root.get("address").get("street"), street);
        });
    }
}
