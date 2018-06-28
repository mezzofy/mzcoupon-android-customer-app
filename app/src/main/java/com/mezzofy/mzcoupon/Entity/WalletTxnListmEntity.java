package com.mezzofy.mzcoupon.Entity;

import java.util.List;

/**
 * Created by aruna on 9/20/17.
 */

public class WalletTxnListmEntity {
    private List<WalletTxnmEntity> wallettxns;
    private SizemEnity size;

    public List<WalletTxnmEntity> getWallettxns() {
        return wallettxns;
    }

    public void setWallettxns(List<WalletTxnmEntity> wallettxns) {
        this.wallettxns = wallettxns;
    }

    public SizemEnity getSize() {
        return size;
    }

    public void setSize(SizemEnity size) {
        this.size = size;
    }
}
