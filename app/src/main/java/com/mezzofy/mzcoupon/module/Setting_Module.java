package com.mezzofy.mzcoupon.module;

import android.content.Context;

import com.mezzofy.mzcoupon.Database.MojodomoDB;
import com.mezzofy.mzcoupon.Dao.Setting_Dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by aruna on 7/27/17.
 */

public class Setting_Module {
    private MojodomoDB dbhelper;

    public Setting_Module(Context pcontext) {
        dbhelper = new MojodomoDB(pcontext);
    }

    public String getSettings(String settingId) throws Exception {
        String res = null;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Setting_Dao daosetting = dbhelper.getsettingDao(con);
            res = daosetting.getSettings(settingId);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {

                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }


    public Boolean addSettings(String settingId, String value) throws Exception {
        Boolean res = null;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Setting_Dao daosetting = dbhelper.getsettingDao(con);
            res = daosetting.addSettings(settingId, value);

            if (res)
                con.commit();
            else
                con.rollback();

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.setAutoCommit(true);
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }

    public Boolean updateSetting(String settingId, String value) throws Exception {
        Boolean res = null;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Setting_Dao daosetting = dbhelper.getsettingDao(con);
            res = daosetting.updatesetting(settingId, value);

            if (res)
                con.commit();
            else
                con.rollback();

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.setAutoCommit(true);
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return res;
    }

    public Boolean CheckSetting(String userid, String pw, String Type) throws Exception {
        Boolean res = null;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Setting_Dao daosetting = dbhelper.getsettingDao(con);

            String value = daosetting.getSettings("User");
            if (value != null) {
                res = daosetting.updatesetting("User", userid);
                res = daosetting.updatesetting("Pw", pw);
                res = daosetting.updatesetting("Type", Type);
            } else {
                res = daosetting.addSettings("User", userid);
                res = daosetting.addSettings("Pw", pw);
                res = daosetting.addSettings("Type", Type);
            }


            if (res)
                con.commit();
            else
                con.rollback();

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    con.setAutoCommit(true);
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        return res;
    }


}
