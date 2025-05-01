package com.company.inventory.productsSize.dao;

import com.company.inventory.productsSize.model.ProductSize;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProductSizeDao extends CrudRepository<ProductSize, Long> {

    // Busca las tallas que tiene un producto específico
    List<ProductSize> findByProductId(Long productId);

    // Buscar tallas por nombre de producto
    List<ProductSize> findByProductNameContainingIgnoreCase(String productName);
}
