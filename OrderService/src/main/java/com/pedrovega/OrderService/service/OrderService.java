package com.pedrovega.OrderService.service;

import com.pedrovega.OrderService.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
}
