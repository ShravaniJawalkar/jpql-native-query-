package org.example.jpqlnativequery.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedNativeQuery(name = "User.findUserDetails",
        query = "select u.user_name, a.street, a.city from user_dummy2 u inner join address_dummy2 a on u.address_id = a.address_id where u.user_id = :userId",
        resultSetMapping = "UserDetailsMapping")
@SqlResultSetMapping(name = "UserDetailsMapping",
        classes = {
                @ConstructorResult(targetClass = UserDto.class,
                        columns = {
                                @ColumnResult(name = "user_name", type = String.class),
                                @ColumnResult(name = "street", type = String.class),
                                @ColumnResult(name = "city", type = String.class)
                        })
        })
@Entity
@Table(name = "user_dummy2")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "email")
    private String email;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    private Address address;




}

