package com.company.inventory.products.response;

import com.company.inventory.ResponseRest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseRest extends ResponseRest {

    private ProductResponse product = new ProductResponse();

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }
}
