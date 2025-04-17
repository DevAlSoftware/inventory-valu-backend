package com.company.inventory.sale.controller;

import com.company.inventory.categories.response.CategoryResponseRest;
import com.company.inventory.sale.model.Sale;
import com.company.inventory.sale.response.SaleResponseRest;
import com.company.inventory.sale.services.ISaleService;
import com.company.inventory.util.CategoryExcelExporter;
import com.company.inventory.util.SaleExcelExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:4200"})
public class SaleRestController {

    @Autowired
    private ISaleService saleService;

    @GetMapping("/sales")
    public ResponseEntity<SaleResponseRest> getSales() {
        return saleService.search();
    }

    @GetMapping("/sales/{id}")
    public ResponseEntity<SaleResponseRest> getSaleById(@PathVariable Long id) {
        return saleService.searchById(id);
    }

    @PostMapping("/sales")
    public ResponseEntity<SaleResponseRest> createSale(@RequestBody Sale sale) {
        return saleService.save(sale);
    }

    @PutMapping("/sales/{id}")
    public ResponseEntity<SaleResponseRest> updateSale(@RequestBody Sale sale, @PathVariable Long id) {
        return saleService.update(sale, id);
    }

    @DeleteMapping("/sales/{id}")
    public ResponseEntity<SaleResponseRest> deleteSale(@PathVariable Long id) {
        return saleService.deleteById(id);
    }

    @GetMapping("/sales/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=result_category.xlsx";
        response.setHeader(headerKey, headerValue);

        ResponseEntity<SaleResponseRest> saleResponse = saleService.search();

        SaleExcelExporter excelExporter = new SaleExcelExporter(
                saleResponse.getBody().getSaleResponse().getSale());

        excelExporter.export(response);


    }
}
