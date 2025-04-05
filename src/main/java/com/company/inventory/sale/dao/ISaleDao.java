package com.company.inventory.sale.dao;

import com.company.inventory.sale.model.Sale;
import org.springframework.data.repository.CrudRepository;

public interface ISaleDao extends CrudRepository<Sale, Long> {
}
