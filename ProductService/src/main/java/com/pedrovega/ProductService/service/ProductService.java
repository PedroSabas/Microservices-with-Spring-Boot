package com.pedrovega.ProductService.service;

import com.pedrovega.ProductService.model.ProductRequest;
import com.pedrovega.ProductService.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
