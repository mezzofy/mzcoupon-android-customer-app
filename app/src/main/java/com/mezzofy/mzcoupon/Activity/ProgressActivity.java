package com.mezzofy.mzcoupon.Activity;

import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.mezzofy.MzCouponAPI.module.MZCampaign;
import com.mezzofy.mzcoupon.Entity.CampGrpEntity;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.Entity.MerchantEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CampaignGroupmEntity;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.Entity.CustomerGroupmEntity;
import com.mezzofy.mzcoupon.Entity.PaymentDetailmEntity;
import com.mezzofy.mzcoupon.Entity.PoListmEntity;
import com.mezzofy.mzcoupon.Entity.SitemEnity;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.mezzofy.mzcoupon.module.Coupon_Module;
import com.mezzofy.mzcoupon.module.CouponOrder_Module;

import com.mezzofy.mzcoupon.module.Merchant_Module;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;
import com.mezzofy.mzcoupon.module.Payment_Module;
import com.mezzofy.mzcoupon.module.PoOrder_Module;
import com.mezzofy.mzcoupon.module.Customer_Module;


public class ProgressActivity extends Activity {

    final Context context = this;

    private ProgressBar progress;

    private Coupon_Module coupon_module;

    private Customer_Module userModule;


    MZCampaign apicampaignModule;

    private PoListmEntity orderlist;

    private PoOrder_Module orderModule;

    //campaigns
    private Campaign_Module campaignModule;
    private List<CampaignmEntity> campaignModelList,toptenModellist;
    private List<CampaignGroupmEntity> campaignGroupModels;
    private List<CampGrpEntity> campGrpDataList;

    private List<SitemEnity> siteModelslist;

//    private List<com.mezzofy.couponapi.data.CampaignDataModel>toptenModellist;

    JSONObject jsonobj = null;

    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            setProgressBarIndeterminateVisibility(true);
            setContentView(R.layout.progress_layout);


            userModule = new Customer_Module(ProgressActivity.this);

            campaignModule=new Campaign_Module(context);
            orderModule=new PoOrder_Module(context);
            coupon_module=new Coupon_Module(context);

            apicampaignModule=new MZCampaign(context);



            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            progress = (ProgressBar) findViewById(R.id.progress1);
//        progress.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FFEB3B"), android.graphics.PorterDuff.Mode.MULTIPLY);

            AsyncCallWS task = new AsyncCallWS();
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class AsyncCallWS extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            CustomerEntity user = null;
            try {
                user = userModule.getUser();
            } catch (Exception e) {
                e.printStackTrace();
            }
//
//            //CampaignDetail
//            campaignModelList = campaignModule.getCampaigns(1);
//            if (campaignModelList != null) {
//                try {
//                    campaignModule.addCampaignlist(campaignModelList,1);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }

            //ToptenCampaignDetail

            toptenModellist = campaignModule.gettopcoupon();
            if (toptenModellist != null) {
                try {
                     campaignModule.addToptenCampaignlist(toptenModellist,1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //CampaignGroup
            campaignGroupModels=campaignModule.getCampaignsGroup();

            if(campaignGroupModels!=null)
            {
                try {
                    campaignModule.addCampaignGroup(campaignGroupModels);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //CustomerGroup
            if(user!=null && user.getCustomerGroupId()!=null) {
                Log.d("getCustomerGroupId",String.valueOf(user.getCustomerGroupId()));
                CustomerGroupmEntity customerGroupModel = userModule.getcustomergroup(user.getCustomerGroupId());
                if(customerGroupModel!=null)
                {
                    try {
                        userModule.addCustomerGrp(customerGroupModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                   }
                }
            }

            //Order
            if(user!=null && user.getCustomerId()!=null) {
                String customerid=user.getCustomerId();
                orderlist = orderModule.getOrderAPI(customerid,1);
                if (orderlist != null) {
                    try {
                        orderModule.addOrderdetail(orderlist);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //SiteDetail
            Merchantsite_Module merchantsiteModule = new Merchantsite_Module(getApplicationContext());
            siteModelslist = merchantsiteModule.getMerchantsites();
            if(siteModelslist!=null)
            {
                try {
                    merchantsiteModule.addMerchantSiteList(siteModelslist);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //MZMerchant
            Merchant_Module merchantModule=new Merchant_Module(getApplicationContext());
            String merchantid=Applaunch.MerchantId;
            if(merchantid!=null) {
                MerchantEntity merchantData = merchantModule.getMerchantfromserver(merchantid);
                if (merchantData != null) {
                    try {
                        merchantModule.addMerchant(merchantData);
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("currency",merchantData.getCurrency());
                        editor.commit();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            //CouponOrderlist
            CouponOrder_Module couponOrderModule=new CouponOrder_Module(getApplicationContext());
            try {
                couponOrderModule.DeleteInActiveRecord();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(user!=null && user.getCustomerId()!=null) {
                boolean res = couponOrderModule.getCouponOrderAPI(user.getCustomerId(), 1);
            }



            //payment detail
            Payment_Module paymentModule=new Payment_Module(getApplicationContext());
            List<PaymentDetailmEntity> paymentDetailModels = paymentModule.getPaymentDetailAPI();
            if (paymentDetailModels != null) {
                try {
                    paymentModule.addPaymentdetail(paymentDetailModels);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ///Delete Inactive record
            Coupon_Module coupon_module=new Coupon_Module(getApplicationContext());
            try {
                coupon_module.DeleteInActiveRecord();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Campaign_Module campaign_module=new Campaign_Module(getApplicationContext());
            try {
                campaign_module.DeleteInActiveRecord();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();
            progress.setVisibility(View.GONE);

            Intent it = new Intent(getApplicationContext(), TabViewActivtiy.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
            ProgressActivity.this.finish();
            overridePendingTransition(0, 0);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }

}