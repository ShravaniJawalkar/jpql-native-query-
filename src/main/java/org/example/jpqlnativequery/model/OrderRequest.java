package org.example.jpqlnativequery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @JsonProperty("order_description")
    private String description;
    @JsonProperty("products")
    private List<Product> products = new ArrayList<>();
}
