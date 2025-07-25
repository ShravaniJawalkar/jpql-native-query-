package org.example.jpqlnativequery.repository;

import org.example.jpqlnativequery.model.Order;
import org.example.jpqlnativequery.model.OrderDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o.description, p.name from Order o join o.products p where o.orderId = :orderId")
    List<Object[]> findOrderDetailsById(@Param("orderId") Long orderId);

    @Query("select new org.example.jpqlnativequery.model.OrderDto(o.description, p.name ) from Order o join o.products p where o.orderId = :orderId")
    List<OrderDto> findOrderDetailsDtoById(@Param("orderId") Long orderId);

    @Query(name = "Order.findAll")
    List<Order> findAllOrders();

    //flushAutomatically=true meaning any insert, update or delete operation pending changes in persistence context will be commited to database immediately before running the query.
    //clearAutomatically=true meaning the persistence context will be cleared after running the query. the purpose of it to remove any stale data in persistence context.
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Transactional
    @Query("update Order o set o.description= :description where o.orderId = :orderId")
    public int updateOrder(@Param("orderId") Long orderId, @Param("description") String description);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Transactional
    @Query("update Order o set o.user.id= :userId where o.orderId = :orderId")
    public int updateOrderUser(@Param("orderId") Long orderId, @Param("userId") Long userId);
}

