package com.company.inventory.saleDetail.services;

import com.company.inventory.saleDetail.model.SaleDetail;
import com.company.inventory.saleDetail.response.SaleDetailResponseRest;
import org.springframework.http.ResponseEntity;

public interface ISaleDetailService {

    ResponseEntity<SaleDetailResponseRest> search();
    ResponseEntity<SaleDetailResponseRest> searchById(Long id);
    ResponseEntity<SaleDetailResponseRest> save(SaleDetail saleDetail);
    ResponseEntity<SaleDetailResponseRest> update(SaleDetail saleDetail, Long id);
    ResponseEntity<SaleDetailResponseRest> deleteById(Long id);
    ResponseEntity<SaleDetailResponseRest> findBySaleId(Long saleId);
}
