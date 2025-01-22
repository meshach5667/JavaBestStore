package com.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mesh.bestore.models.ProductDto;
import com.mesh.bestore.models.Products;
import com.mesh.bestore.services.ProductServices;

@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductServices productServices;

    @GetMapping({"/", ""})
    public String showProductsList(Model model) {
        List<Products> products = productServices.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/create")
    public String createProduct(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("product", productDto);
        return "products/createProduct";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult result) {
        if (productDto.getImage().isEmpty()) {
            result.addError(new FieldError("productDto", "image", "Image is required"));
        }
        if (result.hasErrors()) {
            return "products/createProduct";
        }

        // Save image
        MultipartFile image = productDto.getImage();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, uploadPath.resolve(storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            System.out.println("Error saving file: " + ex.getMessage());
            return "products/createProduct";
        }

        // Save product
        Products product = new Products();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setImageFileName(storageFileName);
        product.setCreated_at(createdAt);

        productServices.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam(name = "id") int id) {
        try {
            Products product = productServices.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());
            model.addAttribute("product", productDto);
            model.addAttribute("id", id);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/products";
        }
        return "products/editProduct";
    }

    @PostMapping("/edit")
    public String editProduct(@RequestParam(name = "id") int id, @Valid @ModelAttribute ProductDto productDto, BindingResult result) {
        if (result.hasErrors()) {
            return "products/editProduct";
        }

        try {
            Products product = productServices.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());

            MultipartFile image = productDto.getImage();
            if (!image.isEmpty()) {
                String storageFileName = new Date().getTime() + "_" + image.getOriginalFilename();
                String uploadDir = "public/images/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, uploadPath.resolve(storageFileName), StandardCopyOption.REPLACE_EXISTING);
                }
                product.setImageFileName(storageFileName);
            }

            productServices.save(product);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam(name = "id") int id) {
        try {
            Products product = productServices.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
            
            // Delete image
            String uploadDir = "public/images/";
            try {
                Files.delete(Paths.get(uploadDir + product.getImageFileName()));
            } catch (Exception e) {
                System.out.println("Error deleting file: " + e.getMessage());
            }
            
            // Delete product
            productServices.delete(product);
            productServices.deleteById(id);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return "redirect:/products";
    }
}    
 