package com.mezzofy.mzcoupon.Adapter;

import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mezzofy.mzcoupon.Entity.CartEntity;
import com.mezzofy.mzcoupon.Entity.MerchantEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.Cart_Module;
import com.mezzofy.mzcoupon.module.Merchant_Module;
import com.mezzofy.mzcoupon.apputills.CommonUtils;
import com.mezzofy.mzcoupon.Activity.TabViewActivtiy;

import java.util.List;

public class CartBaseAdapter extends BaseAdapter {
    Context MyContext;
    List<CartEntity> albumList;
    int pos = 0;
    LayoutInflater li = null;
    SharedPreferences settings = null;
    Merchant_Module merchantModule = null;
   Campaign_Module campaignModule=null;
    Cart_Module cartModule=null;
    ImageView iv;
    double latitude;
    double longitude;

    public CartBaseAdapter(Context _MyContext, List<CartEntity> _albumList) {
        albumList = null;
        notifyDataSetChanged();
        MyContext = _MyContext;
        albumList = _albumList;
        li = (LayoutInflater) _MyContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        settings = PreferenceManager.getDefaultSharedPreferences(_MyContext);
        merchantModule = new Merchant_Module(_MyContext);
        campaignModule = new Campaign_Module(_MyContext);
        cartModule=new Cart_Module(_MyContext);

    }

    @Override
    public int getCount() {
        if(albumList!=null)
            return albumList.size();
        else
            return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View MyView;
        pos = position;
        if (convertView == null) {

            MyView = li.inflate(R.layout.cart_prod, null);
        } else {
            MyView = convertView;
        }

        iv = (ImageView) MyView.findViewById(R.id.imageView1);
        TextView bustext = (TextView) MyView.findViewById(R.id.textView1);
        TextView pricetext = (TextView) MyView.findViewById(R.id.textView2);
        TextView qtytext = (TextView) MyView.findViewById(R.id.textView3);
        ImageView remov = (ImageView) MyView.findViewById(R.id.imageViewRemove);
        TextView merName = (TextView) MyView.findViewById(R.id.textView23);


        MerchantEntity merchant = new MerchantEntity();
        if (albumList.get(position).getMerchantId() == null) {
//            productRes = campaignModule.get(albumList.get(position).getProdId(),latitude,longitude);
        } else {
            if(merchant!=null) {
                merchant = merchantModule.getMerchantfromserver(albumList.get(position).getMerchantId());
            }
        }
        if (albumList.get(position).getCampaignImage() != null) {
            try {
                // Loader image - will be shown before loading image

                Glide.with(MyContext)
                        .load(albumList.get(position).getCampaignImage())
                        .error(R.drawable.appicon_large)
                        .into(iv);
                Glide.get(MyContext).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);



//                imgLoader.DisplayImage(productRes.getProdImageList().get(0).getProductImage(), loader, iv);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            iv.setImageResource(R.drawable.no_image_icon);
        }
        if(merchant!=null)
          merName.setText(merchant.getMerchantName());

        bustext.setText(albumList.get(position).getCampaignName());
        bustext.setMaxLines(3);
        bustext.setEllipsize(TextUtils.TruncateAt.END);
        if (settings.getString("decimal", "N").equals("Y")) {
            pricetext.setText(" " + settings.getString("currency", "IDR") +  albumList.get(position).getTotalPrice() + " ");
        } else {
            pricetext.setText(" " + settings.getString("currency", "IDR") +  albumList.get(position).getTotalPrice() + " ");
        }
        qtytext.setText("X" + " " + String.valueOf(albumList.get(position).getProductQty()));
        remov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeListItem(position);
//                albumList.remove(position);
//                notifyDataSetChanged();
            }
        });

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

    protected void removeListItem(final int positon) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyContext);
        alertDialogBuilder.setMessage(R.string.Sure_Delete);
        alertDialogBuilder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                try {
                    cartModule.deletedchart(albumList.get(positon).getCampaignId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                albumList.remove(positon);

                CommonUtils.Snackbar(iv, MyContext.getString(R.string.Item_Deleted));

                int cout = 0;
                try {
                    cout = cartModule.getItemcart();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                settings = PreferenceManager.getDefaultSharedPreferences(MyContext);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("notif_count", cout);
                editor.commit();

                Intent intent = new Intent(MyContext, TabViewActivtiy.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("tabName", "");
                intent.putExtra("currTab", 3);
                MyContext.startActivity(intent);

            }
        });

        alertDialogBuilder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
