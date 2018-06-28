package com.mezzofy.mzcoupon.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Entity.CartEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.module.Cart_Module;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.module.Wallet_Module;


import java.util.List;

/**
 * Created by udhayinforios on 7/3/16.
 */
public class PromerceDollarActivity extends Activity {

    RelativeLayout layout;
    final Context context = this;

    int prodid;

    String cupid, exp, qr_code;
    String pay_fail, pay_succe, tabbar, topup;
    String Msg, RespNo, RefNo, Respcode, msg_text, remark;

    Integer trans_id, transfer_id;
    String userId;

    ProgressDialog progressDialog;

    List<CartEntity> cartList = null;

    private Cart_Module cartModule;
    private Customer_Module userModule;

    Double total;


    Wallet_Module modwallet;


    TextView message;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);

            cartModule = new Cart_Module(context);
            userModule = new Customer_Module(context);

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            userId = settings.getString("staff_id", null);
            Msg = settings.getString("Msg", "");
            RespNo = settings.getString("RespNo", "");
            RefNo = settings.getString("RefNo", "");
            Respcode = settings.getString("Respcode", "");
            trans_id = settings.getInt("trans_id", 0);

            Bundle extras = getIntent().getExtras();
            pay_succe = extras.getString("pay_succe");
            pay_fail = extras.getString("pay_fail");
            msg_text = extras.getString("msg_text", null);
