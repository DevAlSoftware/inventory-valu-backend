package com.company.inventory.productsSize.response;

import com.company.inventory.ResponseRest;
import com.company.inventory.products.response.ProductResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSizeResponseRest extends ResponseRest {

    private ProductSizeResponse productSizes = new ProductSizeResponse();

    public ProductSizeResponse getProductSizes() {
        return productSizes;
    }

    public void setProductSizes(ProductSizeResponse productSizes) {
        this.productSizes = productSizes;
    }
}
