package com.mezzofy.mzcoupon.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mezzofy.mzcoupon.Dao.Campaign_Dao;
import com.mezzofy.mzcoupon.Dao.Cart_Dao;
import com.mezzofy.mzcoupon.Dao.Coupon_Dao;
import com.mezzofy.mzcoupon.Dao.CouponOrder_Dao;
import com.mezzofy.mzcoupon.Dao.Customer_Dao;
import com.mezzofy.mzcoupon.Dao.MerchantSite_Dao;
import com.mezzofy.mzcoupon.Dao.Merchant_Dao;
import com.mezzofy.mzcoupon.Dao.Payment_Dao;
import com.mezzofy.mzcoupon.Dao.PoOrder_Dao;
import com.mezzofy.mzcoupon.Dao.Setting_Dao;
import com.mezzofy.mzcoupon.Dao.WalletTransaction_Dao;
import com.mezzofy.mzcoupon.Dao.Wallet_Dao;

import org.sqldroid.SQLDroidConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by aruna on 7/26/17.
 */

public class MojodomoDB {
    private static final int DATABASE_VERSION = 1;

    static final String JDBC_DRIVER = "org.sqldroid.SQLDroidDriver";

    // Database Name
    private static final String DATABASE_NAME = "mojodomoDb";

    //MZCustomer Name
    private static final String TABLE_CUSTOMER = "tbl_customer";
    private static final String TABLE_CUSTOMER_GROUP = "tbl_customer_group";
    private static final String TABLE_CUSTOMER_DEVICE_DETAIL = "tbl_customer_devicedetail";

    //MZMerchant
    private static final String TABLE_MERCHANT = "tbl_merchant";

    //campaign
    private static final String TABLE_CAMPAIGN = "tbl_campaign";
    private static final String TABLE_CAMPAIGN_Group = "tbl_campaign_group";
    private static final String TABLE_CAMPAIGN_GROUP_DETAIL = "tbl_campaign_group_detail";
    private static final String TABLE_CAMPAIGN_IMAGE = "tbl_campaign_image";
    private static final String TABLE_CAMPAIGN_SITE = "tbl_campaign_site";

    //setting check
    private static final String TABLE_SETTINGS = "tbl_setting";

    //Order

    private static final String TABLE_PO_ORDER= "tbl_po_order";
    private static final String TABLE_PO_ORDER_DTL= "tbl_po_order_item";

    //MZSite
    private static final String TABLE_SITE = "tbl_site";

    //MZCoupon
    private static final String TABLE_COUPON = "tbl_coupon";
    private static final String TABLE_COUPONCOUNT = "tbl_couponcount";

    //MZCoupon Order
    private static final String TABLE_COUPON_ORDER= "tbl_coupon_order";
    private static final String TABLE_COUPON_ORDER_ITEM= "tbl_coupon_order_item";
    private static final String TABLE_COUPON_ORDER_MOD= "tbl_coupon_order_modifier";


    //Cart
    private static final String TABLE_CART= "tbl_cart";

    //MZPayment Detail
    private static final String TABLE_PAYMENT_DETAIL= "tbl_payment_detail";

    //MZWallet

    private static final String TABLE_WALLET_BALANCE= "tbl_wallet_balance";
    private static final String TABLE_WALLET_TRANSCATION = "tbl_wallet_transcation";

    //MZPayment
    private static final String TABLE_PAYMENT_DTL= "tbl_payment_dtl";



    private Context mcontext;
    private static DBHelper mDBHelper;
    private static SQLDroidConnection con;
    private static int count;

    public MojodomoDB(Context context) {

        this.mcontext = context;
        if (mDBHelper == null) {
            mDBHelper = new DBHelper(mcontext);
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            db.close();
            Log.i("DATABASE-CREATE", "first");
        }
    }

