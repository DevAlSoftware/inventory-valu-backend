package com.company.inventory.sale.response;

import com.company.inventory.sale.model.Sale;
import lombok.Data;

import java.util.List;

@Data
public class SaleResponse {

    private List<Sale> sale;
}
