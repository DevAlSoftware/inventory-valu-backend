package com.company.inventory.services;

import com.company.inventory.response.CategoryResponseRest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements ICategoryService{
    @Override
    public ResponseEntity<CategoryResponseRest> search() {
        return null;
    }
}
