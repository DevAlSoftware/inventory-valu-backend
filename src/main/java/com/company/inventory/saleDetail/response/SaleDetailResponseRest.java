package com.company.inventory.saleDetail.response;

import com.company.inventory.ResponseRest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleDetailResponseRest extends ResponseRest {

    private SaleDetailResponse saleDetailResponse = new SaleDetailResponse();

    public SaleDetailResponse getSaleDetailResponse() {
        return saleDetailResponse;
    }

    public void setSaleDetailResponse(SaleDetailResponse saleDetailResponse) {
        this.saleDetailResponse = saleDetailResponse;
    }
}
