package com.mezzofy.mzcoupon.Adapter;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.PaymentDetailEntity;
import com.mezzofy.mzcoupon.Entity.WalletEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.module.Cart_Module;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.module.Wallet_Module;

import java.util.List;

/**
 * Created by udhayinforios on 24/11/15.
 */
public class MyScanAdapter extends BaseAdapter {

    Context MyContext;
    List<PaymentDetailEntity> albumList;
    int pos = 0;
    LayoutInflater li;
    Cart_Module cartModule;
    WalletEntity walletData;
    SharedPreferences settings;
      Customer_Module customerModule;
    Wallet_Module walletModule;
    public MyScanAdapter(Context _MyContext, List<PaymentDetailEntity> _albumList) {
        albumList = null;
        notifyDataSetChanged();
        MyContext = _MyContext;
        albumList = _albumList;
        li = (LayoutInflater) _MyContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cartModule = new Cart_Module(_MyContext);
        settings = PreferenceManager.getDefaultSharedPreferences(MyContext);
        walletModule = new Wallet_Module(MyContext);
        customerModule = new Customer_Module(_MyContext);
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View MyView;
        pos = position;
        if (convertView == null) {
            MyView = li.inflate(R.layout.list_cards_pay_detail, null);
        } else {
            MyView = convertView;
        }

        ImageView imgicon = (ImageView) MyView.findViewById(R.id.item_entry_drawable);
        ImageView imgicon2 = (ImageView) MyView.findViewById(R.id.item_entry_drawable2);
        imgicon2.setVisibility(View.GONE);
        TextView title = (TextView) MyView.findViewById(R.id.list_item_entry_title);
        TextView amt = (TextView) MyView.findViewById(R.id.amount);

        CustomerEntity ret = null;
        try {
            ret = customerModule.getUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            walletData = walletModule.getwalletdetail(ret.getCustomerId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (albumList.get(position).getPaymentLogourl() != null && !albumList.get(position).getPaymentLogourl().equals("")) {
            try {
                Glide.with(MyContext)
                        .load(albumList.get(position).getPaymentLogourl())
                        .error(R.drawable.appicon_large)
                        .into(imgicon);Glide.get(MyContext).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);

//                imgLoader.DisplayImage(albumList.get(position).getPaymentLogo(), loader, imgicon);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            imgicon.setImageResource(R.drawable.no_image_icon);
        }

        if (albumList.get(position).getPaymentName().equals("RedDot")) {
            title.setText("Credit Card");
        } else {
            title.setText(albumList.get(position).getPaymentName());
        }

        if (albumList.get(position).getPaymentName().equals("Promerce Dollar")) {

            if (walletData != null && Double.parseDouble(walletData.getWalletCredit()) != 0.00)
                amt.setText(albumList.get(position).getCurrency() + String.format("%,.2f", walletData.getWalletCredit()));
        } else {
            amt.setVisibility(View.GONE);
        }

        return MyView;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}

