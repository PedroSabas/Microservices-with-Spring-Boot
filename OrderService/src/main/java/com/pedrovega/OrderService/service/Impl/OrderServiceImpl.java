package com.pedrovega.OrderService.service.Impl;

import com.pedrovega.OrderService.entity.Order;
import com.pedrovega.OrderService.exception.CustomException;
import com.pedrovega.OrderService.external.client.PaymentService;
import com.pedrovega.OrderService.external.client.ProductService;
import com.pedrovega.OrderService.external.request.PaymentRequest;
import com.pedrovega.OrderService.external.response.PaymentResponse;
import com.pedrovega.OrderService.model.OrderRequest;
import com.pedrovega.OrderService.model.OrderResponse;
import com.pedrovega.OrderService.external.response.ProductResponse;
import com.pedrovega.OrderService.repository.OrderRepository;
import com.pedrovega.OrderService.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl  implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    // From OpenFeign - external.client Interface
    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public long placeOrder(OrderRequest orderRequest) {

        // Order Entity -> Save the data with Status Order Created
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

        log.info("Calling Payment Service to complete the payment");
        PaymentRequest paymentRequest
                = PaymentRequest
                .builder()
                .orderId(order.getId()) // Order id from paymentService recive a order id from order Service
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;

        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done Successfuully. Changing the Order Status to PLACED");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.error("Error occured in payment. Changing order status to PAYMENT_FAILED");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        log.info("Order Places Successfully with Order Id: {}", order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for Order Id : {}", orderId);
        Order order
                = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new CustomException("Order not found for the order Id:" + orderId,
                                            "NOT_FOUND", 404));


        log.info("Invoking Product service to fetch the product id: {}", order.getId());
        ProductResponse productResponse
                = restTemplate.getForObject(
                        "http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class);


        log.info("Getting payment information form the payment service");
        PaymentResponse paymentResponse
                = restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/" + order.getId(),
                PaymentResponse.class);

        OrderResponse.ProductDetails productDetails
                = OrderResponse.ProductDetails
                .builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .quantity(productResponse.getQuantity())
                .price(productResponse.getPrice())
                .build();

        OrderResponse.PaymentDetails paymentDetails
                = OrderResponse.PaymentDetails
                .builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentStatus(paymentResponse.getStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
    }
}
