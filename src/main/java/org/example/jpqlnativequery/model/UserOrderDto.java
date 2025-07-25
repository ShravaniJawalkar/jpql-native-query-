package org.example.jpqlnativequery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderDto extends UserDto {
    private long orderId;
    private String description;

    public UserOrderDto(String username, String street, String city, Long orderId, String description) {
        super(username, street, city);
        this.orderId = orderId;
        this.description = description;
    }
}
