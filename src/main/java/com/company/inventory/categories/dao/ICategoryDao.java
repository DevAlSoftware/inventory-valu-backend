package com.company.inventory.categories.dao;

import com.company.inventory.categories.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface ICategoryDao extends CrudRepository<Category, Long> {
}
