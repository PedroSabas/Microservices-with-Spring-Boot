package com.pedrovega.OrderService.repository;

import com.pedrovega.OrderService.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends IGenericRepository<Order, Long>{
}
