package com.mezzofy.mzcoupon.Entity;

import java.util.List;

/**
 * Created by Aruna on 28/03/17.
 */

public class PoListmEntity {
    private List<PomEntity> pos;
    private SitemEnity size;

    public List<PomEntity> getPos() {
        return pos;
    }

    public void setPos(List<PomEntity> pos) {
        this.pos = pos;
    }

    public SitemEnity getSize() {
        return size;
    }

    public void setSize(SitemEnity size) {
        this.size = size;
    }
}
