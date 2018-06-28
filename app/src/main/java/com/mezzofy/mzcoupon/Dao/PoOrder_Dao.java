package com.mezzofy.mzcoupon.Dao;



import com.mezzofy.mzcoupon.Entity.PoEntity;
import com.mezzofy.mzcoupon.Entity.PoDetailEntity;
import com.mezzofy.mzcoupon.apputills.DatabaseUtils;
import com.mezzofy.mzcoupon.mapper.ResultSetMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by aruna on 7/3/17.
 */

public class PoOrder_Dao {
    private Connection connection;

    public PoOrder_Dao(Connection con) {
        connection = con;
    }

    public Boolean addOrder(PoEntity orderData) throws SQLException {
        Boolean res=false;

        ResultSetMapper<PoEntity> resultSetMapper=new ResultSetMapper<PoEntity>();
        String sql = "SELECT * FROM tbl_po_order  where poId=?";
        PoEntity poData= DatabaseUtils.queryForObj(connection,sql,resultSetMapper, PoEntity.class,new Object[]{orderData.getPoId()});

        if(poData!=null) {
            DeleteOrderdetail(orderData.getPoId());
        }
        sql = "insert into tbl_po_order (poId,merchantId,customerId,customerName,poDate,poNo,poStatus,payResponse,hashCode,updatedOn,couponType) values(?,?,?,?,?,?,?,?,?,?,?)";
        res = DatabaseUtils.update(connection, sql, new Object[]{orderData.getPoId(), orderData.getMerchantId(), orderData.getCustomerId(),
                orderData.getCustomerName(),orderData.getPoDate(), orderData.getPoNo(), orderData.getPoStatus(),
                orderData.getPayResponse(),orderData.hashCode(), orderData.getUpdatedOn(), orderData.getCouponType()});
        return res;
    }

    public Boolean addOrderdetail(PoDetailEntity Orderdetail) throws SQLException {
        Boolean res=false;

        String sql = "insert into tbl_po_order_item (podtlId,productId,campaignId,poId,campaignName,campaignPrice,campaignQty,campaignTotal,status,updatedOn) values(?,?,?,?,?,?,?,?,?,?)";

        res = DatabaseUtils.update(connection, sql, new Object[]{Orderdetail.getPodtlId(), Orderdetail.getProductId(), Orderdetail.getCampaignId(),
                Orderdetail.getPoId(),Orderdetail.getCampaignName(),Orderdetail.getCampaignPrice(),Orderdetail.getCampaignQty(),
                Orderdetail.getCampaignTotal(),Orderdetail.getStatus(),Orderdetail.getUpdatedOn()});
        return res;
    }

    public PoEntity getpodata(String PoId)throws SQLException{

        PoEntity poData = null;
        String sql = "select * from tbl_po_order where poId=?";
        ResultSetMapper<PoEntity>resultSetMapper=new ResultSetMapper<PoEntity>();
        poData=DatabaseUtils.queryForObj(connection,sql,resultSetMapper, PoEntity.class,new Object[]{PoId});

        return poData;

    }


    public List<PoEntity> getPolist()throws SQLException{

        List<PoEntity> sitelistdata = null;
        String sql = "select * from tbl_po_order";
        ResultSetMapper<PoEntity>resultSetMapper=new ResultSetMapper<PoEntity>();
        sitelistdata=DatabaseUtils.queryForList(connection,sql,resultSetMapper, PoEntity.class,null);

        return sitelistdata;

    }

    public List<PoDetailEntity> getpodetaildata(String poId)throws SQLException{

        List<PoDetailEntity> poDetailDatas = null;
        String sql = "select * from tbl_po_order_item where poId=?";
        ResultSetMapper<PoDetailEntity>resultSetMapper=new ResultSetMapper<PoDetailEntity>();
        poDetailDatas=DatabaseUtils.queryForList(connection,sql,resultSetMapper, PoDetailEntity.class,new Object[]{poId});

        return poDetailDatas;

    }

    public Boolean DeleteOrderdetail(String poId)throws SQLException {
        Boolean res=false;

        String sql = "delete from tbl_po_order where poId=?";
        res = DatabaseUtils.update(connection, sql, new Object[]{poId});

        if(res) {
            sql = "delete from tbl_po_order_item where poId=?";
            res = DatabaseUtils.update(connection, sql, new Object[]{poId});
        }
        return res;
    }




}
