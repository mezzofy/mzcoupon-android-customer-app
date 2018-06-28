package com.mezzofy.mzcoupon.apputills;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Activity.Youtube_VideoPlayerActivity;

/**
 * Created by LENOVO on 30/06/2015.
 */
public class ImageVideoPagerAdapter extends PagerAdapter {

    Context MyContext;
    CampaignEntity albumList;
    LayoutInflater inflater;

    public ImageVideoPagerAdapter(Context _MyContext,CampaignEntity _albumList) {

        albumList=null;
        notifyDataSetChanged();
        MyContext = _MyContext;
        albumList=_albumList;
    }

    @Override
    public int getCount() {
        return albumList.getCampaignimages().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) MyContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.videoplay_page, container, false);

        ImageView imageView = (ImageView)itemView.findViewById(R.id.pager);
        ImageView playbtn = (ImageView)itemView.findViewById(R.id.playtbn);


        if(position==0){
            playbtn.setVisibility(View.VISIBLE);
            Glide.with(MyContext)
                    .load("http://img.youtube.com/vi/" + albumList.getVideoUrl() + "/0.jpg")
                    .error(R.drawable.appicon_large)
                    .into(imageView);

//            imgLoader.DisplayImage("http://img.youtube.com/vi/" + albumList.getVideoUrl() + "/0.jpg", loader, imageView);
            playbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Youtube_VideoPlayerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("videoid", albumList.getVideoUrl());
                    v.getContext().startActivity(intent);
                }
            });
        }else {
            Glide.with(MyContext)
                    .load(albumList.getCampaignimages().get(position).getCampaignimage().getCampaignImage())
                    .error(R.drawable.appicon_large)
                    .into(imageView);
//            imgLoader.DisplayImage(albumList.getProdImageList().get(position).getProductImage(), loader, imageView);
        }

        ((ViewPager) container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}