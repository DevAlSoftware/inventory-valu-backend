package com.company.inventory.sale.controller;

import com.company.inventory.sale.model.Sale;
import com.company.inventory.sale.response.SaleResponseRest;
import com.company.inventory.sale.services.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SaleRestController {

    @Autowired
    private ISaleService saleService;

    @GetMapping
    public ResponseEntity<SaleResponseRest> getSales() {
        return saleService.search();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseRest> getSaleById(@PathVariable Long id) {
        return saleService.searchById(id);
    }

    @PostMapping
    public ResponseEntity<SaleResponseRest> createSale(@RequestBody Sale sale) {
        return saleService.save(sale);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleResponseRest> updateSale(@RequestBody Sale sale, @PathVariable Long id) {
        return saleService.update(sale, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SaleResponseRest> deleteSale(@PathVariable Long id) {
        return saleService.deleteById(id);
    }
}
