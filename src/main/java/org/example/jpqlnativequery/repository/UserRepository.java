package org.example.jpqlnativequery.repository;

import org.example.jpqlnativequery.model.User;
import org.example.jpqlnativequery.model.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from user_dummy2 where user_id = :userId", nativeQuery = true)
    User findByIdNativeQuery(@Param("userId") Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "insert into user_dummy2 (user_id,user_name, email, address_id) values (:userId,:userName, :email, :address)", nativeQuery = true)
    int insertUserNativeQuery(
            @Param("userId") Long userId,
            @Param("userName") String userName,
            @Param("email") String email,
            @Param("address") Long address);

    @Query(value = "select user_name, street, city from user_dummy2 u join address_dummy2 a on u.address_id = a.address_id where user_id = :userId", nativeQuery = true)
    List<Object[]> findUserNameAndAddressByIdNativeQuery(@Param("userId") Long userId);

    @Query(name = "User.findUserDetails")
    List<UserDto> findUserDetailsById(@Param("userId") Long userId);
}

