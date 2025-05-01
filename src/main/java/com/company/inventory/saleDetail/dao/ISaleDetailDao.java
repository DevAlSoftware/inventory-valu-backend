package com.company.inventory.saleDetail.dao;

import com.company.inventory.saleDetail.model.SaleDetail;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ISaleDetailDao extends CrudRepository<SaleDetail, Long> {
    List<SaleDetail> findBySaleId(Long saleId);

    // Buscar una talla específica de un producto
    Optional<SaleDetail> findByProductSize_Product_IdAndProductSize_Size(Long productId, String size);
}
