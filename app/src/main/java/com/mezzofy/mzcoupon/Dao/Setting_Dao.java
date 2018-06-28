package com.mezzofy.mzcoupon.Dao;

import com.mezzofy.mzcoupon.apputills.DatabaseUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by aruna on 7/27/17.
 */

public class Setting_Dao {

    private Connection connection;

    public Setting_Dao(Connection con) {
        connection = con;
    }


    public boolean addSettings(String settingId,String value) throws SQLException {
        Boolean res=false;
        String sql = "INSERT INTO tbl_setting(keyid, keyvalue ) values (?, ?)";
        res = DatabaseUtils.update(connection, sql, new Object[]{settingId,value});
        return res;
    }

    public String getSettings(String settingId) throws SQLException {

        String retvalue=null;
        String sql = "SELECT keyvalue FROM tbl_setting where keyid=?";
        retvalue = DatabaseUtils.queryForGeneric(connection, sql, String.class,new Object[]{settingId});
        return retvalue;
    }
    public boolean updatesetting(String settingId,String value) throws SQLException {
        Boolean res=false;
        String selectQuery = "update tbl_setting set keyvalue=? where keyid=?";
        res = DatabaseUtils.update(connection, selectQuery, new Object[]{value,settingId});
        return res;

    }

}
