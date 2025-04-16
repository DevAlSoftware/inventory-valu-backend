package com.company.inventory.categories.controller;

import com.company.inventory.categories.model.Category;
import com.company.inventory.categories.response.CategoryResponseRest;
import com.company.inventory.categories.services.ICategoryService;
import com.company.inventory.products.response.ProductResponseRest;
import com.company.inventory.util.CategoryExcelExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
     * search by name
     * @param name
     * @return
     */
    @GetMapping("/categories/filter/{name}")
    public ResponseEntity<CategoryResponseRest> searchByName(@PathVariable String name){
        ResponseEntity<CategoryResponseRest> response = categoryService.searchByName(name);
        return response;
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
    /**
     * export to excel file
     * @param response
     * @throws IOException
     */
    @GetMapping("/categories/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=result_category.xlsx";
        response.setHeader(headerKey, headerValue);

        ResponseEntity<CategoryResponseRest> categoryResponse = categoryService.search();

        CategoryExcelExporter excelExporter = new CategoryExcelExporter(
                categoryResponse.getBody().getCategoryResponse().getCategory());

        excelExporter.export(response);


    }


}
