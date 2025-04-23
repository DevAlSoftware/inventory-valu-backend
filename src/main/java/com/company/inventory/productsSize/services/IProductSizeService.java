package com.company.inventory.productsSize.services;

import com.company.inventory.products.response.ProductResponseRest;
import com.company.inventory.productsSize.model.ProductSize;
import com.company.inventory.productsSize.response.ProductSizeResponseRest;
import org.springframework.http.ResponseEntity;

public interface IProductSizeService {

    public ResponseEntity<ProductSizeResponseRest> save(ProductSize productSize, Long productId);
    public ResponseEntity<ProductSizeResponseRest> searchById(Long id);
    public ResponseEntity<ProductSizeResponseRest> deleteById(Long id);
    public ResponseEntity<ProductSizeResponseRest> search();
    public ResponseEntity<ProductSizeResponseRest> update(ProductSize productSize, Long productId, Long id);
    public ResponseEntity<ProductSizeResponseRest> searchByProductId(Long productId);

}
