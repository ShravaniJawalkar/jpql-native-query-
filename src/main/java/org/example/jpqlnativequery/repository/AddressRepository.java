package org.example.jpqlnativequery.repository;

import org.example.jpqlnativequery.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "insert into address_dummy2 (address_id, street, city, state, zipcode) values (:address_id, :street, :city, :state, :zipcode)", nativeQuery = true)
    void insertAddress(@Param("address_id")Long addressId, @Param("street") String street, @Param("city") String city, @Param("state") String state, @Param("zipcode") String zip);
}

