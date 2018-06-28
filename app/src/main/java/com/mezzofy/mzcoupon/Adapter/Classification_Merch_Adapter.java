package com.mezzofy.mzcoupon.Adapter;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.pojo.Merchant;

import java.util.ArrayList;

public class Classification_Merch_Adapter extends BaseAdapter {
    Context MyContext;
    ArrayList<Merchant> albumList;
    int pos = 0;
    // ImageLoader class instance
    LayoutInflater li;

    public Classification_Merch_Adapter(Context _MyContext, ArrayList<Merchant> _albumList) {
        albumList = null;
        notifyDataSetChanged();
        MyContext = _MyContext;
        albumList = _albumList;
        li = (LayoutInflater) _MyContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            MyView = li.inflate(R.layout.class_merchant, null);
        } else {
            MyView = convertView;
        }
        ImageView iv = (ImageView) MyView.findViewById(R.id.imageView1);
        TextView merch_name = (TextView) MyView.findViewById(R.id.text1);
        TextView merch_desc = (TextView) MyView.findViewById(R.id.text2);

        if (albumList.get(position).getMerchantLogo() != null && !albumList.get(position).getMerchantLogo().equals("")) {
            try {
                // Loader image - will be shown before loading image

                Glide.with(MyContext)
                        .load(albumList.get(position).getMerchantLogo())
                        .error(R.drawable.appicon_large)
                        .into(iv);Glide.get(MyContext).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);


//                imgLoader.DisplayImage(albumList.get(position).getMerchantLogo(), loader, iv);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            iv.setImageResource(R.drawable.no_image_icon);
        }

        merch_name.setText(albumList.get(position).getMerchantName());
//                    merch_desc.setText(albumList.get(position).getMerchantDesc());
//                    merch_desc.setMaxLines(3);
//                    merch_desc.setEllipsize(TextUtils.TruncateAt.END);
//                datetext.setText(albumList.get(position).getSiteName());
//                if (albumList.get(position).getKm() != null) {
//                    pricetext.setText((albumList.get(position).getKm()) + "km");
//                } else {
//                    pricetext.setVisibility(View.GONE);
//                }
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
