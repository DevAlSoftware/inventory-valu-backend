package com.company.inventory.categories.services;

import com.company.inventory.categories.response.CategoryResponseRest;
import org.springframework.http.ResponseEntity;

public interface ICategoryService {

    public ResponseEntity<CategoryResponseRest> search();
}