//        transfer_id = extras.getInt("transfer_id",0);
            tabbar = extras.getString("tabview");
            total = extras.getDouble("total");
            remark = extras.getString("remark");
            topup = extras.getString("topup");
            qr_code = extras.getString("qrcode", "");
            remark = extras.getString("remark");

            message = (TextView) findViewById(R.id.textView1);
            modwallet = new Wallet_Module(getApplicationContext());

            if (topup != null && topup.equals("topup")) {
                if (pay_succe != null && pay_succe.equals("pay_succe")) {
                    message.setText(getString(R.string.topup_success_alert));
                }

                if (pay_fail != null && pay_fail.equals("pay_fail")) {
                    message.setText(getString(R.string.topup_failure_alert));
                }
            }

            if (pay_succe != null)
                if (pay_succe.equals("pay_succe")) {
                    setContentView(R.layout.thank_payment);
                }

            if (pay_fail != null)
                if (pay_fail.equals("pay_fail")) {
                    setContentView(R.layout.payment_fail);
                }

            TextView cancelredeem = (TextView) findViewById(R.id.TextViewCal);
            cancelredeem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    if (pay_succe != null && pay_succe.equals("pay_succe")) {
                        AsyncCallWS task = new AsyncCallWS();
                        task.execute();
                    }

                    if (pay_fail != null && pay_fail.equals("pay_fail")) {
                        onBackPressed();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class AsyncCallWS extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");

            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(PromerceDollarActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");
//            TranscationModule module = new TranscationModule(getApplicationContext());
//
//            if (pay_succe != null && pay_succe.equals("pay_succe")) {
//                if (topup != null && topup.equals("topup")) {
//
//                    WalletDataModel wallet = modwallet.getWalletlistfromserver(userId);
//
//                    if (wallet!=null) {
//                        WalletServer walletServer = new WalletServer();
//                        MZWalletTransaction walletTransaction = new MZWalletTransaction();
//
////                        walletTransaction.setUser_id();
////                        walletTransaction.setMerchant_id(mechantSite.getMerchantId());
////                        walletTransaction.setSite_id(siteid);
//                        walletTransaction.setTransaction_pay_ref(RespNo);
////                        walletTransaction.setCustomer_id(userId);
//                        walletTransaction.setTransaction_amount(total);
//                        walletTransaction.setTransaction_source(3);
//                        walletTransaction.setTransaction_notes((remark));
//                        walletTransaction.setCreated_on(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()));
//
//                        walletServer.setWallet_transaction(walletTransaction);
//
//                        server = module.postTopup(walletServer);
//                    }
//
//                } else if (topup.equals("Paypal")) {
//
//                } else {
//                    WalletDataModel walletModel = modwallet.getwalletQRfromserver(userId);
//                    if (walletModel == null) {
//                        walletModel = new WalletDataModel();
//                    }
//
//                    MZWalletTransaction walletTransaction = new MZWalletTransaction();
//
//                    DecimalFormat df = new DecimalFormat("##0.00");
//                    String ref_no = UUID.randomUUID().toString();
//
////                    walletTransaction.setCustomer_id(userId);
//                    walletTransaction.setTransaction_pay_ref(ref_no);
//                    try {
//                        walletTransaction.setTransaction_amount(cartModule.getTotalcart());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        walletTransaction.setApproval_code(Utils.md5(Utils.md5(Utils.md5(walletModel.getWallet().getQrCode() + ref_no + df.format(cartModule.getTotalcart()) + userId))));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
////                    walletTransaction.setUser_id(userId);
////                    walletTransaction.setMerchant_id(0);
////                    walletTransaction.setSite_id(0);
////                    walletTransaction.setTransaction_notes("MZWallet MZPayment");
////                    walletTransaction.setCreated_on(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()));
////
////                    walletServer.setWallet_transaction(walletTransaction);
////
////                    retval = modwallet.qrPayment(walletServer);
//
//                    if (retval != null && retval.getResult().getResponse_code().equals("0")) {
//                        CustomerEntity staffRes = null;
//                        try {
//                            staffRes = userModule.getUser();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        TransactionRes Res = new TransactionRes();
//                        Res.setTransactionNo(UUID.randomUUID().toString());
//                        Res.setPayReceipt(retval.getWallet_transaction().getTransaction_pay_ref());
//                        Res.setPayResponse("0");
//                        Res.setTransactionId(0);
//                        Res.setCouponType("C");
//                        Res.setPlaceId(staffRes.getMerchantId());
//                        Res.setCustomerId(staffRes.getCustomerId());
//                        if (topup != null && topup.equals("topup")) {
//                            if (total != null)
//                                Res.setTransactionTotal(total);
//                        } else {
//                            try {
//                                Res.setTransactionTotal(cartModule.getTotalcart());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        Res.setTransactionDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()).toString());
//                        Res.setHashCode("0");
//                        Res.setCreatedOn(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()).toString());
//                        Res.setStatus("A");
////                        if (transfer_id == 0) {
////                            Res.setTransferId(String.valueOf(userId));
////                        } else {
//                        Res.setTransferId(String.valueOf(transfer_id));
////                        }
//                        Res.setTransferRemark(msg_text);
//
//                        List<Transactiondtl> transdtls = new ArrayList<Transactiondtl>();
//                        try {
//                            cartList = cartModule.getCartList();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        if (cartList != null && cartList.size() > 0)
//                            for (CartEntity res : cartList) {
//                                Transactiondtl transd = new Transactiondtl();
////                                transd.setProdId(res.getCampaignId());
//                                transd.setProdName(res.getCampaignName());
//                                transd.setProdPrice(Double.parseDouble(res.getOrginalPrice()));
//                                transd.setProdQty(Double.valueOf(res.getProductQty()));
//                                transd.setProdTotal(Double.parseDouble(res.getTotalPrice()));
//                                transdtls.add(transd);
//                            }
//
//                        Res.setTransdtls(transdtls);
//
//                        TransactionRes response = module.postTransc(Res);
//
//                        if (response != null) {
//                            TransactionRes result = module.getTransc(response.getTransactionId());
//
//                            if (result != null) {
//                                module.addTransc(result);
//                            }
//                        }
//                    }
//                }
//            }

            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();
            Intent intent = new Intent(context, TabViewActivtiy.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("tabName", "");
//            if (retval != null && retval.getResult().getResponse_code().equals("0")) {
//                try {
//                    cartModule.deletechart();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
            intent.putExtra("currTab", 3);
            int cout = 0;
            try {
                cout = cartModule.getItemcart();
            } catch (Exception e) {
                e.printStackTrace();
            }

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("Msg", null);
            editor.putString("RespNo", null);
            editor.putString("RefNo", null);
            editor.putString("Respcode", null);
            editor.putInt("notif_count", cout);
            editor.commit();

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }

//            if (pay_succe != null && pay_succe.equals("pay_succe"))
//                if (topup.equals("cart") || topup.equals("Paypal")) {
//                    cartDao.deletechart();
//                }


            if (topup.equals("cart") || topup.equals("Paypal")) {
                intent = new Intent(getApplicationContext(), CardsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            } else if (topup.equals("topup")) {
                intent.putExtra("tabName", "");
                intent.putExtra("currTab", 4);
            }
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }

    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), TabViewActivtiy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (topup.equals("topup")) {
            intent.putExtra("tabName", "");
            intent.putExtra("currTab", 4);
        } else {
            intent.putExtra("tabName", "");
            intent.putExtra("currTab", 3);
        }

        startActivity(intent);
        overridePendingTransition(0, 0);
        this.finish();
    }
}
