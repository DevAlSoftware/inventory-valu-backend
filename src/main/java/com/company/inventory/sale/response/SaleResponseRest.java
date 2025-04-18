package com.company.inventory.sale.response;

import com.company.inventory.ResponseRest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleResponseRest extends ResponseRest {

    private SaleResponse saleResponse = new SaleResponse();

    public SaleResponse getSaleResponse() {
        return saleResponse;
    }

    public void setSaleResponse(SaleResponse saleResponse) {
        this.saleResponse = saleResponse;
    }
}
