package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.apputills.TouchImageView;

/**
 * Created by udhayinforios on 15/10/15.
 */
public class ImageDetailActivity extends Activity {

    final Context context = this;

    private GalleryViewPagerAdapter adapter;

    private ViewPager viewPager;
    private Campaign_Module campaignModule;

    CampaignmEntity productRes;

    String CityName, title;


    String prodid;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_image_detail);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                prodid = extras.getString("prodid");
                position = extras.getInt("position", 1);
            }

            viewPager = (ViewPager) findViewById(R.id.pager);

            campaignModule = new Campaign_Module(context);
            productRes = campaignModule.getCampaign(prodid);

            adapter = new GalleryViewPagerAdapter(ImageDetailActivity.this, productRes);
            viewPager.setAdapter(adapter);

            // displaying selected image first
            viewPager.setCurrentItem(position - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class GalleryViewPagerAdapter extends PagerAdapter {

        private Activity _activity;
        private LayoutInflater inflater;

        CampaignmEntity albumList;
        Context context;

        // constructor
        public GalleryViewPagerAdapter(Activity activity, CampaignmEntity _albumList) {

            this._activity = activity;
            albumList = _albumList;
        }

        @Override
        public int getCount() {
            return this.albumList.getCampaign().getCampaignimages().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TouchImageView imgDisplay;

            inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewLayout = inflater.inflate(R.layout.gallery_image_viewer, container, false);
            imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            int loader = 0;

            Glide.with(ImageDetailActivity.this)
                    .load(albumList.getCampaign().getCampaignimages().get(position).getCampaignimage().getCampaignImage())
                    .error(R.drawable.appicon)
                    .into(imgDisplay);
            Glide.get(context).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);


//            imgLoader.DisplayImage(albumList.getProdImageList().get(position).getProductImage(), loader, imgDisplay);

            TextView photocount = (TextView) viewLayout.findViewById(R.id.photo);
            photocount.setText((position + 1) + " / " + albumList.getCampaign().getCampaignimages().size());
            photocount.setMaxLines(1);
            photocount.setEllipsize(TextUtils.TruncateAt.END);

            ((ViewPager) container).addView(viewLayout);

            return viewLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

    }
}
