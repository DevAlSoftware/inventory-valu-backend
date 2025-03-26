package com.company.inventory.categories.controller;

import com.company.inventory.categories.response.CategoryResponseRest;
import com.company.inventory.categories.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CategoryRestController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<CategoryResponseRest> searchCategories () {
        ResponseEntity<CategoryResponseRest> response = categoryService.search();
        return response;
    }
}
