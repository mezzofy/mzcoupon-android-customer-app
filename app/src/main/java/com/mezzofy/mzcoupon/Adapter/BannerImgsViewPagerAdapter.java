package com.mezzofy.mzcoupon.Adapter;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mezzofy.mzcoupon.R;


public class BannerImgsViewPagerAdapter extends PagerAdapter {

    private Context context;
    private int[] bannerImageList;

    public BannerImgsViewPagerAdapter(Context context, int[] bannerImageList) {
        this.context = context;
        this.bannerImageList = bannerImageList;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(context)
                .load(bannerImageList[position])
                .error(R.drawable.appicon_large)
                .into(imageView);
        Glide.get(context).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);

        //imageView.setImageResource(bannerImageList[position]);

        ((ViewPager) container).addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
