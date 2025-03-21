package com.webpractice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    @Column(length = 500)
    private String description;
    private String imageUrl;
    @Column(nullable = false)
    private String category;
    private Integer price;

}