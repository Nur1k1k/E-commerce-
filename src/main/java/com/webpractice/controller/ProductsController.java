package com.webpractice.controller;

import com.webpractice.entity.Product;
import com.webpractice.controller.payload.NewProductPayload;
import com.webpractice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductsController {
    private final ProductService productService;

    @GetMapping("list")
    public String getAllProductsList(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "list";
    }

    @GetMapping("create")
    public String getNewProductPage() {
        return "new_product";
    }

    @PostMapping("create")
    public String createProduct(NewProductPayload payload) {
        Product product = productService.createProduct(payload.title(), payload.description());
        return "redirect:/api/v1/%d".formatted(product.getId());
    }

}