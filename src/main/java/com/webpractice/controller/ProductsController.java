package com.webpractice.controller;

import com.webpractice.entity.Product;
import com.webpractice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductsController {
    private final ProductService productService;

    @GetMapping
    public String getAllProducts(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "create-product";
    }

    @GetMapping("/uploads/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(System.getProperty("user.dir"), "uploads", filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping
    public String createProduct(@ModelAttribute Product product, @RequestParam("image") MultipartFile imageFile) {
        try {
            if (!imageFile.isEmpty()) {
                String uploadDirPath = System.getProperty("user.dir") + "/uploads";
                File uploadFolder = new File(uploadDirPath);

                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }

                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                File file = new File(uploadFolder, fileName);

                imageFile.transferTo(file);

                product.setImageUrl(fileName);  // сохраняем только имя файла
            }

            productService.createProduct(product.getTitle(), product.getDescription(), product.getImageUrl(), product.getCategory(), product.getPrice());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла", e);
        }

        return "redirect:/products";
    }

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
    public String updateProduct(@PathVariable Integer productId,
                                @ModelAttribute Product product,
                                @RequestParam(value = "image", required = false) MultipartFile file) throws IOException {
        String uploadDirPath = System.getProperty("user.dir") + "/uploads";
        File uploadDir = new File(uploadDirPath);

        if (file != null && !file.isEmpty()) {
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            file.transferTo(new File(uploadDir, fileName));
            product.setImageUrl(fileName);
        } else {
            Product existingProduct = productService.findProduct(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));
            product.setImageUrl(existingProduct.getImageUrl());
        }

        productService.updateProduct(productId, product.getTitle(), product.getDescription(), product.getImageUrl(), product.getCategory(), product.getPrice());
        return "redirect:/products";
    }

    // Удаление товара
    @PostMapping("/{productId}/delete")
    public String deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return "redirect:/products";
    }
}