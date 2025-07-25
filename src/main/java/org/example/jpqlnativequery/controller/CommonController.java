package org.example.jpqlnativequery.controller;

import org.example.jpqlnativequery.model.*;
import org.example.jpqlnativequery.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommonController {
    @Autowired
    private CommonService commonService;

    @GetMapping("/product-orders")
    public List<OrderDto> getProducts() {
        return commonService.getProductsNameAndOrderId();
    }

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return commonService.getAllOrders();
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return commonService.getAllProducts();
    }

    //
    @GetMapping("/orders/{id}")
    public OrderDto getOrders(@PathVariable Long id) {
        return commonService.getOrderDetailsById(id);
    }

    @GetMapping("/orders/{id}/dto")
    public List<OrderDto> getOrdersDto(@PathVariable Long id) {
        return commonService.getOrderDetailsDtoById(id);
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody ProductRequest product) {
        return commonService.saveProduct(product);
    }

    @PostMapping("/orders")
    public Order createOrder(@RequestBody OrderRequest order) {
        return commonService.saveOrder(order);
    }

    @PutMapping("/orders/{id}/description")
    public String updateOrderDescription(@PathVariable Long id, @RequestParam("description") String description) {
        return commonService.updateDescriptionById(id, description);
    }

    @PostMapping("/users")
    public String createUser(@RequestBody UserRequest user) {
        return commonService.createUser(user);
    }

    @GetMapping("/users/{id}")
    public List<UserDto> getUserById(@PathVariable Long id) {
        return commonService.getUserDetailsById(id);
    }

    @GetMapping("/users-address/{id}")
    public List<UserDto> getUserDetailsByIdWithJoin(@PathVariable Long id) {
        return commonService.getUserDetailsByIdWithJoin(id);
    }

    @PutMapping("/orders/{id}/user/{userId}")
    public String updateOrderUser(@PathVariable Long id, @PathVariable Long userId) {
        return commonService.updateOrderUser(id, userId);
    }

    @GetMapping("/orders/user/{userId}")
    public List<UserOrderDto> getUserByUserId(@PathVariable Long userId) {
        return commonService.getUserOrderById(userId);
    }

    @GetMapping("/orders-user/{userId}")
    public List<UserOrderDto> getUserDetailsById(@PathVariable Long userId) {
        return commonService.getUserOrderPageById(userId);
    }

    @GetMapping("/orders-user-page/{userId}")
    public User getUserDetailsByIdDynamicRepo(@PathVariable Long userId) {
        return commonService.getUserDetailsByIdDynamicRepo(userId);
    }

    @GetMapping("/address-user-dto/{userId}")
    public List<UserAddressDto> getUserAddressById(@PathVariable Long userId) {
        return commonService.getUserAddressById(userId);
    }

    @GetMapping("/user-name")
    public List<User> getUserName(@RequestParam("name") String name) {
        return commonService.getUserByName(name);
    }

    @GetMapping("/has-user")
    public boolean hasUser(@RequestParam("name") String name) {
        return commonService.hasUser(name);
    }

    @GetMapping("/has-user-address")
    public List<User> hasUserAddress(@RequestParam("street") String street) {
        return commonService.hasUserByAddress(street);
    }
}

