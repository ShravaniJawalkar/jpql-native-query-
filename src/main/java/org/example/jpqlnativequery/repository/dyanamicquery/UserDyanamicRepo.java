package org.example.jpqlnativequery.repository.dyanamicquery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.example.jpqlnativequery.model.User;
import org.example.jpqlnativequery.model.UserAddressDto;
import org.example.jpqlnativequery.model.UserOrderDto;
import org.example.jpqlnativequery.repository.specificationapi.UserSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDyanamicRepo {

    @PersistenceContext
    EntityManager em;

    public List<UserOrderDto> getUserDetailsByIdWithJoin(Long userId) {
        String sql = "SELECT u.user_name, a.street, a.city, o.order_id, o.order_description FROM user_dummy2 u LEFT JOIN address_dummy2 a ON u.address_id = a.address_id LEFT JOIN order_dummy2 o ON u.user_id = o.user_id WHERE u.user_id = :userId";
        Query query = em.createNativeQuery(sql);
        query.setParameter("userId", userId);
        List<Object[]> result = (List<Object[]>) query.getResultList();
        if (result.isEmpty()) {
            return null; // or throw an exception if no user found
        }
        List<UserOrderDto> userOrderDtos = new ArrayList<>();
        result.forEach(r -> {
            UserOrderDto userOrderDto = new UserOrderDto((String) r[0], (String) r[1], (String) r[2], r[3] != null ? ((Number) r[3]).longValue() : null, (String) r[4]);
            userOrderDtos.add(userOrderDto);
        });
        return userOrderDtos;
    }

    public List<UserOrderDto> getUserDetailsByIdWithJoin(Long userId, PageRequest pageable) {
        //String sql = "SELECT u.user_name, a.street, a.city, o.order_id, o.order_description FROM user_dummy2 u LEFT JOIN address_dummy2 a ON u.address_id = a.address_id LEFT JOIN order_dummy2 o ON u.user_id = o.user_id WHERE u.user_id = :userId OFFSET :offset LIMIT :limit";
        StringBuilder sql = new StringBuilder("SELECT u.user_name, a.street, a.city, o.order_id, o.order_description FROM user_dummy2 u").
                append(" LEFT JOIN address_dummy2 a ON u.address_id = a.address_id LEFT JOIN order_dummy2 o ON u.user_id = o.user_id").
                append(" WHERE u.user_id = :userId ").
                append("OFFSET :offset LIMIT :limit");
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("userId", userId);
        query.setParameter("offset", pageable.getOffset());
        query.setParameter("limit", pageable.getPageSize());
        List<Object[]> result = (List<Object[]>) query.getResultList();
        if (result.isEmpty()) {
            return null; // or throw an exception if no user found
        }
        List<UserOrderDto> userOrderDtos = new ArrayList<>();
        result.forEach(r -> {
            UserOrderDto userOrderDto = new UserOrderDto((String) r[0], (String) r[1], (String) r[2], r[3] != null ? ((Number) r[3]).longValue() : null, (String) r[4]);
            userOrderDtos.add(userOrderDto);
        });
        return userOrderDtos;
    }
    //criteria builder query which support JPQL

    public User getUserDetails(Long userId, PageRequest pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
        var root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        criteriaQuery.where(cb.equal(root.get("id"), userId));
        TypedQuery<User> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<User> results = typedQuery.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public List<UserAddressDto> getUserDetailsWithJoinById(Long userId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        var root = criteriaQuery.from(User.class);
        var addressJoin = root.join("address", JoinType.LEFT);
        criteriaQuery.multiselect(root.get("email"), root.get("userName"), addressJoin.get("street"), addressJoin.get("city"));
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), userId));
        TypedQuery<Object[]> query = em.createQuery(criteriaQuery);
        List<Object[]> results = query.getResultList();
        List<UserAddressDto> userOrderDtos = new ArrayList<>();
        if (results.isEmpty()) {
            throw new RuntimeException("No user found with ID: " + userId);
        } else {
            results.forEach(r -> {
                UserAddressDto userOrderDto = new UserAddressDto((String) r[0], (String) r[1], (String) r[2], (String) r[3]);
                userOrderDtos.add(userOrderDto);
            });
        }
        return userOrderDtos;
    }

    public List<User> getUserByNameInOrder(String userName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> user = cb.createQuery(User.class);
        var root = user.from(User.class);
        user.select(root);
        Predicate predicate = cb.equal(root.get("userName"), userName);
        user.where(predicate);
        user.orderBy(cb.desc(root.get("userName")));
        TypedQuery<User> typedQuery = em.createQuery(user);
        List<User> results = typedQuery.getResultList();
        if (results.isEmpty()) {
            throw new RuntimeException("No user found with name: " + userName);
        }
        return results;
    }

    public boolean hasUser(String userName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> user = cb.createQuery(User.class);
        Root<User> root = user.from(User.class);
        UserSpecification userSpecification = new UserSpecification();
        Specification<User> userSpecification1 = userSpecification.hasUserName(userName).and(userSpecification.joinAddress());
        Predicate predicate = userSpecification1.toPredicate(root, user, cb);
        user.select(root).where(predicate);
        TypedQuery<User> typedQuery = em.createQuery(user);
        List<User> results = typedQuery.getResultList();

        return !results.isEmpty();
    }
}
