package com.mezzofy.mzcoupon.Dao;

import android.util.Log;

import com.mezzofy.mzcoupon.Entity.OrderEntity;
import com.mezzofy.mzcoupon.Entity.OrderItemEntity;
import com.mezzofy.mzcoupon.Entity.OrderModifierEntity;
import com.mezzofy.mzcoupon.apputills.DatabaseUtils;
import com.mezzofy.mzcoupon.mapper.ResultSetMapper;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by aruna on 9/19/17.
 */

public class CouponOrder_Dao {
    private Connection connection;

    public CouponOrder_Dao(Connection con) {
        connection = con;
    }

    public Boolean addCouponOrder(OrderEntity orderData)throws SQLException {
        Boolean res=false;
        ResultSetMapper<OrderEntity> resultSetMapper=new ResultSetMapper<OrderEntity>();

        Log.d("orderData",orderData.toString());
        String sql = "SELECT * FROM tbl_coupon_order  where couponId=?";
        OrderEntity res1 = DatabaseUtils.queryForObj(connection, sql,resultSetMapper,OrderEntity.class, new Object[]{orderData.getCouponId()});

        if(res1!=null)
        {
            if(orderData.getHashCode()==res1.getHashCode()) {
                sql = "update tbl_coupon_order SET deleteflag=? where couponId=?";
                res = DatabaseUtils.update(connection, sql, new Object[]{"N",orderData.getCouponId()});
                return true;
            }
            else
            {
                sql = "update tbl_coupon_order SET merchantId=?,siteId=?,productId=?,customerId=?,customerName=?,trackId=?,orderDate=?,startendTime=?," +
                        "orderType=?,orderRemark=?,contactNo=?,orderNo=?,hashCode=?,orderStatus=?," +
                        "pushRead=?,customerAddress=?,updatedOn=?,pickupDeliveryType=?,deleteflag=? where couponId=?";
                res = DatabaseUtils.update(connection, sql, new Object[]{orderData.getMerchantId(), orderData.getSiteId(), orderData.getProductId(), orderData.getCustomerId(),
                        orderData.getCustomerName(),orderData.getTrackId(), orderData.getOrderDate(),orderData.getStartendTime(),orderData.getOrderType(),orderData.getOrderRemark(),
                        orderData.getContactNo(),orderData.getOrderNo(),orderData.hashCode(),orderData.getOrderStatus(),orderData.getPushRead(),orderData.getCustomerAddress(),
                        orderData.getUpdatedOn(), orderData.getPickupDeliveryType(), "N",orderData.getCouponId()});
            }
        }
        else {

            sql = "insert into tbl_coupon_order (couponId,merchantId,siteId,productId,customerId,customerName,trackId,orderDate,startendTime,orderType,orderRemark," +
                    "contactNo,orderNo,hashCode,orderStatus,pushRead,customerAddress,updatedOn,pickupDeliveryType,deleteflag) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'N')";
            res = DatabaseUtils.update(connection, sql, new Object[]{orderData.getCouponId(), orderData.getMerchantId(), orderData.getSiteId(), orderData.getProductId(),
                    orderData.getCustomerId(),orderData.getCustomerName(), orderData.getTrackId(),orderData.getOrderDate(),orderData.getStartendTime(),orderData.getOrderType(),
                    orderData.getOrderRemark(),orderData.getContactNo(),orderData.getOrderNo(),orderData.getHashCode(),orderData.getOrderStatus(),orderData.getPushRead(),
                    orderData.getCustomerAddress(), orderData.getUpdatedOn(), orderData.getPickupDeliveryType()});
        }

        return res;
    }

    public Boolean DeleteInActiveRecord() throws SQLException{
        Boolean res=false;
        String sql = "delete from tbl_coupon_order where deleteflag=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{"Y"});
        return res;

    }


    public Boolean addCouponItem(OrderItemEntity orderItemData)throws SQLException {
        Boolean res=false;
        ResultSetMapper<OrderItemEntity> resultSetMapper=new ResultSetMapper<OrderItemEntity>();

        Log.d("OrderItemEntity",orderItemData.toString());
        String sql = "SELECT * FROM tbl_coupon_order_item  where itemId=?";
        OrderItemEntity res1 = DatabaseUtils.queryForObj(connection, sql,resultSetMapper,OrderItemEntity.class, new Object[]{orderItemData.getItemId()});
        if(res1!=null)
        {
            sql = "update tbl_coupon_order_item SET orderItemId=?,couponId=?,itemName=?,groupName=?,deleteflag=? where itemId=?";
                res = DatabaseUtils.update(connection, sql, new Object[]{orderItemData.getOrderItemId(), orderItemData.getCouponId(), orderItemData.getItemName(), orderItemData.getGroupName(),"N",orderItemData.getItemId()});
        }
        else {

            sql = "insert into tbl_coupon (itemId,orderItemId,couponId,itemName,groupName,deleteflag) " +
                    "values(?,?,?,?,?,'N')";
            res = DatabaseUtils.update(connection, sql, new Object[]{orderItemData.getItemId(), orderItemData.getOrderItemId(), orderItemData.getCouponId(), orderItemData.getItemName(),
                    orderItemData.getGroupName()});
        }

        return res;
    }



    public Boolean addCouponModifier(OrderModifierEntity orderModifierData)throws SQLException {
        Boolean res=false;
        ResultSetMapper<OrderModifierEntity> resultSetMapper=new ResultSetMapper<OrderModifierEntity>();

        Log.d("OrderModifierEntity",orderModifierData.toString());
        String sql = "SELECT * FROM tbl_coupon_order_modifier  where modifierId=?";
        OrderModifierEntity res1 = DatabaseUtils.queryForObj(connection, sql,resultSetMapper,OrderModifierEntity.class, new Object[]{orderModifierData.getModifierId()});
        if(res1!=null)
        {
            sql = "update tbl_coupon_order_modifier SET orderModifierId=?,orderItemId=?,modifierName=?,deleteflag=? where modifierId=?";
            res = DatabaseUtils.update(connection, sql, new Object[]{orderModifierData.getOrderModifierId(), orderModifierData.getOrderItemId(), orderModifierData.getModifierName(), "N",orderModifierData.getModifierId()});
        }
        else {

            sql = "insert into tbl_coupon_order_modifier (orderModifierId,orderItemId,modifierId,modifierName,deleteflag) " +
                    "values(?,?,?,?,'N')";
            res = DatabaseUtils.update(connection, sql, new Object[]{orderModifierData.getOrderModifierId(), orderModifierData.getOrderItemId(), orderModifierData.getModifierId(), orderModifierData.getModifierName()});
        }

        return res;
    }


    public OrderEntity getCouponOrder(String CouponId)throws SQLException{
        OrderEntity orderData=new OrderEntity();
        String Sql="SELECT * FROM tbl_coupon_order WHERE couponId=?";
        ResultSetMapper<OrderEntity>resultSetMapper=new ResultSetMapper<OrderEntity>();
        orderData=DatabaseUtils.queryForObj(connection,Sql,resultSetMapper, OrderEntity.class,new Object[]{CouponId});
        return orderData;
    }


}
