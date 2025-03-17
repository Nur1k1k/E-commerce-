package com.webpractice.service;

import com.webpractice.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProducts();
    Product createProduct(String title, String description);
    Optional<Product> findProduct(Integer productId);
    Product updateProduct(Integer id, String title, String description);
    void deleteProduct(Integer id);
}
