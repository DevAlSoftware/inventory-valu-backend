package com.company.inventory.saleDetail.response;

import com.company.inventory.saleDetail.model.SaleDetail;
import lombok.Data;

import java.util.List;

@Data
public class SaleDetailResponse {

    private List<SaleDetail> saleDetail;

    public List<SaleDetail> getSaleDetail() {
        return saleDetail;
    }

    public void setSaleDetail(List<SaleDetail> saleDetail) {
        this.saleDetail = saleDetail;
    }
}
