package com.sena.tecmiecommercebackend.controller;

import com.sena.tecmiecommercebackend.dto.product.ProductDto;
import com.sena.tecmiecommercebackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam("q") String query) {
        List<ProductDto> results = productService.searchProducts(query);

        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(results);
    }
}