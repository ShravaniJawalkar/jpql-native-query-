package org.example.jpqlnativequery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressDto {
    private String email;
    private String userName;
    private String street;
    private String city;
}
