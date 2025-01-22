package com.mesh.bestore.models;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

public class ProductDto {

    @NotEmpty(message = "Product name is required")
    private String name;

    @NotEmpty(message = "Product brand is required")
    private String brand;

    @NotEmpty(message = "Product description is required")
    @Size(min = 10, message = "The description must be at least 10 characters long")
    @Size(max = 200, message = "The description must not exceed 200 characters")
    private String description;

    @NotEmpty(message = "Product category is required")
    private String category;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private double price;

    private MultipartFile image;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
