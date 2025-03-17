package com.webpractice.controller;

import com.webpractice.entity.Product;
import com.webpractice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductsController {
    private final ProductService productService;

    // Получение всех товаров
    @GetMapping
    public String getAllProducts(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    // Форма создания товара
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "create-product";
    }

    // Создание товара
    @PostMapping
    public String createProduct(@ModelAttribute Product product) {
        productService.createProduct(product.getTitle(), product.getDescription());
        return "redirect:/products";
    }

    // Получение одного товара
    @GetMapping("/{productId}")
    public String getProduct(@PathVariable Integer productId, Model model) {
        Product product = productService.findProduct(productId)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));
        model.addAttribute("product", product);
        return "product-details";
    }

    // Форма редактирования
    @GetMapping("/{productId}/edit")
    public String showEditForm(@PathVariable Integer productId, Model model) {
        Product product = productService.findProduct(productId)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));
        model.addAttribute("product", product);
        return "edit-product";
    }

    // Обновление товара
    @PostMapping("/{productId}/edit")
    public String updateProduct(@PathVariable Integer productId, @ModelAttribute Product product) {
        productService.updateProduct(productId, product.getTitle(), product.getDescription());
        return "redirect:/products";
    }

    // Удаление товара
    @PostMapping("/{productId}/delete")
    public String deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return "redirect:/products";
    }
}
