package com.mezzofy.mzcoupon.Adapter;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mezzofy.mzcoupon.Entity.CouponEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CustomerCouponmEntity;
import com.mezzofy.mzcoupon.pojo.CouponRes;

import java.util.ArrayList;
import java.util.List;

public class MerCouponRedeemAdapter extends BaseAdapter {
    Context MyContext;
    List<CustomerCouponmEntity> albumList;
    int pos = 0;
    CouponRes res;
    Typeface regular;

    public MerCouponRedeemAdapter(Context _MyContext, List<CustomerCouponmEntity> _albumList) {
        albumList = null;
        notifyDataSetChanged();
        MyContext = _MyContext;
        albumList = _albumList;
        regular = Typeface.createFromAsset(MyContext.getAssets(), "Roboto-Light.ttf");
//            res = couponDao.getCouponList(prodid, "R");
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
            LayoutInflater li = (LayoutInflater) MyContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            MyView = li.inflate(R.layout.couponlist_detail, null);
        } else {
            MyView = convertView;
        }
        ImageView iv = (ImageView) MyView.findViewById(R.id.imageView1);
        TextView bustext = (TextView) MyView.findViewById(R.id.textView1);
        TextView pricetext = (TextView) MyView.findViewById(R.id.textView2);
        TextView datetext = (TextView) MyView.findViewById(R.id.textView3);
        ImageView Nicon = (ImageView) MyView.findViewById(R.id.imageView2);
        TextView desc = (TextView) MyView.findViewById(R.id.textView14);

        bustext.setTypeface(regular, Typeface.BOLD);
        pricetext.setTypeface(regular, Typeface.BOLD);
        datetext.setTypeface(regular, Typeface.BOLD);
        desc.setTypeface(regular);


        CouponEntity couponData=albumList.get(position).getCoupon();

        if (couponData.getProductImageurl() != null && !couponData.getProductImageurl().equals("")) {
            try {

                Glide.with(MyContext)
                        .load(couponData.getProductImageurl())
                        .error(R.drawable.appicon_large)
                        .into(iv);Glide.get(MyContext).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);

//                imgLoader.DisplayImage(albumList.get(position).getProductImage(), loader, iv);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            iv.setImageResource(R.drawable.no_image_icon);
        }
        bustext.setText(couponData.getCouponName());
        desc.setText(couponData.getProductDesc());
        desc.setMaxLines(3);
        desc.setEllipsize(TextUtils.TruncateAt.END);

        datetext.setText(couponData.getEndDate());


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
