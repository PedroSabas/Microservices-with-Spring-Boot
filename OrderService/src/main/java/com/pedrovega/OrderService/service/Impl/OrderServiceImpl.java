package com.pedrovega.OrderService.service.Impl;

import com.pedrovega.OrderService.entity.Order;
import com.pedrovega.OrderService.external.client.ProductService;
import com.pedrovega.OrderService.model.OrderRequest;
import com.pedrovega.OrderService.repository.OrderRepository;
import com.pedrovega.OrderService.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // From OpenFeign - external.client Interface
    @Autowired
    private ProductService productService;


    @Override
    public long placeOrder(OrderRequest orderRequest) {

        // Order Entity -> Save the data with Status Order Crated
        // Product Service - Block Products (Reduce the Quantity)
        // Payment Service -> Payments -> Success -> COMPLETE, Else
        // CANCELLED

        log.info("Placing Order Request: {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating Order with Status CREATED");
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order = orderRepository.save(order);

        log.info("Order Places Successfully with Order Id: {}", order.getId());
        return order.getId();
    }
}
