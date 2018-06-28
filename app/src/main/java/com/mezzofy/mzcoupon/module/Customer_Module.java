package com.mezzofy.mzcoupon.module;

import android.content.Context;

import com.mezzofy.MzCouponAPI.data.CountryListDataModel;
import com.mezzofy.MzCouponAPI.data.CustomerDeviceDataModel;
import com.mezzofy.MzCouponAPI.data.CustomerGroupDataModel;
import com.mezzofy.MzCouponAPI.data.CustomerDataModel;
import com.mezzofy.MzCouponAPI.mapper.JsonMapper;
import com.mezzofy.MzCouponAPI.module.MZCustomer;
import com.mezzofy.MzCouponAPI.utills.APIServerException;

import com.mezzofy.mzcoupon.Entity.CustomerEntity;

import com.mezzofy.mzcoupon.Database.MojodomoDB;

import com.mezzofy.mzcoupon.Dao.Customer_Dao;
import com.mezzofy.mzcoupon.Entity.CountryList;
import com.mezzofy.mzcoupon.Entity.CustomerDevicemEntity;
import com.mezzofy.mzcoupon.Entity.CustomerGroupmEntity;
import com.mezzofy.mzcoupon.Entity.CustomermEntity;
import com.mezzofy.mzcoupon.Entity.MzCountryList;


import org.json.JSONException;

