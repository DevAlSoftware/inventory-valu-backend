package com.company.inventory.saleDetail.controller;

import com.company.inventory.saleDetail.model.SaleDetail;
import com.company.inventory.saleDetail.response.SaleDetailResponseRest;
import com.company.inventory.saleDetail.services.ISaleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sale-details")
@CrossOrigin(origins = "*")
public class SaleDetailRestController {

    @Autowired
    private ISaleDetailService saleDetailService;

    @GetMapping
    public ResponseEntity<SaleDetailResponseRest> getAllSaleDetails() {
        return saleDetailService.search();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDetailResponseRest> getSaleDetailById(@PathVariable Long id) {
        return saleDetailService.searchById(id);
    }

    @PostMapping
    public ResponseEntity<SaleDetailResponseRest> createSaleDetail(@RequestBody SaleDetail saleDetail) {
        return saleDetailService.save(saleDetail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDetailResponseRest> updateSaleDetail(@RequestBody SaleDetail saleDetail, @PathVariable Long id) {
        return saleDetailService.update(saleDetail, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SaleDetailResponseRest> deleteSaleDetail(@PathVariable Long id) {
        return saleDetailService.deleteById(id);
    }
}