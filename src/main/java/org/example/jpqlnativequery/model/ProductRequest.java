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
public class ProductRequest {
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("orders")
    private List<OrderRequest> orders = new ArrayList<>();

}
