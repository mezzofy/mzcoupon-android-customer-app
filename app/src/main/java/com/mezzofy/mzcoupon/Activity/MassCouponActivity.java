package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mezzofy.MzCouponAPI.utills.APIServerException;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.Entity.MasscouponEntity;
import com.mezzofy.mzcoupon.Entity.MasscouponDetailEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.MassCouponDetailmEntity;
import com.mezzofy.mzcoupon.Entity.MassCouponmEntity;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.MassRedeem_Module;
import com.mezzofy.mzcoupon.apputills.CommonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by udhayinforios on 21/1/16.
 */
public class MassCouponActivity extends Activity {

    ListView list;
    List<CampaignEntity> CampaignList = null;

    private Campaign_Module campaignModule;
    private MassRedeem_Module massRedeemModule;

    int offset = 0, totalCount = 0, count = 0, listsize;
    int size = 10;
    String staffId,siteId;
    int prodid;

    int qtycount=0;

    private PullToRefreshListView couponPullRefreshListView;
    JSONObject jsonobj = null;

    List<CampaignEntity> albumList;

    Typeface regular;

    private ProgressBar progress;
    private ProgressBar progress_detail;

    String flag = "pulldown";
    String status, exp;
    SharedPreferences settings;

    int i = 0;

    RelativeLayout layout;
    CommonUtils common;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merc_coupon_page);

        try {
            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            staffId = settings.getString("staff_id", null);

            campaignModule = new Campaign_Module(getApplicationContext());
            massRedeemModule=new MassRedeem_Module(getApplicationContext());

            common = new CommonUtils();
            layout = (RelativeLayout) findViewById(R.id.signin_page);

            Bundle extras = getIntent().getExtras();
            siteId = extras.getString("site_id");


            try {
                CampaignList = campaignModule.getCampaignSizeList();
            } catch (Exception e) {
                e.printStackTrace();
            }


            couponPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listViewcoup);
            list = couponPullRefreshListView.getRefreshableView();
            list.setSelector(R.drawable.listselector);
            if (CampaignList.size() > 0)
                list.setAdapter(new ImageBaseAdapter(getApplicationContext(), CampaignList));

            TextView next = (TextView) findViewById(R.id.nxtTitle);
            next.setVisibility(View.VISIBLE);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MassCouponmEntity massCouponModel=new MassCouponmEntity();

                   ArrayList<MassCouponDetailmEntity> masscouponDetailDatasList=new ArrayList<MassCouponDetailmEntity>();

                    for(CampaignEntity campaignData:CampaignList)
                    {
                        if(campaignData.getQtycount()!=null && campaignData.getQtycount()>0)
                        {
                            MasscouponDetailEntity masscouponDetailData=new MasscouponDetailEntity();
                            MassCouponDetailmEntity massCouponDetailModel=new MassCouponDetailmEntity();

                            masscouponDetailData.setCampaignId(campaignData.getCampaignId());
                            masscouponDetailData.setQty(campaignData.getQtycount());

                            massCouponDetailModel.setMasscoupondetail(masscouponDetailData);

                            masscouponDetailDatasList.add(massCouponDetailModel);
                        }
                    }

                    if(masscouponDetailDatasList!=null && masscouponDetailDatasList.size()>0) {

                        MasscouponEntity masscouponData = new MasscouponEntity();
                        masscouponData.setSiteId(siteId);
                        masscouponData.setCustomerId(staffId);


                        massCouponModel.setMasscoupon(masscouponData);
                        massCouponModel.setMasscoupondtls(masscouponDetailDatasList);

                        MassCouponmEntity ret= null;
                        try {
                            ret = massRedeemModule.postMassRedeem(massCouponModel);
                        } catch (APIServerException e) {
                            common.Snackbar(layout, e.getMessage());
                        }
                        if(ret!=null && ret.getMasscoupon()!=null && ret.getMasscoupon().getReferenceNo()!=null)
                        {
                            Intent intent = new Intent(getApplicationContext(), QrCodewalnetActivity.class);
                            intent.putExtra("ref_no", ret.getMasscoupon().getReferenceNo());
                            intent.putExtra("masscouponstatus","Mass MZCoupon");
                            intent.putExtra("site_id",siteId);
                            startActivity(intent);
                        }
                        else {
                            common.Snackbar(layout, "Can not Redeem Error in Server");
//                            common.Snackbar(layout, Applaunch.ErrorMessage);
                        }

                    } else
                        common.Snackbar(layout, getString(R.string.Qty_alert));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class ImageBaseAdapter extends BaseAdapter {
        Context MyContext;

        int pos = 0;
        Boolean favclk = false;

        public ImageBaseAdapter(Context _MyContext, List<CampaignEntity> _albumList) {
            albumList = null;
            notifyDataSetChanged();
            MyContext = _MyContext;
            albumList = _albumList;
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
                MyView = li.inflate(R.layout.massredem_coupon_adapter, null);
            } else {
                MyView = convertView;
            }
            ImageView iv = (ImageView) MyView.findViewById(R.id.imageView1);
            TextView bustext = (TextView) MyView.findViewById(R.id.textView1);
            TextView pricetext = (TextView) MyView.findViewById(R.id.textView2);
            TextView datetext = (TextView) MyView.findViewById(R.id.textView3);
            final TextView addcoupon = (TextView) MyView.findViewById(R.id.add);
            final TextView cancelcoupon = (TextView) MyView.findViewById(R.id.cancel);
            final TextView couponvalue = (TextView) MyView.findViewById(R.id.qty);

            bustext.setTypeface(regular, Typeface.BOLD);
            pricetext.setTypeface(regular, Typeface.BOLD);
            datetext.setTypeface(regular);

            if (albumList.get(position).getCampaignimages() != null && albumList.get(position).getCampaignimages().get(0).getCampaignimage()!=null) {

                try {
                    Glide.with(MyContext)
                            .load(albumList.get(position).getCampaignimages().get(0).getCampaignimage().getCampaignImage())
                            .error(R.drawable.appicon)
                            .into(iv);
                    Glide.get(MyContext).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                iv.setImageResource(R.drawable.no_image_icon);
            }

            bustext.setText(albumList.get(position).getCampaignName());
            pricetext.setText(String.valueOf(albumList.get(position).getSize()));
            datetext.setText(albumList.get(position).getCampaignDesc());
            datetext.setMaxLines(3);
            datetext.setEllipsize(TextUtils.TruncateAt.END);

            addcoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Integer.valueOf(albumList.get(position).getSize()) > qtycount) {
                        qtycount++;
                        albumList.get(position).setQtycount(qtycount);
                        couponvalue.setText(String.valueOf(getResources().getString(R.string.qty) + "  " + albumList.get(position).getQtycount()));
                    }

                }
            });

            cancelcoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(qtycount>0) {
                        qtycount--;
                        albumList.get(position).setQtycount(qtycount);
                            couponvalue.setText(String.valueOf(getResources().getString(R.string.qty) + "  " + albumList.get(position).getQtycount()));
                    }
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
    }
}
