package com.company.inventory.sale.services;

import com.company.inventory.sale.model.Sale;
import com.company.inventory.sale.response.SaleResponseRest;
import org.springframework.http.ResponseEntity;

public interface ISaleService {

    ResponseEntity<SaleResponseRest> search();
    ResponseEntity<SaleResponseRest> searchById(Long id);
    ResponseEntity<SaleResponseRest> save(Sale sale);
    ResponseEntity<SaleResponseRest> update(Sale sale, Long id);
    ResponseEntity<SaleResponseRest> deleteById(Long id);
}
