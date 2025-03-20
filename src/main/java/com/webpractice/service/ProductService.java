package com.webpractice.service;

import com.webpractice.entity.Product;
import com.webpractice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findProduct(Integer id) {
        return productRepository.findById(id);
    }

    public void createProduct(String title, String description, String imageUrl, String category, Integer price) {
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setImageUrl(imageUrl);
        product.setCategory(category);
        product.setPrice(price);
        productRepository.save(product);
    }

    public void updateProduct(Integer id, String title, String description, String imageUrl, String category, Integer price) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));
        product.setTitle(title);
        product.setDescription(description);
        if (imageUrl != null) {
            product.setImageUrl(imageUrl);
        }
        product.setCategory(category);
        product.setPrice(price);
        productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}