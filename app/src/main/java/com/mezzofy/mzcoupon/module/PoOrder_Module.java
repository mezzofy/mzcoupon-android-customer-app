package com.mezzofy.mzcoupon.module;

import android.content.Context;


import com.mezzofy.MzCouponAPI.data.PoListDataModel;
import com.mezzofy.MzCouponAPI.mapper.JsonMapper;
import com.mezzofy.MzCouponAPI.module.MZPoOrder;
import com.mezzofy.mzcoupon.Entity.PoEntity;
import com.mezzofy.mzcoupon.Entity.PoDetailEntity;
import com.mezzofy.mzcoupon.Database.MojodomoDB;
import com.mezzofy.mzcoupon.Dao.PoOrder_Dao;
import com.mezzofy.mzcoupon.Entity.PoDetailmEntity;
import com.mezzofy.mzcoupon.Entity.PoListmEntity;
import com.mezzofy.mzcoupon.Entity.PomEntity;

import org.json.JSONException;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by aruna on 7/3/17.
 */

public class PoOrder_Module {
    private MojodomoDB dbhelper;
    private MZPoOrder poOrderModule;

    public PoOrder_Module(Context context) {
        dbhelper = new MojodomoDB(context);
        poOrderModule=new MZPoOrder(context);
    }

    public PoListmEntity getOrderAPI(String CustomerId, int offset) {
        PoListmEntity poListmEntity = null;
        PoListDataModel poListmData=poOrderModule.GetOrders(CustomerId,offset);
        if(poListmData!=null){
            try {
                poListmEntity=(PoListmEntity) JsonMapper.mapJsonToObj(poListmData, PoListmEntity.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return poListmEntity;
    }
    public List<PoEntity> getPolist()throws  Exception{
        List<PoEntity> polist=null;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            PoOrder_Dao daoorder = dbhelper.getOrderDao(con);
            polist = daoorder.getPolist();

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return polist;
    }

    public PoEntity getpodata(String PoId)throws Exception{
        PoEntity poData=null;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            PoOrder_Dao daoorder = dbhelper.getOrderDao(con);
            poData = daoorder.getpodata(PoId);

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return poData;
    }
    public List<PoDetailEntity> getpodetaildata(String poId)throws Exception{
        List<PoDetailEntity> poDetailDatas=null;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            PoOrder_Dao daoorder = dbhelper.getOrderDao(con);
            poDetailDatas = daoorder.getpodetaildata(poId);

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                try {
                    dbhelper.releaseConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return poDetailDatas;
    }

    public Boolean addOrderdetail(PoListmEntity poListModel) throws Exception{

        boolean res=false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            PoOrder_Dao daoorder = dbhelper.getOrderDao(con);

            List<PomEntity> poModels=poListModel.getPos();

            for(PomEntity poModel:poModels)
            {
                PoEntity poData=poModel.getPo();
                List<PoDetailmEntity> poDetailDatas=poModel.getPodetails();

                res = daoorder.addOrder(poData);
                for(PoDetailmEntity poDetailModel:poDetailDatas)
                {
                    res = daoorder.addOrderdetail(poDetailModel.getPodetail());
                }
                if(res)
                    con.commit();
                else
                    con.rollback();
            }

        }catch (SQLException e) {
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
