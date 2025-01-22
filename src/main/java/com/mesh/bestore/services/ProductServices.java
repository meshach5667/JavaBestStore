package com.mesh.bestore.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mesh.bestore.models.Products;

public interface ProductServices extends JpaRepository<Products, Integer> {
    
}
