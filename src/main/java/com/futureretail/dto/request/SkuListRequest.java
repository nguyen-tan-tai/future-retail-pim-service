package com.futureretail.dto.request;

import java.util.List;

public class SkuListRequest {
    private List<String> skus;

    public List<String> getSkus() {
        return skus;
    }

    public void setSkus(List<String> skus) {
        this.skus = skus;
    }
}
