package com.mesh.bestore.models;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
public class ProductDto {
 @NotEmpty(message = "Product name is required")
    private String name;
    @NotEmpty(message = "Product description is required")
    private String description;
    @NotEmpty(message = "Product category is required")
    private String category;
    @Min(0)
    private double price;
    @size (min = 10, messaage = "the description must be at least 10 characters long")
    @size(max = 200, message = "the description must nnot exceed 200 characters long")
    private String description;

    private MultipartFile image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

 

    
}
