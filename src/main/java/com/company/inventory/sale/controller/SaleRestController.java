package com.company.inventory.sale.controller;

import com.company.inventory.sale.model.Sale;
import com.company.inventory.sale.response.SaleResponseRest;
import com.company.inventory.sale.services.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
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
}
