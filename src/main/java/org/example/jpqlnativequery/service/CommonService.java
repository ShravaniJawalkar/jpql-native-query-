package org.example.jpqlnativequery.service;

import org.example.jpqlnativequery.model.*;
import org.example.jpqlnativequery.repository.AddressRepository;
import org.example.jpqlnativequery.repository.OrderRepository;
import org.example.jpqlnativequery.repository.ProductRepository;
import org.example.jpqlnativequery.repository.UserRepository;
import org.example.jpqlnativequery.repository.dyanamicquery.UserDyanamicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommonService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserDyanamicRepo userDyanamicRepo;

    public String createUser(UserRequest userRequest) {
        int result = 0;
        AddressRequest addressRequest = userRequest.getAddress();
        addressRepository.insertAddress(addressRequest.getAddressId(), addressRequest.getStreet(), addressRequest.getCity(), addressRequest.getState(), addressRequest.getZipcode());
        result = userRepository.insertUserNativeQuery(userRequest.getUserId(), userRequest.getUserName(),
                userRequest.getEmail(), addressRequest.getAddressId());
        if (result == 0) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user");
        } else {
            return "User created successfully with name: " + userRequest.getUserName();
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllOrders();
    }

    public Product saveProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getProductName());
        product.setOrders(saveOrdersToProduct(productRequest.getOrders(), product));
        return productRepository.save(product);
    }

    private List<Order> saveOrdersToProduct(List<OrderRequest> orders, Product product) {
        return orders.stream()
                .map(orderRequest -> {
                    Order order = new Order();
                    order.setDescription(orderRequest.getDescription());
                    order.addProduct(product);
                    return order;
                }).toList();
    }

    public Order saveOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setDescription(orderRequest.getDescription());
        order.setProducts(saveProductsToOrder(orderRequest.getProducts(), order));

        return orderRepository.save(order);
    }

    private List<Product> saveProductsToOrder(List<Product> products, Order order) {
        return products.stream().map(product -> {
            Product newProduct = new Product();
            newProduct.setName(product.getName());
            newProduct.addOrder(order);
            return newProduct;
        }).toList();
    }

    public OrderDto getOrderDetailsById(Long orderId) {
        List<Object[]> order = orderRepository.findOrderDetailsById(orderId);

        if (order == null || order.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Order not found");
        }
        OrderDto orderDto = new OrderDto();
        order.forEach(o -> {

            orderDto.setDescription(o[0].toString());
            orderDto.setProductName(o[1].toString());
        });

        return orderDto;
    }

    public List<OrderDto> getOrderDetailsDtoById(Long orderId) {
        return orderRepository.findOrderDetailsDtoById(orderId);
    }

    public List<OrderDto> getProductsNameAndOrderId() {
        Sort sort = Sort.by(
                List.of(Sort.Order.asc("name"), Sort.Order.desc("id"))
        );
        List<Object[]> products = productRepository.findProductNamesAndOrderIds(sort);
        if (products == null || products.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No products found");
        }
        List<OrderDto> orderDtos = new ArrayList<>();
        products.forEach(o -> {
            OrderDto orderDto = new OrderDto();
            orderDto.setProductName(o[0].toString());
            orderDto.setDescription(o[1].toString());
            orderDtos.add(orderDto);
        });
        return orderDtos;
    }

    public List<Product> getAllProducts() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"));
        List<Product> products = productRepository.findAllProducts(pageable);
        if (products.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No products found");
        }
        return products;
    }

    public String updateDescriptionById(Long orderId, String description) {
        int updatedCount = orderRepository.updateOrder(orderId, description);
        if (updatedCount == 0) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return "Description updated successfully for order ID: " + orderId;
    }

    public List<UserDto> getUserDetailsById(Long userId) {
        List<Object[]> userDetails = userRepository.findUserNameAndAddressByIdNativeQuery(userId);
        if (userDetails == null || userDetails.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }
        List<UserDto> userDtos = new ArrayList<>();
        userDetails.forEach(o -> {
            UserDto userDto = new UserDto();
            userDto.setUsername((String) o[0]);
            userDto.setStreet((String) o[1]);
            userDto.setCity((String) o[2]);
            userDtos.add(userDto);
        });
        return userDtos;
    }

    public List<UserDto> getUserDetailsByIdWithJoin(Long userId) {
        return userRepository.findUserDetailsById(userId);
    }

    public String updateOrderUser(Long orderId, Long userId) {
        int updatedCount = orderRepository.updateOrderUser(orderId, userId);
        if (updatedCount == 0) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return "Order user updated successfully for order ID: " + orderId;
    }

    public List<UserOrderDto> getUserOrderById(Long userId) {
        return userDyanamicRepo.getUserDetailsByIdWithJoin(userId);
    }


    public List<UserOrderDto> getUserOrderPageById(Long userId) {
        return userDyanamicRepo.getUserDetailsByIdWithJoin(userId,
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id")));
    }

    public User getUserDetailsByIdDynamicRepo(Long userId) {
        return userDyanamicRepo.getUserDetails(userId, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id")));
    }

    public List<UserAddressDto> getUserAddressById(Long userId) {
        return userDyanamicRepo.getUserDetailsWithJoinById(userId);
    }

    public List<User> getUserByName(String name) {
        return userDyanamicRepo.getUserByNameInOrder(name);
    }
}

