package com.company.inventory.products.response;

import java.util.List;


import com.company.inventory.products.model.Product;
import lombok.Data;

@Data
public class ProductResponse {

    List<Product> products;

}
