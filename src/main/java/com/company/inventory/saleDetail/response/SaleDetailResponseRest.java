package com.company.inventory.saleDetail.response;

import com.company.inventory.categories.response.ResponseRest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleDetailResponseRest extends ResponseRest {

    private SaleDetailResponse saleDetailResponse = new SaleDetailResponse();
}
