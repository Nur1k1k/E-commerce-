package com.webpractice.controller;

import com.webpractice.controller.payload.UpdateProductPayload;
import com.webpractice.entity.Product;
import com.webpractice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/{productId:\\d+}")
public class ProductController {
    private final ProductService productService;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return productService.findProduct(productId).orElseThrow();
    }

    @GetMapping
    public String getProductPage() {
        return "product";
    }

    @GetMapping("edit")
    public String editProductPage() {
        return "edit";
    }

    @PostMapping("edit")
    public String editProduct(@ModelAttribute("product") Product product, UpdateProductPayload payload) {
        productService.updateProduct(product.getId(), payload.title(), payload.description());
        return "redirect:/api/v1/%d".formatted(product.getId());
    }

    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
        productService.deleteProduct(product.getId());
        return "redirect:/api/v1/list";
    }

}
