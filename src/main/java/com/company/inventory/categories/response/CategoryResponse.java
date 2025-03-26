package com.company.inventory.categories.response;

import com.company.inventory.categories.model.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse {

    private List<Category> category;
}
