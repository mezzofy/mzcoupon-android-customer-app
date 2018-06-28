package com.mezzofy.mzcoupon.Entity;

import java.util.List;

/**
 * Created by aruna on 11/14/17.
 */

public class NotificationListmEntity {

    private List<NotificationmEntity> notifications;

    private SizemEnity size;

    public List<NotificationmEntity> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationmEntity> notifications) {
        this.notifications = notifications;
    }

    public SizemEnity getSize() {
        return size;
    }

    public void setSize(SizemEnity size) {
        this.size = size;
    }
}
