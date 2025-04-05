package com.company.inventory.saleDetail.dao;

import com.company.inventory.saleDetail.model.SaleDetail;
import org.springframework.data.repository.CrudRepository;

public interface ISaleDetailDao extends CrudRepository<SaleDetail, Long> {
}
