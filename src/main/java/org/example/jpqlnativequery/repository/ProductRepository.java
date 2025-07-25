package org.example.jpqlnativequery.repository;

import org.example.jpqlnativequery.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p.name, o.orderId from Product p join p.orders o")
    List<Object[]> findProductNamesAndOrderIds(Sort sort);

    @Query("select p from Product p join fetch p.orders")
    List<Product> findAllProducts(Pageable pageable);
}

