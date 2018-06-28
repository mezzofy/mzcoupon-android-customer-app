package com.mezzofy.mzcoupon.module;

import android.content.Context;

import com.mezzofy.MzCouponAPI.data.MerchantDataModel;
import com.mezzofy.MzCouponAPI.mapper.JsonMapper;
import com.mezzofy.MzCouponAPI.module.MZMerchant;
import com.mezzofy.mzcoupon.Entity.MerchantEntity;
import com.mezzofy.mzcoupon.Database.MojodomoDB;
import com.mezzofy.mzcoupon.Dao.Merchant_Dao;
import com.mezzofy.mzcoupon.Entity.MerchantmEntity;

import org.json.JSONException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;




public class Merchant_Module {
	private MojodomoDB dbhelper;
	private MZMerchant merchantModule;


	public Merchant_Module(Context context) {
		dbhelper = new MojodomoDB(context);
		merchantModule=new MZMerchant(context);
	}

	public MerchantEntity getMerchantfromserver(String merchantId) {
		MerchantEntity merchantdatas = null;

		MerchantDataModel merchantmData=merchantModule.GetMerchantById(merchantId);

		if(merchantmData!=null)
		{
			try {
				MerchantmEntity merchantmEntity=(MerchantmEntity) JsonMapper.mapJsonToObj(merchantmData, MerchantmEntity.class);
				merchantdatas=merchantmEntity.getMerchant();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return merchantdatas;

	}

	public MerchantEntity getMerchantList(String merchantId) throws Exception{

		MerchantEntity merchantData=new MerchantEntity();
		Connection con=null;
		try {
			con = dbhelper.getConnection();
			Merchant_Dao daomerchant = dbhelper.getMerchantDao(con);
			merchantData=daomerchant.getMerchantList(merchantId);
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
		return merchantData;
	}




	public boolean DeleteChartTable() throws Exception{
		boolean res = false;
		Connection con=null;
		try {
			con = dbhelper.getConnection();
			con.setAutoCommit(false);
			Merchant_Dao daomerchant = dbhelper.getMerchantDao(con);
			res=daomerchant.DeleteChartTable();

			if(res)
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

	public MerchantEntity getMerchantList() throws Exception{

		MerchantEntity merchantData=new MerchantEntity();
		Connection con=null;
		try {
			con = dbhelper.getConnection();
			Merchant_Dao daomerchant = dbhelper.getMerchantDao(con);
			merchantData=daomerchant.getMerchantList();

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
		return merchantData;
	}

	public Boolean updateMerchantSetting(MerchantEntity merchantData) throws Exception{

		boolean res=false;
		Connection con=null;
		try {
			con = dbhelper.getConnection();
			con.setAutoCommit(false);
			Merchant_Dao daomerchant = dbhelper.getMerchantDao(con);
			res=daomerchant.updateMerchantProfile1(merchantData);

			if(res)
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

    public Boolean updateMerchantProfile1(MerchantEntity merchantData) throws Exception{

        boolean res=false;
        Connection con=null;
        try {
            con = dbhelper.getConnection();
			con.setAutoCommit(false);
            Merchant_Dao daomerchant = dbhelper.getMerchantDao(con);
            res=daomerchant.updateMerchantProfile1(merchantData);

			if(res)
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

	public Boolean addMerchant(MerchantEntity merchantData) throws Exception{

		boolean res=false;
		Connection con=null;
		try {
			con = dbhelper.getConnection();
			con.setAutoCommit(false);
			Merchant_Dao daomerchant = dbhelper.getMerchantDao(con);
			res = daomerchant.addMerchant(merchantData);

			if(res)
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
