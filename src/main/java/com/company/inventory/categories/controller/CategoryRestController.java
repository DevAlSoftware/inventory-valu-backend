package com.company.inventory.categories.controller;

import com.company.inventory.categories.model.Category;
import com.company.inventory.categories.response.CategoryResponseRest;
import com.company.inventory.categories.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class CategoryRestController {

    @Autowired
    private ICategoryService categoryService;

    /**
     *  get all the categories
     * @return
     */
    @GetMapping("/categories")
    public ResponseEntity<CategoryResponseRest> searchCategories () {
        return categoryService.search();
    }

    /**
     * get categories by id
     * @param id
     * @return
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryResponseRest> searchCategoriesById (@PathVariable Long id) {

        return categoryService.searchById(id);
    }

    /**
     * SAVE categories
     * @param category
     * @return
     */
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponseRest> save (@RequestBody Category category) {

        return categoryService.save(category);
    }

    /**
     * update categories
     * @param category
     * @param id
     * @return
     */
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryResponseRest> update(@RequestBody Category category, @PathVariable Long id) {

        return categoryService.update(category, id);
    }

    /**
     * delete categories
     * @param id
     * @return
     */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<CategoryResponseRest> delete(@PathVariable Long id) {

        return categoryService.deleteById(id);
    }



}
