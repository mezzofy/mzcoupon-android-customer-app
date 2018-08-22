package com.mezzofy.mzcoupon.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.mezzofy.mzcoupon.apputills.ObjectSerializer;
import com.mezzofy.mzcoupon.Entity.NotificationListmEntity;
import com.mezzofy.mzcoupon.Entity.NotificationmEntity;
import com.mezzofy.mzcoupon.Entity.SizemEnity;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import mezzofy.com.libmzcoupon.data.NotificationListDataModel;
import mezzofy.com.libmzcoupon.mapper.JsonMapper;
import mezzofy.com.libmzcoupon.module.MZNotification;

/**
 * Created by aruna on 11/14/17.
 */

public class Notification_Module {

    SharedPreferences settings;
    private MZNotification notificationModule;


    public Notification_Module(Context context) {

        settings = PreferenceManager.getDefaultSharedPreferences(context);
        notificationModule=new MZNotification(context);
    }

    public List<NotificationmEntity> getNotificationListfromserver(String CustomerId, int Page) {
        List<NotificationmEntity> notificationList = null;
        SizemEnity size=null;

        NotificationListDataModel notificationListmData=notificationModule.GetNotifications(CustomerId,Page);
        if(notificationListmData!=null){
            try {
                NotificationListmEntity notificationListmEntity=(NotificationListmEntity) JsonMapper.mapJsonToObj(notificationListmData, NotificationListmEntity.class);
                if (notificationListmEntity != null) {
                    notificationList = notificationListmEntity.getNotifications();

                    size=notificationListmEntity.getSize();
                    SharedPreferences.Editor editor = settings.edit();
                    try {
                        editor.putString("size", ObjectSerializer.serialize(size));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editor.commit();

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return notificationList;

    }
}
