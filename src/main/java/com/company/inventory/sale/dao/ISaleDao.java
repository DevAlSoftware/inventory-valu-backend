package com.company.inventory.sale.dao;

import com.company.inventory.sale.model.Sale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISaleDao extends CrudRepository<Sale, Long> {

    @Query("SELECT s FROM Sale s JOIN s.saleDetails sd WHERE sd.product.id = :productId")
    List<Sale> findSalesByProductId(@Param("productId") Long productId);

}
