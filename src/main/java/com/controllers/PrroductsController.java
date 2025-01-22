package com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mesh.bestore.models.ProductDto;
import com.mesh.bestore.models.Products;
import com.mesh.bestore.services.ProductServices;

@Controller
@RequestMapping("/products")

public class PrroductsController {
    @Autowired
    private ProductServices productServices;


    @GetMapping({"/", " "})

    public String showProductsList(Model model){
     List<Products> products = productServices.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", products); 
        return "products/index";
    }

    @GetMapping("/create")
    public String createProduct(Model model){
        ProductDto productDto = new ProductDto();
        model.addAttribute("product", productDto);
        return "products/createProduct";
    }

      
}
