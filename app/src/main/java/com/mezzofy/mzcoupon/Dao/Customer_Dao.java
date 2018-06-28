package com.mezzofy.mzcoupon.Dao;

import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.CustomerGroupEntity;
import com.mezzofy.mzcoupon.apputills.DatabaseUtils;
import com.mezzofy.mzcoupon.mapper.ResultSetMapper;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by aruna on 7/26/17.
 */

public class Customer_Dao {
    private Connection connection;

    public Customer_Dao(Connection con) {
        connection = con;
    }

    public Boolean addUser(CustomerEntity customerData)throws SQLException {
        Boolean res=false;
        String sql = "insert into tbl_customer (customerId,merchantId,customerGroupId,customerFirstName,customerLastName,customerEmail,customerPassword,countryCode,customerDob," +
                "customerMobile,customerGender,customerAddress,userType,vipNo,dgvipNo,fbToken,customerStatus,hashCode,createdOn,updatedOn,updatedBy," +
                "customerUsername,customerImageUrl,logout_flag) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        res = DatabaseUtils.update(connection, sql, new Object[]{customerData.getCustomerId(), customerData.getMerchantId(), customerData.getCustomerGroupId(),customerData.getCustomerFirstName(),
                customerData.getCustomerLastName(),customerData.getCustomerEmail(),customerData.getCustomerPassword(),customerData.getCountryCode(),customerData.getCustomerDob(),
                customerData.getCustomerMobile(),customerData.getCustomerGender(),customerData.getCustomerAddress(),customerData.getUserType(),customerData.getVipNo(),customerData.getDgvipNo(),
                customerData.getFbToken(),customerData.getCustomerStatus(),customerData.getHashCode(),customerData.getCreatedOn(),customerData.getUpdatedOn(),customerData.getUpdatedBy(),
                customerData.getCustomerUsername(),customerData.getCustomerImageUrl(),"Y"});
        return res;
    }

    public Boolean addCustomerGrp(CustomerGroupEntity customerGroupData)throws SQLException {
        Boolean res=false;
        String sql = "insert into tbl_customer_group (customerGroupId,merchantId,groupName,groupStatus,dollarSpending,rewardPoint,updatedOn,hashCode,deleteflag)" +
                " values(?,?,?,?,?,?,?,?,?)";

        res = DatabaseUtils.update(connection, sql, new Object[]{customerGroupData.getCustomerGroupId(), customerGroupData.getMerchantId(), customerGroupData.getGroupName(),customerGroupData.getGroupStatus(),
                customerGroupData.getDollarSpending(),customerGroupData.getRewardPoint(),customerGroupData.getUpdatedOn(),customerGroupData.hashCode(),"N"});
        return res;
    }

    public Boolean UpdateCustomer(CustomerEntity customerData)throws SQLException {
        Boolean res=false;
        String sql = "update tbl_customer set customerId=?,merchantId=?,customerGroupId=?,customerFirstName=?,customerLastName=?,customerEmail=?,customerPassword=?" +
                ",countryCode=?,customerDob=?,customerMobile=?,customerGender=?,customerAddress=?,userType=?,vipNo=?,dgvipNo=?,fbToken=?,customerStatus=?,hashCode=?," +
                "createdOn=?,updatedOn=?,updatedBy=?,customerUsername=?,customerImageUrl=?,logout_flag=?";

        res = DatabaseUtils.update(connection, sql, new Object[]{customerData.getCustomerId(), customerData.getMerchantId(), customerData.getCustomerGroupId(),customerData.getCustomerFirstName(),
                customerData.getCustomerLastName(),customerData.getCustomerEmail(),customerData.getCustomerPassword(),customerData.getCountryCode(),customerData.getCustomerDob(),
                customerData.getCustomerMobile(),customerData.getCustomerGender(),customerData.getCustomerAddress(),customerData.getUserType(),customerData.getVipNo(),customerData.getDgvipNo(),
                customerData.getFbToken(),customerData.getCustomerStatus(),customerData.getHashCode(),customerData.getCreatedOn(),customerData.getUpdatedOn(),customerData.getUpdatedBy(),
                customerData.getCustomerUsername(),customerData.getCustomerImageUrl(),"Y"});
        return res;
    }

    public Boolean DeleteCustomerGrpDetail()throws SQLException{
        Boolean res=false;
        String  sql = "delete from tbl_customer_group";
        DatabaseUtils.update(connection, sql,null);
        return res;
    }

    public CustomerEntity getUser()throws SQLException {
        String selectQuery="SELECT * FROM tbl_customer where logout_flag='Y'";
        ResultSetMapper<CustomerEntity> resultSetMapper=new ResultSetMapper<CustomerEntity>();
        CustomerEntity customerData=DatabaseUtils.queryForObj(connection,selectQuery,resultSetMapper,CustomerEntity.class,null);
        return customerData;
    }

    public CustomerEntity getUser(String username, String pass)throws SQLException{
        String selectQuery="SELECT * FROM tbl_customer customerUsername=? and customerPassword=?";
        ResultSetMapper<CustomerEntity> resultSetMapper=new ResultSetMapper<CustomerEntity>();
        CustomerEntity customerData=DatabaseUtils.queryForObj(connection,selectQuery,resultSetMapper,CustomerEntity.class,new Object[]{username,pass});
        return customerData;
    }
    public Boolean updateUser(String customerId, String loginName, String mobile)throws SQLException{
        Boolean res=false;
        String selectQuery = "update tbl_customer set customerUsername=?,customerMobile=? where customerId=?";
        res = DatabaseUtils.update(connection, selectQuery, new Object[]{loginName, mobile,customerId});
        return res;
    }

    public Boolean ChangePassword(CustomerEntity customerData)throws SQLException{
        Boolean res=false;
        String selectQuery = "update tbl_customer set customerPassword=? where customerId=?";
        res = DatabaseUtils.update(connection, selectQuery, new Object[]{customerData.getCustomerPassword(), customerData.getCustomerId()});
        return res;
    }

    public Boolean updatePwd(String customerId, String pwd)throws SQLException{
        Boolean res=false;
        String selectQuery = "update tbl_customer set customerPassword=? where customerId=?";
        res = DatabaseUtils.update(connection, selectQuery, new Object[]{pwd,customerId});
        return res;
    }

    public Boolean logoutUser(String customerId)throws SQLException{
        Boolean res=false;
        String selectQuery = "update tbl_customer set logout_flag=? where customerId=?";
        res = DatabaseUtils.update(connection, selectQuery, new Object[]{"N",customerId});
        return res;
    }
}
