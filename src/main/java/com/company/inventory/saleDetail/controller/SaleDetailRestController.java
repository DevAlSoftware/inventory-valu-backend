package com.company.inventory.saleDetail.controller;

import com.company.inventory.saleDetail.model.SaleDetail;
import com.company.inventory.saleDetail.response.SaleDetailResponseRest;
import com.company.inventory.saleDetail.services.ISaleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:4200"})
public class SaleDetailRestController {

    @Autowired
    private ISaleDetailService saleDetailService;

    @GetMapping("/sales-detail")
    public ResponseEntity<SaleDetailResponseRest> getAllSaleDetails() {
        return saleDetailService.search();
    }

    @GetMapping("/sales-detail/{id}")
    public ResponseEntity<SaleDetailResponseRest> getSaleDetailById(@PathVariable Long id) {
        return saleDetailService.searchById(id);
    }

    @GetMapping("/sales-detail/sale/{saleId}")
    public ResponseEntity<SaleDetailResponseRest> getSaleDetailsBySaleId(@PathVariable Long saleId) {
        return saleDetailService.findBySaleId(saleId);
    }

    @PostMapping("/sales-detail")
    public ResponseEntity<SaleDetailResponseRest> createSaleDetail(@RequestBody SaleDetail saleDetail) {
        return saleDetailService.save(saleDetail);
    }

    @PutMapping("/sales-detail/{id}")
    public ResponseEntity<SaleDetailResponseRest> updateSaleDetail(@RequestBody SaleDetail saleDetail, @PathVariable Long id) {
        return saleDetailService.update(saleDetail, id);
    }

    @DeleteMapping("/sales-detail/{id}")
    public ResponseEntity<SaleDetailResponseRest> deleteSaleDetail(@PathVariable Long id) {
        return saleDetailService.deleteById(id);
    }
}