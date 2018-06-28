package com.mezzofy.mzcoupon.Adapter;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.R;

public class ImagePagerAdapter extends PagerAdapter {

		Context MyContext;
	CampaignEntity albumList;
	    int i;

		public ImagePagerAdapter(Context _MyContext,CampaignEntity _albumList) {
			albumList=null;
			notifyDataSetChanged();
			MyContext = _MyContext;
			albumList=_albumList;
		}
		
		@Override
		public int getCount() {
			if(albumList.getCampaignimages()!=null)
				return albumList.getCampaignimages().size();
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

			Glide.with(MyContext)
					.load(albumList.getCampaignimages().get(i).getCampaignimage().getCampaignImage())
					.error(R.drawable.appicon_large)
					.into(imageView);
			Glide.get(MyContext).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);

//			imgLoader.DisplayImage(albumList.getProdImageList().get(i).getProductImage(), loader, imageView);


			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}