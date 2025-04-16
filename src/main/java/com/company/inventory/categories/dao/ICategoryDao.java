package com.company.inventory.categories.dao;

import com.company.inventory.categories.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ICategoryDao extends CrudRepository<Category, Long> {

    @Query("select c from Category c where c.name like %?1%")
    List<Category> findByNameLike(String name);

    List<Category> findByNameContainingIgnoreCase(String name);
}
