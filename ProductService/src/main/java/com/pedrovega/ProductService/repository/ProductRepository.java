package com.pedrovega.ProductService.repository;

import com.pedrovega.ProductService.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends IGenericRepository<Product, Long> {
}
