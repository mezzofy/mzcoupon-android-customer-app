package com.mezzofy.mzcoupon.Adapter;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.Activity.ImageDetailActivity;


public class SpecialDetailAdapter extends PagerAdapter {

        Context MyContext;
        CampaignmEntity albumList;
        String prodid;
        int i;

        public SpecialDetailAdapter(Context _MyContext, CampaignmEntity _albumList, String prod_id) {
            albumList=null;
            notifyDataSetChanged();
            MyContext = _MyContext;
            albumList=_albumList;
            prodid = prod_id;
        }

        @Override
        public int getCount() {
            if(albumList.getCampaign().getCampaignimages()!=null)
            return albumList.getCampaign().getCampaignimages().size();
            else
                return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            i=position;

            ImageView imageView = new ImageView(MyContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MyContext,ImageDetailActivity.class);
                    intent.putExtra("position",i);
                    intent.putExtra("prodid",prodid);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyContext.startActivity(intent);

                }
            });

            int loader = R.drawable.no_image_icon;

            Glide.with(MyContext)
                    .load(albumList.getCampaign().getCampaignimages().get(i).getCampaignimage().getCampaignImage())
                    .error(R.drawable.appicon)
                    .into(imageView);
            Glide.get(MyContext).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);

//            imgLoader.DisplayImage(albumList.getProdImageList().get(i).getProductImage(), loader, imageView);


            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }