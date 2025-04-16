package com.company.inventory.saleDetail.dao;

import com.company.inventory.saleDetail.model.SaleDetail;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ISaleDetailDao extends CrudRepository<SaleDetail, Long> {
    List<SaleDetail> findBySaleId(Long saleId);
}
