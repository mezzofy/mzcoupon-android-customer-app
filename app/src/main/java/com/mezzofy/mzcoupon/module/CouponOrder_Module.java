package com.mezzofy.mzcoupon.module;

import android.content.Context;



import com.mezzofy.mzcoupon.Entity.OrderEntity;
import com.mezzofy.mzcoupon.Database.CouponDB;
import com.mezzofy.mzcoupon.Dao.CouponOrder_Dao;

import com.mezzofy.mzcoupon.Entity.OrderItemmEntity;
import com.mezzofy.mzcoupon.Entity.OrderListmEntity;
import com.mezzofy.mzcoupon.Entity.OrdermEntity;
import com.mezzofy.mzcoupon.Entity.OrderModifiermEntity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import mezzofy.com.libmzcoupon.data.OrderListDataModel;
import mezzofy.com.libmzcoupon.data.OrdermData;
import mezzofy.com.libmzcoupon.mapper.JsonMapper;
import mezzofy.com.libmzcoupon.module.MZCouponOrder;
import mezzofy.com.libmzcoupon.utills.APIServerException;


/**
 * Created by aruna on 9/19/17.
 */

public class CouponOrder_Module {

    private JSONObject jsonobj =null;
    private CouponDB dbhelper;
    private MZCouponOrder couponOrderModule;

    public CouponOrder_Module(Context context) {
        dbhelper = new CouponDB(context);
        couponOrderModule=new MZCouponOrder(context);
    }

    public OrdermEntity PostOrderToServer(OrdermEntity orderModel) throws APIServerException {

        OrdermEntity resp = null;
        try {

            OrdermData ordermData = (OrdermData) JsonMapper.mapJsonToObj(orderModel, OrdermData.class);
            OrdermData ordermData1 = couponOrderModule.createCouponOrder(ordermData);

            if (ordermData1 != null) {
                resp = (OrdermEntity) JsonMapper.mapJsonToObj(ordermData1, OrdermEntity.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resp;
    }



    public boolean getCouponOrderAPI(String CustomerId,int page) {
        boolean res=false;
        try {

            OrderListDataModel orderListmData=couponOrderModule.getCouponOrder(CustomerId,page);
            if(orderListmData!=null) {
                OrderListmEntity orderListmEntity = (OrderListmEntity) JsonMapper.mapJsonToObj(orderListmData, OrderListmEntity.class);
                for (OrdermEntity orderModel : orderListmEntity.getOrders()) {
                    if (orderModel != null) {
                        try {
                            res = addOrderlist(orderModel);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    public OrderListmEntity getCouponOrdertrackAPI(String CouponId) {
        OrderListmEntity orderListmEntity = null;

        try {

            OrderListDataModel orderListmData = couponOrderModule.trackCouponOrder(CouponId);
            if (orderListmData != null) {
                orderListmEntity = (OrderListmEntity) JsonMapper.mapJsonToObj(orderListmData, OrderListmEntity.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderListmEntity;
    }

    public boolean getCouponOrderDetailAPI(String CouponId) {

        boolean res=false;
        OrdermEntity ordermEntity = null;
        try {

            OrdermData ordermData = couponOrderModule.getCouponOrderById(CouponId);
            if (ordermData != null) {
                ordermEntity = (OrdermEntity) JsonMapper.mapJsonToObj(ordermData, OrdermEntity.class);
                if(ordermEntity!=null) {
                    try {
                        res = addOrderlist(ordermEntity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    public OrderEntity getCouponOrder(String CouponId)throws Exception{
        OrderEntity res=new OrderEntity();
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            CouponOrder_Dao daoorder = dbhelper.getCouponOrderDao(con);
            res = daoorder.getCouponOrder(CouponId);

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
        return res;
    }

    public Boolean addOrderlist(OrdermEntity orderModel) throws Exception{

        boolean res=false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            CouponOrder_Dao daoorder = dbhelper.getCouponOrderDao(con);
                List<OrderItemmEntity> OrderItem=orderModel.getOrderitems();
            res = daoorder.addCouponOrder(orderModel.getOrder());
            if(OrderItem!=null && OrderItem.size()!=0) {
                for (OrderItemmEntity orderItemModel : OrderItem) {
                    res = daoorder.addCouponItem(orderItemModel.getOrderitem());
                    for (OrderModifiermEntity orderModifierModel : orderItemModel.getOrdermodifiers()) {
                        res = daoorder.addCouponModifier(orderModifierModel.getOrdermodifier());
                    }

                }
                if (res)
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

    public Boolean DeleteInActiveRecord() throws Exception{

        boolean res=false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            CouponOrder_Dao daoorder = dbhelper.getCouponOrderDao(con);
            res = daoorder.DeleteInActiveRecord();

                if (res)
                    con.commit();
                else
                    con.rollback();
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
