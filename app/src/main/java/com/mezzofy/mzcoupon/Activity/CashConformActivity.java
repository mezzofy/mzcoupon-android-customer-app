package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Entity.CartEntity;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.R;

import com.mezzofy.mzcoupon.module.Cart_Module;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.pojo.TransactionRes;
import com.mezzofy.mzcoupon.pojo.Transactiondtl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by udhayinforios on 24/11/15.
 */
public class CashConformActivity extends Activity implements View.OnClickListener {

    String staff_name, email, mobile, msg;

    int staff_id;

    TextView email_text, staff_name_text, mobile_text;

    String topup, remark;

    double total;

    List<CartEntity> cartList = null;

    private Cart_Module cartModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_conform_layout);

        initialise();

        Button conform = (Button) findViewById(R.id.conform);
        conform.setOnClickListener(this);

    }

    private void initialise() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            staff_id = extras.getInt("staffid", 0);
            staff_name = extras.getString("staff_name", "");
            email = extras.getString("email", "");
            mobile = extras.getString("mobile", "");
            msg = extras.getString("message", "");
            topup = extras.getString("topup", "");
            total = extras.getDouble("total");
            remark = extras.getString("remark");
        }

        cartModule = new Cart_Module(getApplicationContext());
        email_text = (TextView) findViewById(R.id.text1);
        mobile_text = (TextView) findViewById(R.id.text2);
        staff_name_text = (TextView) findViewById(R.id.text3);

        email_text.setText(email);
        mobile_text.setText(String.valueOf(mobile));
        staff_name_text.setText(staff_name);
    }

    @Override
    public void onClick(View v) {

        String val = UUID.randomUUID().toString();

        if (topup != null && topup.equals("topup")) {

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            int userId = settings.getInt("staff_id", 0);
            String Msg = settings.getString("Msg", "");
            String RespNo = settings.getString("RespNo", "");
            String RefNo = settings.getString("RefNo", "");
            String Respcode = settings.getString("Respcode", "");
            int trans_id = settings.getInt("trans_id", 0);

//            TranscationModule module = new TranscationModule(getApplicationContext());
//            WalletServer wallet = module.getwallet(userId);

//            if (wallet.getResult().getResponse_code().equals("0")) {
//
//
//                WalletServer walletServer = new WalletServer();
//                MZWalletTransaction walletTransaction = new MZWalletTransaction();
//
////                        walletTransaction.setUser_id();
////                        walletTransaction.setMerchant_id(mechantSite.getMerchantId());
////                        walletTransaction.setSite_id(siteid);
//                walletTransaction.setTransaction_pay_ref("Cash");
//                walletTransaction.setCustomer_id(userId);
//                walletTransaction.setTransaction_amount(total);
//                walletTransaction.setTransaction_source(3);
//                walletTransaction.setTransaction_notes((remark));
//                walletTransaction.setCreated_on(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()));
//
//                walletServer.setWallet_transaction(walletTransaction);
//
//                WalletServer server = module.postTopup(walletServer);
//
//                if (server.getResult().getResponse_code().equals("0")) {
//
//                    editor.putString("Msg", "Cash");
//                    editor.commit();
//
//                    Intent intent = new Intent(getApplicationContext(), Payment_SucceActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("pay_succe", "pay_succe");
//                    intent.putExtra("topup", topup);
//                    intent.putExtra("total", total);
//                    startActivity(intent);
//                    overridePendingTransition(0, 0);
//                    finish();
//                } else {
//
//                    editor.putString("Msg", "decline");
//                    editor.commit();
//                    Intent intent = new Intent(getApplicationContext(), Payment_SucceActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("pay_fail", "pay_fail");
//                    intent.putExtra("topup", topup);
//                    intent.putExtra("total", total);
//                    startActivity(intent);
//                    overridePendingTransition(0, 0);
//                    finish();
//                }
//            }
        } else {

            Customer_Module userModule = new Customer_Module(getApplicationContext());

            CustomerEntity staffRes = null;
            try {
                staffRes = userModule.getUser();
            } catch (Exception e) {
                e.printStackTrace();
            }

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();

            editor.putString("Msg", "approved");
            editor.putString("RespNo", "");
            editor.putString("RefNo", val);
            editor.putString("Respcode", "0");
            editor.commit();

            TransactionRes Res = new TransactionRes();
            Res.setTransactionNo(val);
            Res.setPayReceipt("");
            Res.setPayResponse("0");
            Res.setTransactionId(0);
            Res.setPlaceId(staffRes.getMerchantId());
            Res.setCustomerId(staffRes.getCustomerId());
            try {
                Res.setTransactionTotal(Double.valueOf(cartModule.getTotalcart()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Res.setTransactionDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()).toString());
            Res.setHashCode("0");
            Res.setCreatedOn(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()).toString());
            Res.setStatus("A");
            Res.setCouponType("C");
            Res.setTransferId(String.valueOf(staff_id));
            Res.setTransferRemark(msg);

            cartModule = new Cart_Module(getApplicationContext());

            try {
                cartList = cartModule.getCartList();
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<Transactiondtl> transdtls = new ArrayList<Transactiondtl>();

            Transactiondtl transd = new Transactiondtl();
            for (CartEntity res : cartList) {
//                transd.setProdId(res.getProdId());
                transd.setProdName(res.getCampaignName());
//                transd.setProdPrice(res.getSellingPrice());
                transd.setProdQty(Double.valueOf(res.getProductQty()));
//                transd.setProdTotal(res.getTotalPrice());
                transdtls.add(transd);
            }

            Res.setTransdtls(transdtls);
//
//            TranscationModule module = new TranscationModule(getApplicationContext());
//            TransactionRes response = module.postTransc(Res);

//            editor.putInt("trans_id", response.getTransactionId());
//            editor.commit();
//
//            if (response.getTransactionId() != 0) {
//
//                editor.putString("Msg", "Cash");
//                editor.commit();
//
//                Intent intent = new Intent(getApplicationContext(), Payment_SucceActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("pay_succe", "pay_succe");
//                intent.putExtra("topup", topup);
//                intent.putExtra("total", total);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//                finish();
//            } else {
//
//                editor.putString("Msg", "decline");
//                editor.commit();
//                Intent intent = new Intent(getApplicationContext(), Payment_SucceActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("pay_fail", "pay_fail");
//                intent.putExtra("topup", topup);
//                intent.putExtra("total", total);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//                finish();
//            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
