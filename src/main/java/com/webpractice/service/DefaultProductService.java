package com.webpractice.service;

import com.webpractice.entity.Product;
import com.webpractice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(String title, String description) {
        return productRepository.save(new Product(null, title, description));
    }

    @Override
    public Optional<Product> findProduct(Integer productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Product updateProduct(Integer id, String title, String description) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Продукт не найден"));

        product.setTitle(title);
        product.setDescription(description);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}