    static {
        try {
            Class.forName(JDBC_DRIVER).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getURL(){
        String url = "jdbc:sqldroid:" + mcontext.getDatabasePath(DATABASE_NAME).getAbsolutePath();
        return url;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (con == null) {
            con = (SQLDroidConnection) DriverManager.getConnection(getURL());
            count += 1;
        }
        return con;
    }

    public synchronized void releaseConnection() throws SQLException {
        if (count > 1) {
            count -= 1;
        } else {
            if (con != null) {
                con.close();
                con = null;
            }
        }
    }

    public Customer_Dao getCustomerDao(Connection connection){
        Customer_Dao daoret=null;
        if(connection!=null){
            daoret=new Customer_Dao(connection);
        }
        return daoret;

    }

    public Setting_Dao getsettingDao(Connection connection){
        Setting_Dao daoret=null;
        if(connection!=null){
            daoret=new Setting_Dao(connection);
        }
        return daoret;

    }
    public Campaign_Dao getcampaignDao(Connection connection){
        Campaign_Dao daoret=null;
        if(connection!=null){
            daoret=new Campaign_Dao(connection);
        }
        return daoret;
    }

    public Merchant_Dao getMerchantDao(Connection connection){
        Merchant_Dao daoret=null;
        if(connection!=null){
            daoret=new Merchant_Dao(connection);
        }
        return daoret;

    }

    public MerchantSite_Dao getMerchantSiteDao(Connection connection){
        MerchantSite_Dao daoret=null;
        if(connection!=null){
            daoret=new MerchantSite_Dao(connection);
        }
        return daoret;
    }
    public Wallet_Dao getWalletDao(Connection connection){
        Wallet_Dao daoret=null;
        if(connection!=null){
            daoret=new Wallet_Dao(connection);
        }
        return daoret;
    }
    public Cart_Dao getCartDao(Connection connection){
        Cart_Dao daoret=null;
        if(connection!=null){
            daoret=new Cart_Dao(connection);
        }
        return daoret;
    }

    public PoOrder_Dao getOrderDao(Connection connection){
        PoOrder_Dao daoret=null;
        if(connection!=null){
            daoret=new PoOrder_Dao(connection);
        }
        return daoret;

    }

    public Coupon_Dao getCouponDao(Connection connection){
        Coupon_Dao daoret=null;
        if(connection!=null){
            daoret=new Coupon_Dao(connection);
        }
        return daoret;
    }

    public CouponOrder_Dao getCouponOrderDao(Connection connection){
        CouponOrder_Dao daoret=null;
        if(connection!=null){
            daoret=new CouponOrder_Dao(connection);
        }
        return daoret;
    }

    public Payment_Dao getPaymentDao(Connection connection){
        Payment_Dao daoret=null;
        if(connection!=null){
            daoret=new Payment_Dao(connection);
        }
        return daoret;

    }

    public WalletTransaction_Dao getWalletTransactionDao(Connection connection){
        WalletTransaction_Dao daoret=null;
        if(connection!=null){
            daoret=new WalletTransaction_Dao(connection);
        }
        return daoret;

    }


    public final class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            upgradeDBTablesV2(db);
        }
    }

