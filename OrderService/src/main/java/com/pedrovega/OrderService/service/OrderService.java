package com.pedrovega.OrderService.service;

import com.pedrovega.OrderService.model.OrderRequest;
import com.pedrovega.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
