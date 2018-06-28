package com.mezzofy.mzcoupon.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mezzofy.mzcoupon.module.Merchant_Module;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;

public class DeepLinkActivity extends AppCompatActivity {


    private Merchant_Module merchantModule;
    private Merchantsite_Module merchantsiteModule;
    double latitude;
    double langitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {



            merchantModule = new Merchant_Module(getApplicationContext());
            merchantsiteModule = new Merchantsite_Module(getApplicationContext());

            Uri uri = this.getIntent().getData();
            if (uri == null)
                return;

            String prodid = uri.getQueryParameter("prodid");

            if (prodid == null)
                return;

            String coupid = uri.getQueryParameter("coupid");
            if (coupid == null)
                coupid = "0";

            String expdate = uri.getQueryParameter("expdate");
            if (expdate == null)
                expdate="0";

            AsyncCallWSDetail asyncCallWSDetail = new AsyncCallWSDetail();
            asyncCallWSDetail.execute(prodid,coupid,expdate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class AsyncCallWSDetail extends AsyncTask<String, Void, Void> {
        int prodid;
        String coupid;
        String expdate;

        @Override
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");

        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            Log.i("TAG", "doInBackground");
            prodid = Integer.parseInt(params[0]);
            coupid = params[1];
            expdate = params[2];

            if (coupid != null) {

                //ProductRes productRes = productModule.getProduct(prodid);
                //lavanya
//                ProductRes productRes = productModule.getProduct(prodid,latitude,langitude);
//                if (productRes != null) {
//                    ProductRes prodres = productDao.getProductRes2(productRes.getProdId());
//                    if (prodres != null)
//                        if (!productRes.getHashCode().equals(prodres.getItem_hashCode())) {
//                            productDao.updateitem_hashcode(productRes.getProdId(), productRes.getHashCode());
//                            ArrayList<GroupRes> Groupitemsdelete = productDao.getProductGroupList(prodid);
//                            for (GroupRes groupRes : Groupitemsdelete) {
//                                List<ItemRes> itemRes = itemModule.getItemResList(groupRes.getGroupId());
//                                if (itemRes != null)
//                                    for (ItemRes res : itemRes) {
//                                        itemModule.deleteModifier(res.getItemId());
//                                    }
//                                itemModule.deleteItem(groupRes.getGroupId());
//                            }
//                            productDao.deleteProductGroup(prodid);
//                            productModule.addProduct(productRes);
//                            ArrayList<GroupRes> Groupitems = productDao.getProductGroupList(productRes.getProdId());
//                            for (GroupRes groupRes : Groupitems) {
//                                List<ItemRes> itemRes = itemModule.getItem(groupRes.getGroupId());
//                                if (itemRes != null) {
//                                    int k = 0;
//                                    for (ItemRes res : itemRes) {
//                                        if (k != 0) {
//                                            itemModule.addItem(res);
//                                        }
//                                        k++;
//                                    }
//                                }
//                            }
//                        }
//                }

//                ArrayList<SiteProduct> Productsite = productDao.getProductSiteList(prodid);
//                for (SiteProduct prodsite : Productsite) {
//                    SiteEntity mechantSite = null;
//                    try {
//                        mechantSite = merchantsiteModule.getMerchantSite(prodsite.getSiteId());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    if (mechantSite != null) {
//                        merchant_SiteDao.deleteMerch_deltime(mechantSite.getSiteId());
//                        try {
//                            merchantsiteModule.addMerchantSite(mechantSite);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        MerchantEntity merchant = merchantModule.getMerchantfromserver(mechantSite.getMerchantId());
//                        if (merchant != null)
//                            try {
//                                merchantModule.addMerchant(merchant);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                    }
//                }
            }
            return null;
        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            Intent intent;
            if (coupid != null) {
                intent = new Intent(getApplicationContext(), CouponDetail_Activity.class);
                intent.putExtra("cupid", coupid);
                intent.putExtra("exp", "act");
                intent.putExtra("expday", expdate);
                intent.putExtra("flag", "send_gift");
            } else {
                intent = new Intent(getApplicationContext(), Campaigndetail_Activity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("prodid", prodid);
            startActivity(intent);
            DeepLinkActivity.this.finish();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }
}