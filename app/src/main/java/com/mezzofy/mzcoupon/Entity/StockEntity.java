package com.mezzofy.mzcoupon.Entity;

/**
 * Created by aruna on 9/20/17.
 */

public class StockEntity {
    private String campaignId;
    private String stockId;
    private int stockqty;

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public int getStockqty() {
        return stockqty;
    }

    public void setStockqty(int stockqty) {
        this.stockqty = stockqty;
    }
}
