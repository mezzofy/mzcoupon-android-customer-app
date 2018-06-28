package com.mezzofy.mzcoupon.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.MerchantEntity;
import com.mezzofy.mzcoupon.Entity.WalletEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.WalletmEntity;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.module.Merchant_Module;
import com.mezzofy.mzcoupon.module.Wallet_Module;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Wallet_Activity extends Fragment implements View.OnClickListener {


    private Wallet_Module walletModule;
    private WalletmEntity walletModel;
    private Merchant_Module merchantModule;
    private MerchantEntity merchantData;
    private Customer_Module customerModule;
    private CustomerEntity customerData;
    String custid,Currency;
    String custname;
    private boolean doubleBackToExitPressedOnce = false;
    WalletEntity wallet;
    TextView account, date;
    TextView name;
    View view;

    RelativeLayout wallet_layout, transaction, pending_trans, coupon, notification, lucky_draw;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.wallet_bal, null);
        try {
            walletModule=new Wallet_Module(getActivity());
            merchantModule=new Merchant_Module(getActivity());
            customerModule=new Customer_Module(getActivity());

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
            custid = settings.getString("staff_id", null);
            custname = settings.getString("user_name", "");
            Currency=settings.getString("currency",null);

            walletModel= walletModule.getWalletlistfromserver(custid);
            if (walletModel != null)
                walletModule.addWallet(walletModel.getWallet());

            wallet = walletModule.getwalletdetail(custid);

            merchantData=merchantModule.getMerchantList();
            customerData=customerModule.getUser();

            inititalView();

            name.setText(getString(R.string.wel_come) + " " + custname);


            if (wallet != null) {
                account.setText(merchantData.getCurrency() + wallet.getWalletCredit());

                try {
                    Date sx = new SimpleDateFormat("yyyyMMddhhmm").parse(wallet.getUpdatedOn());
                    date.setText(getString(R.string.lastupdate) + " " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(sx).toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public void inititalView() {

        wallet_layout = (RelativeLayout) view.findViewById(R.id.wallet_layout);
        wallet_layout.setOnClickListener(this);
        transaction = (RelativeLayout) view.findViewById(R.id.transaction_layout);
        transaction.setOnClickListener(this);
        coupon = (RelativeLayout) view.findViewById(R.id.coupon_layout);
        coupon.setOnClickListener(this);
        lucky_draw = (RelativeLayout) view.findViewById(R.id.lucky_layout);
        lucky_draw.setOnClickListener(this);
        pending_trans = (RelativeLayout) view.findViewById(R.id.pending_layout);
        pending_trans.setOnClickListener(this);
        notification = (RelativeLayout) view.findViewById(R.id.notifi_layout);
        notification.setOnClickListener(this);

        name = (TextView) view.findViewById(R.id.textView24);
        account = (TextView) view.findViewById(R.id.textView25);
        date = (TextView) view.findViewById(R.id.textView26);

        TextView topup = (TextView) view.findViewById(R.id.topup);
        topup.setOnClickListener(this);
        ImageView setting = (ImageView) view.findViewById(R.id.settings);
        setting.setOnClickListener(this);
        ImageView refresh = (ImageView) view.findViewById(R.id.imageView14);
        refresh.setOnClickListener(this);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.lineView1);
        linearLayout.setOnClickListener(this);

        if(wallet!=null) {
            if(Double.parseDouble(wallet.getWalletCredit())>0)
                account.setText(Currency+"  " + wallet.getWalletCredit());
            else
                account.setText(Currency+"  " + "0.0");

            long timestamp = Long.parseLong(wallet.getUpdatedOn());
            Log.d("timestamp po",timestamp+"  - "+wallet.getUpdatedOn());

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            sdf.setTimeZone(TimeZone.getDefault());
            String formattedDate = sdf.format(timestamp);

            date.setText("Last Update On  "+formattedDate);
        }
        else
        {
            account.setText("");
            date.setText("");
        }
    }


    @Override
    public void onClick(View v) {

        Intent intent = null;
        switch (v.getId()) {
            case R.id.wallet_layout:
//                intent = new Intent(getActivity().getApplicationContext(), TranscationActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent);
                break;
            case R.id.transaction_layout:
                intent = new Intent(getActivity().getApplicationContext(), PaymentTranscationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.pending_layout:
                intent = new Intent(getActivity().getApplicationContext(), PendingTranscActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.lucky_layout:
//                intent = new Intent(getActivity().getApplicationContext(), LuckyDrawListActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent);
                break;
            case R.id.notifi_layout:
                intent = new Intent(getActivity().getApplicationContext(), NotificationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.coupon_layout:
                intent = new Intent(getActivity().getApplicationContext(), CouponCardsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.lineView1:
                intent = new Intent(getActivity().getApplicationContext(), QrCodeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case R.id.imageView14:
                intent = new Intent(getActivity().getApplicationContext(), TabViewActivtiy.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("currTab", 5);
                intent.putExtra("flag", "wallet");
                startActivity(intent);
                break;
            case R.id.topup:
                intent = new Intent(getActivity(), TopupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.settings:
                intent = new Intent(getActivity(), SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
        }
    }



}