import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Customer_Module {

   private MZCustomer customerModule;
    private MojodomoDB dbhelper;


    public Customer_Module(Context context) {
        dbhelper = new MojodomoDB(context);
        customerModule=new MZCustomer(context);
    }

    public boolean addStaff(CustomerEntity staffRes) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Customer_Dao daouser = dbhelper.getCustomerDao(con);
            res = daouser.addUser(staffRes);

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

    public boolean updateUser(String customerId, String loginName, String mobile) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Customer_Dao daouser = dbhelper.getCustomerDao(con);
            res = daouser.updateUser(customerId, loginName, mobile);

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

    public boolean updatePwd(String customerId, String pwd) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Customer_Dao daouser = dbhelper.getCustomerDao(con);
            res = daouser.updatePwd(customerId, pwd);

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

    public boolean logoutUser(String customerId) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Customer_Dao daouser = dbhelper.getCustomerDao(con);
            res = daouser.logoutUser(customerId);

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


    public boolean addCustomerGrp(CustomerGroupmEntity customerGroupModel) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Customer_Dao daouser = dbhelper.getCustomerDao(con);

            res=daouser.DeleteCustomerGrpDetail();
            if(res) {
                res = daouser.addCustomerGrp(customerGroupModel.getCustomergroup());
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


    public boolean UpdateCustomer(CustomermEntity customerModel) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Customer_Dao daouser = dbhelper.getCustomerDao(con);

            res = daouser.UpdateCustomer(customerModel.getCustomer());

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

    public boolean ChangePassword(CustomermEntity customerModel) throws Exception {
        boolean res = false;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            con.setAutoCommit(false);
            Customer_Dao daouser = dbhelper.getCustomerDao(con);

            res = daouser.ChangePassword(customerModel.getCustomer());

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

    public CustomerEntity getUser(String useremail, String password) throws Exception {
        CustomerEntity objuser = null;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Customer_Dao daouser = dbhelper.getCustomerDao(con);
            objuser = daouser.getUser(useremail, password);

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
        return objuser;
    }


    public CustomerEntity getUser() throws Exception {
        CustomerEntity objuser = null;
        Connection con = null;
        try {
            con = dbhelper.getConnection();
            Customer_Dao daouser = dbhelper.getCustomerDao(con);
            objuser = daouser.getUser();

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
        return objuser;
    }




    public CustomermEntity getforgetpwd(CustomermEntity customermEntity) throws APIServerException {

        CustomermEntity resp = null;
        try {

            CustomerDataModel customermData=(CustomerDataModel) JsonMapper.mapJsonToObj(customermEntity, CustomerDataModel.class);

            CustomerDataModel retcustomermData=customerModule.PasswordForgot(customermData);
            if(retcustomermData!=null)
                 resp=(CustomermEntity) JsonMapper.mapJsonToObj(retcustomermData, CustomermEntity.class);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resp;
    }

    public CustomermEntity Changepassward(CustomermEntity customermEntity) throws APIServerException {

        CustomermEntity resp = null;
        try {
            CustomerDataModel customermData=(CustomerDataModel) JsonMapper.mapJsonToObj(customermEntity, CustomerDataModel.class);

            CustomerDataModel retcustomermData=customerModule.PasswordChange(customermData);
            if(retcustomermData!=null)
                resp=(CustomermEntity) JsonMapper.mapJsonToObj(retcustomermData, CustomermEntity.class);

        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return resp;
    }

    public CustomerGroupmEntity getcustomergroup(String customergroupid) {

        CustomerGroupmEntity customerGroupmEntity=null;
        CustomerGroupDataModel customerGroupmData=customerModule.GetCustomerGroupById(customergroupid);
        if(customerGroupmData!=null)
            try {
                customerGroupmEntity=(CustomerGroupmEntity) JsonMapper.mapJsonToObj(customerGroupmData, CustomerGroupmEntity.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return customerGroupmEntity;
    }


    public static String VersionCheck() {
//        String ver = JSONSTRINGS.getJSONFromUrl(CommonModule.getUrlpath4() + "androidversionv4");
//        if (ver == null || ver.equals("")) {
//            ver = "0";
//        }
        return null;
    }


    public  CustomermEntity getfblogin(CustomerDevicemEntity customerDevicemEntity)throws APIServerException {

        CustomermEntity resp = null;
        try {

            CustomerDeviceDataModel customerDevicemData=(CustomerDeviceDataModel) JsonMapper.mapJsonToObj(customerDevicemEntity, CustomerDeviceDataModel.class);

            CustomerDataModel retcustomermData=customerModule.LoginFacebook(customerDevicemData);
            if(retcustomermData!=null)
                resp=(CustomermEntity) JsonMapper.mapJsonToObj(retcustomermData, CustomermEntity.class);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resp;
    }


    public CustomermEntity getlogin(CustomerDevicemEntity customerDevicemEntity)throws APIServerException {

        CustomermEntity resp = null;
        try {

            CustomerDeviceDataModel customerDevicemData=(CustomerDeviceDataModel) JsonMapper.mapJsonToObj(customerDevicemEntity, CustomerDeviceDataModel.class);

            CustomerDataModel retcustomermData=customerModule.LoginPlatform(customerDevicemData);
            if(retcustomermData!=null)
                resp=(CustomermEntity) JsonMapper.mapJsonToObj(retcustomermData, CustomermEntity.class);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resp;
    }



    public CustomermEntity customer_update(CustomermEntity customermEntity)throws APIServerException {

        CustomermEntity resp = null;
        try {

            CustomerDataModel customermData=(CustomerDataModel) JsonMapper.mapJsonToObj(customermEntity, CustomerDataModel.class);

            CustomerDataModel retcustomermData=customerModule.CustomerUpdate(customermData);
            if(retcustomermData!=null)
                resp=(CustomermEntity) JsonMapper.mapJsonToObj(retcustomermData, CustomermEntity.class);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resp;
    }


    public CustomermEntity signup(CustomermEntity customermEntity) throws APIServerException{


        CustomermEntity resp = null;
        try {

            CustomerDataModel customermData=(CustomerDataModel) JsonMapper.mapJsonToObj(customermEntity, CustomerDataModel.class);

            CustomerDataModel retcustomermData=customerModule.CustomerSignup(customermData);
            if(retcustomermData!=null)
                resp=(CustomermEntity) JsonMapper.mapJsonToObj(retcustomermData, CustomermEntity.class);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resp;

    }


    public ArrayList<MzCountryList> getCountrylist() {
        ArrayList<MzCountryList> countryLists = new ArrayList<MzCountryList>();
        CountryList countryList=null;
        try {
            CountryListDataModel countryListmData=customerModule.GetCountries();
            if(countryListmData!=null)
                countryList=(CountryList) JsonMapper.mapJsonToObj(countryListmData, CountryList.class);
                 if(countryList!=null){
                     countryLists = countryList.getCountries();
                 }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countryLists;
    }


}