    private void createTable(SQLiteDatabase db) {

        String SQL_CREATE_TBL_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMER + "  (" +
                "tbl_user_ID  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "customerId TEXT," +
                "merchantId TEXT," +
                "customerGroupId TEXT," +
                "customerFirstName TEXT," +
                "customerLastName TEXT," +
                "customerPassword TEXT," +
                "customerEmail TEXT," +
                "countryCode TEXT," +
                "customerDob TEXT," +
                "customerMobile TEXT," +
                "customerGender TEXT," +
                "customerAddress TEXT," +
                "userType TEXT," +
                "vipNo TEXT," +
                "dgvipNo TEXT," +
                "logout_flag TEXT," +
                "fbToken TEXT," +
                "customerStatus TEXT," +
                "hashCode TEXT," +
                "createdOn TEXT," +
                "updatedOn TEXT," +
                "updatedBy TEXT," +
                "customerUsername TEXT," +
                "customerImageUrl TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_CUSTOMER);


        String SQL_CREATE_TBL_CUSTOMER_DEVICEDETAIL = "CREATE TABLE " + TABLE_CUSTOMER_DEVICE_DETAIL + "  (" +
                "tbl_customerdevice_ID  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "custDeviceId TEXT," +
                "customerId TEXT," +
                "deviceToken TEXT," +
                "deviceName TEXT," +
                "deviceUuid TEXT," +
                "updatedOn TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_CUSTOMER_DEVICEDETAIL);


        String SQL_CREATE_TBL_CAMPAIGN = "CREATE TABLE " + TABLE_CAMPAIGN + "  (" +
                "tbl_campaignId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "campaignId TEXT,"+
                "merchantId TEXT,"+
                "productId TEXT,"+
                "productmerchantId TEXT,"+
                "campaignCode TEXT,"+
                "campaignType TEXT,"+
                "brand TEXT,"+
                "campaignName TEXT,"+
                "campaignDesc TEXT,"+
                "sellingPrice double,"+
                "orginalPrice double,"+
                "campaignTc TEXT,"+
                "campaignStatus TEXT,"+
                "dailyLimit INTEGER,"+
                "packQty INTEGER,"+
                "emailStaff TEXT,"+
                "couponOver TEXT,"+
                "dayFilter TEXT,"+
                "expiryDays INTEGER,"+
                "fromDate TEXT,"+
                "createdOn TEXT,"+
                "toDate TEXT,"+
                "expirydue TEXT,"+
                "expiryname TEXT,"+
                "totalRedeem INTEGER,"+
                "allocationCount INTEGER,"+
                "couponUrl TEXT,"+
                "passbookUrl TEXT,"+
                "issuedcoupon INTEGER,"+
                "redeemcoupon INTEGER,"+
                "hashCode TEXT,"+
                "deleteflag TEXT,"+
                "Topten Text,"+
                "Favourite Text,"+
                "campaignNote1 Text,"+
                "campaignNote2 Text,"+
                "campaignNote3 Text,"+
                "distance INTEGER,"+
                "campaignUuid Text,"+
                "typeService Text,"+
                "reviewUrl Text,"+
                "videoUrl Text,"+
                "txBrand Text,"+
                "campaignRemark Text,"+
                "pickup Text,"+
                "delivery INTEGER,"+
                "booking Text,"+
                "outcall Text,"+
                "dailyLimitType Text,"+
                "allocationcoupon INTEGER"+
                ")";

        db.execSQL(SQL_CREATE_TBL_CAMPAIGN);

        String SQL_CREATE_CAMPAIGN_GROUP_DETAIL = "CREATE TABLE " +  TABLE_CAMPAIGN_GROUP_DETAIL+ "  (" +
                "tbl_campgrp_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "campaignId TEXT," +
                "campgrpId TEXT" +
                ")";
        db.execSQL(SQL_CREATE_CAMPAIGN_GROUP_DETAIL);


        String  SQL_CREATE_TABLE_CAMPAIGN_Group= "CREATE TABLE " + TABLE_CAMPAIGN_Group + "  (" +
                "tbl_campgrp_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "campgrpId TEXT," +
                "merchantId TEXT," +
                "campgrpName TEXT," +
                "campgrpImageurl TEXT," +
                "campgrpSeq INTEGER," +
                "hashCode TEXT," +
                "updatedOn TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TABLE_CAMPAIGN_Group);

        String SQL_CREATE_TBL_CAMPAIGN_IMAGE = "CREATE TABLE " + TABLE_CAMPAIGN_IMAGE + "  (" +
                "tbl_campaignImage_ID  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "pimageId TEXT," +
                "campaignId TEXT," +
                "campaignImage TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_CAMPAIGN_IMAGE);

        String SQL_CREATE_TBL_CAMPAIGN_SITE = "CREATE TABLE " + TABLE_CAMPAIGN_SITE + "  (" +
                "tbl_campaignSite_ID  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "siteId TEXT," +
                "campaignId TEXT," +
                "assigned TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_CAMPAIGN_SITE);

        String SQL_CREATE_TBL_SETTINGS = "CREATE TABLE " + TABLE_SETTINGS + "  (" +
                "keyid TEXT," +
                "keyvalue TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_SETTINGS);


        String SQL_CREATE_CUSTOMER_GROUP_DETAIL = "CREATE TABLE " + TABLE_CUSTOMER_GROUP + "  (" +
                "tbl_customergrp_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "customerGroupId TEXT," +
                "merchantId TEXT," +
                "groupName TEXT," +
                "groupStatus TEXT" +
                "dollarSpending TEXT," +
                "rewardPoint TEXT," +
                "updatedOn TEXT," +
                "deleteflag TEXT,"+
                "hashCode TEXT" +
                ")";

        db.execSQL(SQL_CREATE_CUSTOMER_GROUP_DETAIL);

        String SQL_CREATE_TBL_PO_ORDER = "CREATE TABLE " + TABLE_PO_ORDER + "  (" +
                "tbl_Payment_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "poId TEXT," +
                "merchantId TEXT," +
                "customerId TEXT," +
                "customerName TEXT," +
                "poDate TEXT," +
                "poNo TEXT," +
                "poStatus TEXT," +
                "payResponse TEXT," +
                "hashCode TEXT," +
                "updatedOn TEXT," +
                "couponType TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_PO_ORDER);

        String SQL_CREATE_TBL_PO_ORDER_DTL = "CREATE TABLE " + TABLE_PO_ORDER_DTL + "  (" +
                "tbl_order_dtl_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "podtlId TEXT," +
                "productId TEXT," +
                "campaignId TEXT," +
                "poId TEXT," +
                "campaignName TEXT," +
                "campaignPrice TEXT," +
                "campaignQty TEXT," +
                "campaignTotal TEXT," +
                "status TEXT," +
                "updatedOn TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_PO_ORDER_DTL);

        String SQL_CREATE_TBL_SITE = "CREATE TABLE " + TABLE_SITE + "  (" +
                "tbl_site_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "siteId TEXT," +
                "merchantId TEXT," +
                "siteName TEXT," +
                "siteAddress TEXT," +
                "siteLatitude DOUBLE," +
                "siteLongitude DOUBLE," +
                "siteLocation TEXT," +
                "siteContact TEXT," +
                "siteDesc TEXT," +
                "siteEmailId TEXT," +
                "siteDisplayWallet ," +
                "siteSeqNo INTEGER," +
                "siteImageurl TEXT," +
                "siteOnlineStatus TEXT," +
                "siteStatus TEXT," +
                "siteRedeemPass TEXT," +
                "hashCode TEXT," +
                "locationId TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_SITE);


        String SQL_CREATE_TBL_COUPON = "CREATE TABLE " + TABLE_COUPON + "  (" +
                "tbl_Coupon_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "couponId TEXT," +
                "productId TEXT," +
                "allocationId TEXT," +
                "campaignId TEXT," +
                "customerId TEXT," +
                "campaignCode TEXT," +
                "couponNo TEXT," +
                "purchaseDate TEXT," +
                "redeemDate TEXT," +
                "startDate TEXT," +
                "endDate TEXT," +
                "couponName TEXT," +
                "couponStatus TEXT," +
                "hashCode TEXT," +
                "createdOn TEXT," +
                "updatedOn TEXT," +
                "productNote1 TEXT," +
                "productNote2 TEXT," +
                "productNote3 TEXT," +
                "sellingPrice Double," +
                "orginalPrice Double," +
                "productDesc TEXT," +
                "productImageurl TEXT," +
                "deleteflag TEXT"+
                ")";
        db.execSQL(SQL_CREATE_TBL_COUPON);

        String SQL_CREATE_TBL_COUPONCOUNT = "CREATE TABLE " + TABLE_COUPONCOUNT + "  (" +
                "tbl_Couponcount_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "campaignId TEXT," +
                "size TEXT," +
                "deleteflag TEXT"+
                ")";
        db.execSQL(SQL_CREATE_TBL_COUPONCOUNT);

        String SQL_CREATE_TBL_MERCHANT = "CREATE TABLE " + TABLE_MERCHANT + "  (" +
                "tbl_merchant_ID  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "merchantId TEXT," +
                "merchantName TEXT," +
                "merchantDesc TEXT," +
                "merchantLogo TEXT," +
                "backgroundImage TEXT," +
                "remark TEXT," +
                "status TEXT," +
                "countryCode TEXT," +
                "CountryName TEXT,"+
                "merchantCode TEXT," +
                "merchantType TEXT," +
                "merchantLogourl TEXT," +
                "merchantImageurl TEXT," +
                "merchantTc TEXT," +
                "hashCode TEXT," +
                "merchantEmail TEXT,"+
                "profileStatus TEXT," +
                "merchantHotline TEXT," +
                "merchantStatus TEXT," +
                "channelCode TEXT,"+
                "currency TEXT,"+
                "merchantTimezone TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_MERCHANT);


        String SQL_CREATE_TBL_ORDER = "CREATE TABLE " + TABLE_COUPON_ORDER + "  (" +
                "tbl_Order_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "couponId TEXT," +
                "merchantId TEXT," +
                "siteId TEXT," +
                "productId TEXT," +
                "customerId TEXT," +
                "customerName TEXT," +
                "trackId TEXT," +
                "orderDate TEXT," +
                "startendTime TEXT," +
                "orderType TEXT," +
                "orderRemark TEXT," +
                "contactNo TEXT," +
                "orderNo TEXT," +
                "hashCode TEXT," +
                "orderStatus TEXT," +
                "pushRead TEXT," +
                "customerAddress TEXT," +
                "updatedOn TEXT," +
                "deleteflag TEXT,"+
                "pickupDeliveryType TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_ORDER);


        String SQL_CREATE_TBL_ORDER_ITEM = "CREATE TABLE " + TABLE_COUPON_ORDER_ITEM + "  (" +
                "tbl_order_dtl_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "orderItemId TEXT," +
                "couponId TEXT," +
                "itemId TEXT," +
                "itemName TEXT," +
                "deleteflag TEXT,"+
                "groupName TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_ORDER_ITEM);

        String SQL_CREATE_TBL_ORDER_MOD = "CREATE TABLE " + TABLE_COUPON_ORDER_MOD + "  (" +
                "tbl_orderModifier_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "orderModifierId TEXT," +
                "orderItemId TEXT," +
                "modifierId TEXT," +
                "deleteflag TEXT,"+
                "modifierName TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_ORDER_MOD);

        String SQL_CREATE_TBL_CART = "CREATE TABLE " + TABLE_CART + "  (" +
                "tbl_cart_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "campaignId TEXT," +
                "merchantId TEXT," +
                "orginalPrice TEXT," +
                "campaignCode TEXT,"+
                "campaignName TEXT," +
                "campaignDesc TEXT," +
                "sellingPrice TEXT," +
                "campaignTc TEXT,"+
                "campaignStatus TEXT," +
                "reviewUrl TEXT," +
                "productQty TEXT," +
                "campaignImage TEXT,"+
                "totalPrice TEXT," +
                "status TEXT," +
                "flag TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_CART);

//        String SQL_CREATE_TBL_PAYMENT_DETAIL = "CREATE TABLE " + TABLE_PAYMENT_DETAIL + "  (" +
//                "tbl_payment_dtl_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
//                "paymentDetailId TEXT," +
//                "paymentId TEXT," +
//                "paymentLogourl TEXT," +
//                "paymentName TEXT,"+
//                "paymentMerchantId TEXT," +
//                "paymentKey TEXT," +
//                "paymentToken TEXT," +
//                "paymentUrl TEXT,"+
//                "paymentStatus TEXT," +
//                "paymentType TEXT," +
//                "paymentEnviornment TEXT," +
//                "currency TEXT,"+
//                "hashCode TEXT," +
//                "updatedOn TEXT," +
//                "delflag TEXT" +
//                ")";
//        db.execSQL(SQL_CREATE_TBL_PAYMENT_DETAIL);

        String SQL_CREATE_TBL_WALLET_BALANCE = "CREATE TABLE " + TABLE_WALLET_BALANCE + "  (" +
                "tbl_WalletId  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "walletId TEXT," +
                "customerId TEXT," +
                "walletCredit TEXT," +
                "qrCode TEXT," +
                "qr_time TEXT, " +
                "rewardPoint TEXT, " +
                "updatedOn TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_WALLET_BALANCE);


        String SQL_CREATE_TBL_PAYMENT_DTL = "CREATE TABLE " + TABLE_PAYMENT_DTL + "  (" +
                "tbl_payment_dtl_id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "paymentDetailId TEXT," +
                "merchantId TEXT," +
                "paymentId TEXT," +
                "paymentName TEXT," +
                "paymentLogourl TEXT," +
                "paymentMerchantId TEXT," +
                "paymentKey TEXT," +
                "paymentToken TEXT," +
                "paymentUrl TEXT," +
                "paymentStatus TEXT," +
                "hashCode TEXT," +
                "paymentType TEXT," +
                "paymentEnviornment TEXT," +
                "currency TEXT,"+
                "updatedOn TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_PAYMENT_DTL);


        String SQL_CREATE_TBL_WALLET_TRANSCATION = "CREATE TABLE " + TABLE_WALLET_TRANSCATION + "  (" +
                "tbl_transId  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "transactionReference TEXT," +
                "userId TEXT," +
                "merchantId TEXT," +
                "siteId TEXT," +
                "customerId TEXT," +
                "walletId TEXT," +
                "transactionType TEXT," +
                "transactionAmount TEXT," +
                "transactionPayRef TEXT," +
                "paidOn TEXT," +
                "processBy TEXT," +
                "status TEXT," +
                "transactionNotes TEXT," +
                "hashCode TEXT," +
                "transactionDate TEXT," +
                "paymentResponse TEXT," +
                "rewardPoint TEXT," +
                "updatedOn TEXT," +
                "deleteflag TEXT," +
                "approvalCode TEXT" +
                ")";
        db.execSQL(SQL_CREATE_TBL_WALLET_TRANSCATION);
    }

