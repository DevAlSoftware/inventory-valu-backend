package com.company.inventory.productsSize.response;

import com.company.inventory.productsSize.model.ProductSize;
import lombok.Data;

import java.util.List;

@Data
public class ProductSizeResponse {

    List<ProductSize> productSizes;

    public List<ProductSize> getProductSizes() {
        return productSizes;
    }

    public void setProductSizes(List<ProductSize> productSizes) {
        this.productSizes = productSizes;
    }
}
