package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mezzofy.mzcoupon.Entity.MechantSiteEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.module.Merchantsite_Module;
import com.mezzofy.mzcoupon.pojo.CouponRes;
import com.mezzofy.mzcoupon.pojo.Merchant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by udhayinforios on 21/1/16.
 */
public class MerchantListActivtiy extends Activity {

        private ProgressBar progress;


    ArrayList<Merchant> merchantList = new ArrayList<>();
    ArrayList<Merchant> merchlist = new ArrayList<>();

    ArrayList<MechantSiteEntity> sitelist;

    int size =10;
    private Merchantsite_Module merchantsiteModule;

    List<CouponRes> transactioncouponList = new ArrayList<>();

    int staffId = 0;

    ListView listView = null;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.choose_site);

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            staffId = settings.getInt("staff_id", 0);


            merchantsiteModule = new Merchantsite_Module(getApplicationContext());


            TextView txttitle = (TextView) findViewById(R.id.txtTitle);
            txttitle.setText(getString(R.string.sel_merchant));

//            sitelist = siteDao.getSite();

            progress = (ProgressBar) findViewById(R.id.progress1);
            progress.getIndeterminateDrawable().setColorFilter(Color.parseColor("#2D80A2"), android.graphics.PorterDuff.Mode.MULTIPLY);

            listView = (ListView) findViewById(R.id.listView1);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    position = (int) id;
                    Merchant album = merchantList.get(position);

//                    sitelist = siteDao.getSitebyMerchantId(merchantList.get(position).getMerchantId());

                    Intent intent = new Intent(getApplicationContext(), SiteListActivity.class);
                    intent.putExtra("merch_id", merchantList.get(position).getMerchantId());
                    intent.putExtra("merch_name", merchantList.get(position).getMerchantName());
                    startActivity(intent);
                }
            });

            AsyncOnload onload = new AsyncOnload();
            onload.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class AsyncOnload extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            progress.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

//            try {
//                ArrayList<CouponRes> CoupList = merCouponDao.getCouponListW();
//                if (CoupList.size() != 0)
//                    transactioncouponList = couponModule.getCouponList(staffId, 0, CoupList.size(), "A");
//                else
//                    transactioncouponList = couponModule.getCouponList(staffId, 0, size, "A");
//
//                if (transactioncouponList != null) {
//                    int j = 0;
//                    //                if (flag.equals("pullup")) {
//                    merCouponDao.deactiveMercoup("A");
                    //                }
                    for (CouponRes transactioncoupon : transactioncouponList) {
//                        if (j != 0) {
//
//                            merCouponDao.activeMercoup(transactioncoupon.getProductId(), "A");
//
//                            if (transactioncoupon != null) {
//                                merCouponDao.addCoupon(transactioncoupon);
//                                ProductRes productRes = productModule.getProduct(transactioncoupon.getProductId(),latitude,longitude);
//                                if (productRes != null) {
//                                    ProductRes prodres = productDao.getProductRes2(transactioncoupon.getProductId());
//                                    if (prodres != null)
//                                        if (!productRes.getHashCode().equals(prodres.getItem_hashCode())) {
//                                            productDao.updateitem_hashcode(productRes.getProdId(), productRes.getHashCode());
//                                            ArrayList<GroupRes> Groupitemsdelete = productDao.getProductGroupList(transactioncoupon.getProductId());
//                                            for (GroupRes groupRes : Groupitemsdelete) {
//                                                List<ItemRes> itemRes = itemModule.getItemResList(groupRes.getGroupId());
//                                                if (itemRes != null)
//                                                    for (ItemRes res : itemRes) {
//                                                        itemModule.deleteModifier(res.getItemId());
//                                                    }
//                                                itemModule.deleteItem(groupRes.getGroupId());
//                                            }
//                                            productDao.deleteProductGroup(transactioncoupon.getProductId());
//                                            productModule.addProduct(productRes);
//                                            ArrayList<GroupRes> Groupitems = productDao.getProductGroupList(productRes.getProdId());
//                                            for (GroupRes groupRes : Groupitems) {
//                                                List<ItemRes> itemRes = itemModule.getItem(groupRes.getGroupId());
//                                                if (itemRes != null) {
//                                                    int k = 0;
//                                                    for (ItemRes res : itemRes) {
//                                                        if (k != 0) {
//                                                            itemModule.addItem(res);
//                                                        }
//                                                        k++;
//                                                    }
//                                                }
//                                            }
//                                        }
//                                }
//                            }
//                            ArrayList<SiteProduct> Productsite = productDao.getProductSiteList(transactioncoupon.getProductId());
//                            for (SiteProduct prodsite : Productsite) {
//                                SiteEntity mechantSite = merchantsiteModule.getMerchantSite(prodsite.getSiteId());
//                                if (mechantSite != null) {
//                                    siteDao.deleteMerch_deltime(mechantSite.getSiteId());
//                                    merchantsiteModule.addMerchantSite(mechantSite);
//                                    MZMerchant merchant = merchantModule.getMerchant(mechantSite.getMerchantId());
//                                    if (merchant != null)
//                                        merchantModule.addMerchant(merchant);
//                                }
//                            }
//                        }
//                        j++;
//                    }
//
//                    MerchantDao merchantDao = new MerchantDao(getApplicationContext());
//                    merchlist = merchantDao.getMerchantsformassRedeem();
//
//                    for (MZMerchant res : merchlist) {
//                        sitelist = siteDao.getSitelistformassredeem(res.getMerchantId());
//
//                        if (sitelist.size() > 0)
//                            merchantList.add(res);
//                    }
//                } else {
//                    merCouponDao.deactiveMercoup("A");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
                    }

            return null;
        }


        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();
            progress.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            if(merchantList != null && merchantList.size() > 0)
                listView.setAdapter(new NoteAdapter(getApplicationContext(), merchantList));
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }


    public class NoteAdapter extends BaseAdapter {

        Context MyContext;
        ArrayList<Merchant> albumList;
        int pos = 0;

        public NoteAdapter(Context _MyContext, ArrayList<Merchant> _albumList) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View MyView;
            pos = position;
            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                MyView = li.inflate(R.layout.merchant_list_adapter, null);
            } else {
                MyView = convertView;
            }

            TextView bustext = (TextView) MyView.findViewById(R.id.list_content);
            bustext.setText(albumList.get(position).getMerchantName());

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplication().getApplicationContext(), TabViewActivtiy.class);
        intent.putExtra("currTab", 1);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
        this.finish();
    }
}