    private void upgradeDBTablesV2(SQLiteDatabase db) {

    }

    private void deleteTxnTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER_DEVICE_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER_GROUP);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPAIGN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPAIGN_GROUP_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPAIGN_Group);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPAIGN_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPAIGN_SITE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PO_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PO_ORDER_DTL);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUPON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUPONCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MERCHANT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUPON_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUPON_ORDER_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUPON_ORDER_MOD);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT_DETAIL);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALLET_BALANCE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT_DTL);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALLET_TRANSCATION);



    }

    public boolean clearTables() {
        boolean res = false;
        DBHelper helper = new DBHelper(mcontext);
        SQLiteDatabase database = helper.getWritableDatabase();
        try {
            deleteTxnTables(database);
            createTable(database);
            res = true;
        } finally {
            database.close();
        }
        return res;
    }

    // This method will return if your table exist a field or not
    private boolean isFieldExist(SQLiteDatabase db, String tableName, String fieldName) {
        boolean isExist = true;
        Cursor res = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        int value = res.getColumnIndex(fieldName);

        if (value == -1) {
            isExist = false;
        }
        return isExist;
    }

    private boolean isColumnExist(SQLiteDatabase db, String tableName, String fieldName) {
        boolean isExist = true;
        try {

            Cursor res = db.rawQuery("SELECT " + fieldName + " FROM " + tableName, null);
            int value = res.getColumnIndex(fieldName);

            if (value == -1) {
                isExist = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isExist = false;
        }
        return isExist;
    }
}
