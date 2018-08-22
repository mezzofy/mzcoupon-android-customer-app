package com.mezzofy.mzcoupon.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.module.Campaign_Module;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.mezzofy.mzcoupon.Adapter.ProductBaseAdapter;

import com.mezzofy.mzcoupon.Activity.FavouriteActivity;
import com.mezzofy.mzcoupon.Activity.FilterActivity;
import com.mezzofy.mzcoupon.Activity.SpecialDetailActivity;
import com.mezzofy.mzcoupon.Activity.TabViewActivtiy;
import com.mezzofy.mzcoupon.pojo.CompanyProduct;

import com.mezzofy.mzcoupon.apputills.CommonUtils;
import com.mezzofy.mzcoupon.apputills.ListPulldownHelper;
import com.mezzofy.mzcoupon.apputills.ObjectSerializer;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment implements OnClickListener {

    JSONObject jsonobj = null;
    String flag = "pulldown";




    private Campaign_Module campaignModule;


    int listprod;
    int companyId;
    int offset = 0, totalCount = 0, count = 0, soffset,limit=0;
    int size = 10;

    private SearchView.OnQueryTextListener searchList;
    SharedPreferences settings;

    private ProgressBar progress;

    ArrayList<CampaignmEntity> prodList = null;
    private ArrayList<Integer> filterCateList = new ArrayList<Integer>();
    private ArrayList<Integer> filterLocList = new ArrayList<Integer>();
    private ArrayList<Integer> filterMerchList = new ArrayList<Integer>();
    private ArrayList<Integer> Filterresult = new ArrayList<>();
    ListView list;
    List<CompanyProduct> CompyURLList = null;

    TextView filterindicat, special, coupon;

    ListView specialList;
    private SwipyRefreshLayout mSwipyRefreshLayout;



    ArrayList<CampaignEntity> specList = null;
    Gson gson = new Gson();

    RelativeLayout layout;
    CommonUtils common;

    TabViewActivtiy getcontext;
    double latitude;
    double longitude;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof TabViewActivtiy) {
            getcontext = (TabViewActivtiy) context;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product, null);

        try {

            common = new CommonUtils();
            layout = (RelativeLayout) view.findViewById(R.id.signin_page);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            settings = PreferenceManager.getDefaultSharedPreferences(getcontext);
            companyId = settings.getInt("company_Id", 0);

            special = (TextView) view.findViewById(R.id.special);
           // coupon = (TextView) view.findViewById(R.id.coupon);


            campaignModule = new Campaign_Module(getcontext);

            TextView titletext = (TextView) view.findViewById(R.id.txtTitle);
            titletext.setText(getString(R.string.Bali_COUPON));

            progress = (ProgressBar) view.findViewById(R.id.progress1);

            specialList = (ListView) view.findViewById(R.id.speciallist);
            specialList.setSelector(R.drawable.listselector);
            specialList.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    position = (int) id;
                    CampaignEntity album = specList.get(position);

                    Intent intent = new Intent(getcontext, SpecialDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("tabName", getString(R.string.menu_Special));
                    intent.putExtra("currTab", 0);
                    startActivity(intent);
                }
            });


            try {
                prodList = campaignModule.getCampaignList();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
            list = (ListView) view.findViewById(R.id.couponlist);
            list.setSelector(R.drawable.listselector);
            list.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    position = (int) id;
//                    AsyncCallDetail task = new AsyncCallDetail();
//                    task.execute(position);
                }
            });

            ImageView imgfav = (ImageView) view.findViewById(R.id.imageView1);
            imgfav.setOnClickListener(this);

            ImageView imgfli = (ImageView) view.findViewById(R.id.imageViewflit2);
            imgfli.setOnClickListener(this);

            Bundle extras = getcontext.getIntent().getExtras();
            if (extras != null)
                listprod = extras.getInt("listprod");

            filterindicat = (TextView) view.findViewById(R.id.filterindicat);
            filterindicat.setOnClickListener(this);
            if (Filterresult.size() != 0) {
                filterindicat.setVisibility(View.VISIBLE);
            }

            try {
                filterCateList = (ArrayList) ObjectSerializer.deserialize(settings.getString("sendfilterCateList", ObjectSerializer.serialize(new ArrayList())));
                filterLocList = (ArrayList) ObjectSerializer.deserialize(settings.getString("sendfilterLocList", ObjectSerializer.serialize(new ArrayList())));
                filterMerchList = (ArrayList) ObjectSerializer.deserialize(settings.getString("sendfilterMerchList", ObjectSerializer.serialize(new ArrayList())));
            } catch (IOException e) {
                e.printStackTrace();
            }

            mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection direction) {

                    if (direction == SwipyRefreshLayoutDirection.TOP) {

                        flag = "pullup";

//                        if (filterCateList.size() != 0 && filterLocList.size() != 0 && filterMerchList.size() != 0) {
//                            if (filterCateList.get(0) != 0 || filterLocList.get(0) != 0 || filterMerchList.get(0) != 0) {
//
//                                if (Filterresult.size() != 0) {
//                                    Productfilter prodftr = setPojo(0, Filterresult.size());
//                                    CompyURLList = assignProdModule.getFilterAssignProd(prodftr);
//                                } else {
//                                    Productfilter prodftr = setPojo(0, size);
//                                    CompyURLList = assignProdModule.getFilterAssignProd(prodftr);
//                                }
//
//                                AsyncfilterCallWS asyncfilterCallWS = new AsyncfilterCallWS();
//                                asyncfilterCallWS.execute();
//
//                            } else {
//                                ArrayList<CampaignDataModel> pList = null;
//                                try {
//                                    pList = campaignModule.getCampaignList();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                if (pList.size() != 0)
//                                    CompyURLList = assignProdModule.getAssignProd(companyId, 0, pList.size());
//                                else
//                                    CompyURLList = assignProdModule.getAssignProd(companyId, 0, size);
//
//                                AsyncCallWS task = new AsyncCallWS();
//                                task.execute();
//                            }
//                        } else {
//                            ArrayList<CampaignDataModel> pList = null;
//                            try {
//                                pList = campaignModule.getCampaignList();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            if (pList.size() != 0)
//                                CompyURLList = assignProdModule.getAssignProd(companyId, 0, pList.size());
//                            else
//                                CompyURLList = assignProdModule.getAssignProd(companyId, 0, size);
//
////                            AsyncCallWS task = new AsyncCallWS();
////                            task.execute();
//                        }
//                    } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
//                        if (filterCateList.size() != 0 && filterLocList.size() != 0 && filterMerchList.size() != 0) {
//                            if (filterCateList.get(0) != 0 || filterLocList.get(0) != 0 || filterMerchList.get(0) != 0) {
//
//                                soffset = (soffset / 10) * 10;
//                                Productfilter samp = setPojo(0, size);
//                                String res = assignProdModule.getFilterStringProd(samp);
//
//                                ProductServer server = gson.fromJson(res, ProductServer.class);
//
//                                if (server != null && server.getResponse() != null && server.getResponse().getResponse_code().equals("0")) {
//                                    totalCount = Integer.valueOf(String.valueOf(server.getResponse().getTotal()));
//                                    count = server.getResponse().getSize();
//                                }
//
//                                if (soffset < totalCount) {
//                                    soffset = soffset + count;
//                                }
//
//                                Productfilter prodftr = setPojo(soffset, size);
//
//                                CompyURLList = assignProdModule.getFilterAssignProd(prodftr);
//
//                                flag = "pulldown";
//
////                                AsyncfilterCallWS asyncfilterCallWS = new AsyncfilterCallWS();
////                                asyncfilterCallWS.execute();
//
//                            } else {
//                                offset = (offset / 10) * 10;
//                                String res = JSONSTRINGS.getJSONFromUrl(CommonModule.getUrlpath6() + "assign_product/" + companyId + "?offset=" + offset + "&size=" + size);
//                                ProductServer server = gson.fromJson(res, ProductServer.class);
//
//                                if (server != null && server.getResponse() != null && server.getResponse().getResponse_code().equals("0")) {
//                                    totalCount = Integer.valueOf(String.valueOf(server.getResponse().getTotal()));
//                                    count = server.getResponse().getSize();
//                                }
//
//                                if (offset < totalCount) {
//                                    offset = offset + count;
//                                    CompyURLList = assignProdModule.getAssignProd(companyId, offset, size);
//
//                                    flag = "pulldown";
//
////                                    AsyncCallWS task = new AsyncCallWS();
////                                    task.execute();
//                                }
//                            }
//                        } else {
//                            offset = (offset / 10) * 10;
//                            String res = JSONSTRINGS.getJSONFromUrl(CommonModule.getUrlpath6() + "assign_product/" + companyId + "?offset=" + offset + "&size=" + size);
//                            ProductServer server = gson.fromJson(res, ProductServer.class);
//
//                            if (server != null && server.getResponse() != null && server.getResponse().getResponse_code().equals("0")) {
//                                totalCount = Integer.valueOf(String.valueOf(server.getResponse().getTotal()));
//                                count = server.getResponse().getSize();
//                            }
//
//                            if (offset < totalCount) {
//                                offset = offset + count;
//                                CompyURLList = assignProdModule.getAssignProd(companyId, offset, size);
//
//                                flag = "pulldown";
//
////                                AsyncCallWS task = new AsyncCallWS();
////                                task.execute();
//                            }
//                        }
                    }
                }
            });

            final SearchView mySearchView = (SearchView) view.findViewById(R.id.searchly);
            searchList = new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    // TODO Auto-generated method stub
                    Log.d("input", query);

//                    AsyncOnSearch task = new AsyncOnSearch();
//                    task.execute(query);

                    mySearchView.clearFocus();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // TODO Auto-generated method stub
                    Log.d("inputkey", newText);

                    return false;
                }
            };

            SearchView.OnCloseListener onCloseListener = new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    prodList = null;
//                    if (filterCateList.size() != 0 && filterLocList.size() != 0 && filterMerchList.size() != 0) {
//                        if (filterCateList.get(0) != 0 || filterLocList.get(0) != 0 || filterMerchList.get(0) != 0) {
//
//                            prodList = productDao.getProductList(Filterresult);
//                            list.setAdapter(new ProductBaseAdapter(getcontext, prodList,"P"));
//                            ListPulldownHelper.getListViewSize(list);
//                        } else {
//                            prodList = productDao.getProductList();
//                            list.setAdapter(new ProductBaseAdapter(getcontext, prodList,"P"));
//                            ListPulldownHelper.getListViewSize(list);
//                        }
//                    } else {
//                        prodList = productDao.getProductList();
//                        list.setAdapter(new ProductBaseAdapter(getcontext, prodList,"P"));
//                        ListPulldownHelper.getListViewSize(list);
//                    }
                    return false;
                }
            };

            mySearchView.setOnQueryTextListener(searchList);
            mySearchView.setOnCloseListener(onCloseListener);
            mySearchView.setSubmitButtonEnabled(true);
            //mySearchView.setIconifiedByDefault(false);

//            if (filterCateList.size() != 0 && filterLocList.size() != 0 && filterMerchList.size() != 0) {
//                if (filterCateList.get(0) != 0 || filterLocList.get(0) != 0 || filterMerchList.get(0) != 0) {
//
//                    Productfilter prodftr = setPojo(0, size);
//                    CompyURLList = assignProdModule.getFilterAssignProd(prodftr);
//
//                    AsyncfilterCallWS asyncfilterCallWS = new AsyncfilterCallWS();
//                    asyncfilterCallWS.execute();
//                } else {
//                    AsyncOnload task = new AsyncOnload();
//                    task.execute();
//                }
//            } else {
//                AsyncOnload task = new AsyncOnload();
//                task.execute();
//            }
//
            Asyncstart onstart = new Asyncstart();
            onstart.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.imageView1:
                intent = new Intent(getcontext, FavouriteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("prodfav", "prodfav");
                startActivity(intent);
                break;

            case R.id.imageViewflit2:
                filteractivity();
                break;

            case R.id.filterindicat:
                filteractivity();
                break;
            default:
                break;
        }
    }


    public void filteractivity() {
        SharedPreferences.Editor editor = settings.edit();
        try {
            editor.putString("filterCategry", ObjectSerializer.serialize(filterCateList));
            editor.putString("filterlocList", ObjectSerializer.serialize(filterLocList));
            editor.putString("filterMerchList", ObjectSerializer.serialize(filterMerchList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();

        Intent intent = new Intent(getcontext, FilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public class Asyncstart extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            list.setClickable(false);
            list.setEnabled(false);
            specialList.setClickable(false);
            specialList.setEnabled(false);
            special.setVisibility(View.GONE);
           // coupon.setVisibility(View.GONE);
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

//            specList = specialDao.getSpecialResList();

            return null;
        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();

            list.setClickable(true);
            list.setEnabled(true);
            specialList.setClickable(true);
            specialList.setEnabled(true);
            special.setVisibility(View.VISIBLE);
            coupon.setVisibility(View.VISIBLE);

            if (prodList.size() > 0) {
                list.setAdapter(new ProductBaseAdapter(getcontext, prodList,"P"));
                ListPulldownHelper.getListViewSize(list);
            }
//            if (specList.size() > 0) {
//                specialList.setAdapter(new SpecialAdapter(getcontext, specList));
//                ListPulldownHelper.getListViewSize(specialList);
//            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }


//    public class AsyncCallWS extends AsyncTask<String, Void, Void> {
//
//        @Override
//        //Make Progress Bar visible
//        protected synchronized void onPreExecute() {
//            Log.i("TAG", "onPostExecute");
//            list.setClickable(false);
//            list.setEnabled(false);
//        }
//
//        @Override
//        protected synchronized Void doInBackground(String... params) {
//            // TODO Auto-generated method stub
//            Log.i("TAG", "doInBackground");
//
//            if (CompyURLList != null) {
//                int j = 0;
//                if (flag.equals("pullup")) {
//                    productDao.deactiveAssign();
//                }
//                for (CompanyProduct compyurl : CompyURLList) {
//                    if (j != 0) {
//                        if (flag.equals("pullup")) {
//                            productDao.updateAssign(compyurl.getProdId());
//                        }
//                        ProductRes productRes = productModule.getProduct(compyurl.getProdId(),latitude,longitude);
//                        if (productRes != null) {
//                            ProductRes prodres = productDao.getProductRes(productRes.getProdId());
//                            if (prodres != null) {
//                                if (!productRes.getHashCode().equals(prodres.getHashCode())) {
//                                    productModule.addProduct(productRes);
//                                }
//                            } else {
//
//                                productModule.addProduct(productRes);
//
//                                ArrayList<SiteProduct> Productsite = productDao.getProductSiteList(productRes.getProdId());
//                                for (SiteProduct prodsite : Productsite) {
//                                    MechantSite mechantSite = merchant_siteModule.getMerchant_site(prodsite.getSiteId());
//                                    if (mechantSite != null) {
//                                        merchant_SiteDao.deleteMerch_deltime(mechantSite.getSiteId());
//                                        merchant_siteModule.addMerchant_site(mechantSite);
//
//                                        MZMerchant merchant = merchantModule.getMerchant(mechantSite.getMerchantId());
//                                        if (merchant != null)
//                                            merchantModule.addMerchant(merchant);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    j++;
//                }
//            }
//
//
//            List<CategoryRes> categoryRes = categoryModule.getCategory(companyId);
//            if (categoryRes != null) {
//                int j = 0;
//                for (CategoryRes catgryRes : categoryRes) {
//                    if (j != 0) {
//                        categoryModule.addCategory(catgryRes);
//                    }
//                    j++;
//                }
//            }
//
//
//            List<LocationRes> locationURLList = locationModule.getLocationResList(companyId);
//
//            if (locationURLList != null) {
//                int j = 0;
//                for (LocationRes compyurl : locationURLList) {
//                    if (j != 0) {
//                        locationDao.addLocation(compyurl);
//                    }
//                    j++;
//                }
//            }
//
//            List<MZMerchant> merchantsURLList = merchantModule.getMerchantbycmpy(companyId);
//
//            if (merchantsURLList != null) {
//                int j = 0;
//                for (MZMerchant compyurl : merchantsURLList) {
//                    if (j != 0) {
//                        merchantDao.addMerchant(compyurl);
//                    }
//                    j++;
//                }
//            }
//
//            specList = specialModule.getSpecial(companyId);
//
//            if (specList != null) {
//                int j = 0;
//                specialDao.deactivespecial();
//                for (SpecialRes specurl : specList) {
//                    if (j != 0) {
//                        SpecialRes specli = specialDao.getSpecialRes(specurl.getSpecialId());
//                        if (specli != null) {
//                            specialDao.updatespecial(specurl.getSpecialId());
//                            if (!specli.getHashCode().equals(specurl.getHashCode())) {
//                                specialModule.addSpecial(specurl);
//                            }
//                        } else {
//                            specialModule.addSpecial(specurl);
//                        }
//                    }
//                    j++;
//                }
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected synchronized void onPostExecute(Void result) {
//            Log.i("TAG", "onPostExecute");
//            // new Thread(myThread).start();
//
////            mPullRefreshListView.onRefreshComplete();
//            mSwipyRefreshLayout.setRefreshing(false);
//            list.setClickable(true);
//            list.setEnabled(true);
//
//            prodList = null;
//            if (filterCateList.size() != 0 && filterLocList.size() != 0 && filterMerchList.size() != 0)
//                if (filterCateList.get(0) != 0 || filterLocList.get(0) != 0 || filterMerchList.get(0) != 0) {
//                    prodList = productDao.getProductList();
//                } else {
//                    prodList = productDao.getProductList();
//                }
//            else
//                prodList = productDao.getProductList();
//
//            list.setAdapter(new ProductBaseAdapter(getcontext, prodList,"P"));
//            ListPulldownHelper.getListViewSize(list);
//
//            specList = null;
//            specList = specialDao.getSpecialResList();
//            specialList.setAdapter(new SpecialAdapter(getcontext, specList));
//            ListPulldownHelper.getListViewSize(specialList);
//
//            if (flag.equals("pulldown")) {
//                list.setSelection(prodList.size());
//            }
//
//            if (CompyURLList != null)
//                CompyURLList.clear();
//
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            Log.i("TAG", "onProgressUpdate");
//
//        }
//    }
//
//
//    public class AsyncCallDetail extends AsyncTask<Integer, Void, Void> {
//
//        int position;
//        ProductRes album = null;
//
//        @Override
//        //Make Progress Bar visible
//        protected synchronized void onPreExecute() {
//            Log.i("TAG", "onPostExecute");
//            progress.setVisibility(View.VISIBLE);
//            list.setClickable(false);
//            list.setEnabled(false);
//        }
//
//        @Override
//        protected synchronized Void doInBackground(Integer... params) {
//            // TODO Auto-generated method stub
//            Log.i("TAG", "doInBackground");
//
//            position = params[0];
//            album = prodList.get(position);
//
//            if (album != null) {
//                ProductRes productRes = productModule.getProduct(album.getProdId(),latitude,longitude);
//                if (productRes != null) {
//                    ProductRes prodres = productDao.getProductRes(productRes.getProdId());
//                    if (prodres != null)
//                        if (!productRes.getHashCode().equals(prodres.getItem_hashCode())) {
//
//                            productDao.updateitem_hashcode(productRes.getProdId(), productRes.getHashCode());
//
//                            ArrayList<GroupRes> Groupitemsdelete = productDao.getProductGroupList(productRes.getProdId());
//                            for (GroupRes groupRes : Groupitemsdelete) {
//                                List<ItemRes> itemRes = itemModule.getItemResList(groupRes.getGroupId());
//                                if (itemRes != null)
//                                    for (ItemRes res : itemRes) {
//
//                                        itemModule.deleteModifier(res.getItemId());
//                                    }
//                                itemModule.deleteItem(groupRes.getGroupId());
//                            }
//
//
//                            productDao.deleteProductGroup(productRes.getProdId());
//                            productModule.addProduct(productRes);
//
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
//            }
//
//            ArrayList<SiteProduct> Productsite = productDao.getProductSiteList(album.getProdId());
//            for (SiteProduct prodsite : Productsite) {
//                MechantSite mechantSite = merchant_siteModule.getMerchant_site(prodsite.getSiteId());
//                if (mechantSite != null) {
//                    merchant_SiteDao.deleteMerch_deltime(mechantSite.getSiteId());
//                    merchant_siteModule.addMerchant_site(mechantSite);
//
//                    MZMerchant merchant = merchantModule.getMerchant(mechantSite.getMerchantId());
//                    if (merchant != null)
//                        merchantModule.addMerchant(merchant);
//                }
//            }
//
//            return null;
//        }
//
//
//        @Override
//        protected synchronized void onPostExecute(Void result) {
//            Log.i("TAG", "onPostExecute");
//            // new Thread(myThread).start();
//
//            progress.setVisibility(View.INVISIBLE);
//            list.setClickable(true);
//            list.setEnabled(true);
//
//            Intent intent = new Intent(getcontext, Campaigndetail_Activity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("prodid", album.getProdId());
//            intent.putExtra("listprod", list.getFirstVisiblePosition());
//
//            startActivity(intent);
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            Log.i("TAG", "onProgressUpdate");
//        }
//    }
//
//
//    public class AsyncOnload extends AsyncTask<String, Void, Void> {
//
//        @Override
//        //Make Progress Bar visible
//        protected synchronized void onPreExecute() {
//            Log.i("TAG", "onPostExecute");
//            progress.setVisibility(View.VISIBLE);
//            list.setClickable(false);
//            list.setEnabled(false);
//        }
//
//        @Override
//        protected synchronized Void doInBackground(String... params) {
//            // TODO Auto-generated method stub
//            Log.i("TAG", "doInBackground");
//
//            CompyURLList = assignProdModule.getAssignProd(companyId, 0, size);
//
//            if (CompyURLList != null) {
//                int j = 0;
//                for (CompanyProduct compyurl : CompyURLList) {
//                    if (j != 0) {
//
//                        ProductRes productRes = productModule.getProduct(compyurl.getProdId(),latitude,longitude);
//                        if (productRes != null) {
//                            ProductRes prodres = productDao.getProductRes(productRes.getProdId());
//                            if (prodres != null) {
//                                if (!productRes.getHashCode().equals(prodres.getHashCode())) {
//                                    productModule.addProduct(productRes);
//                                }
//                            } else {
//                                productModule.addProduct(productRes);
//
//                                ArrayList<SiteProduct> Productsite = productDao.getProductSiteList(productRes.getProdId());
//                                for (SiteProduct prodsite : Productsite) {
//                                    MechantSite mechantSite = merchant_siteModule.getMerchant_site(prodsite.getSiteId());
//                                    if (mechantSite != null) {
//                                        merchant_SiteDao.deleteMerch_deltime(mechantSite.getSiteId());
//                                        merchant_siteModule.addMerchant_site(mechantSite);
//
//                                        MZMerchant merchant = merchantModule.getMerchant(mechantSite.getMerchantId());
//                                        if (merchant != null)
//                                            merchantModule.addMerchant(merchant);
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    j++;
//                }
//            }
//
//            return null;
//        }
//
//        @Override
//        protected synchronized void onPostExecute(Void result) {
//            Log.i("TAG", "onPostExecute");
//            // new Thread(myThread).start();
//
//            progress.setVisibility(View.INVISIBLE);
//            list.setClickable(true);
//            list.setEnabled(true);
//
//            prodList = null;
//            if (filterCateList.size() != 0 && filterLocList.size() != 0 && filterMerchList.size() != 0)
//                if (filterCateList.get(0) != 0 || filterLocList.get(0) != 0 || filterMerchList.get(0) != 0) {
//                    prodList = productDao.getProductList();
//                } else {
//                    prodList = productDao.getProductList();
//                }
//            else
//                prodList = productDao.getProductList();
//
//
//            list.setAdapter(new ProductBaseAdapter(getcontext, prodList,"P"));
//            ListPulldownHelper.getListViewSize(list);
//            if (CompyURLList != null)
//                CompyURLList.clear();
//
//            list.setSelection(listprod);
//
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            Log.i("TAG", "onProgressUpdate");
//        }
//    }
//
//
//    public class AsyncOnSearch extends AsyncTask<String, Void, Void> {
//
//        String searchtxt;
//
//        @Override
//        //Make Progress Bar visible
//        protected synchronized void onPreExecute() {
//            Log.i("TAG", "onPostExecute");
//            progress.setVisibility(View.VISIBLE);
//            list.setClickable(false);
//            list.setEnabled(false);
//        }
//
//        @Override
//        protected synchronized Void doInBackground(String... params) {
//            // TODO Auto-generated method stub
//            Log.i("TAG", "doInBackground");
//
//            searchtxt = params[0];
//
//            CompyURLList = assignProdModule.getSearchAssignProd(companyId, searchtxt);
//
//            if (CompyURLList != null) {
//                int j = 0;
//                for (CompanyProduct compyurl : CompyURLList) {
//                    if (j != 0) {
//
//                        ProductRes productRes = productModule.getProduct(compyurl.getProdId(),latitude,longitude);
//                        if (productRes != null) {
//                            ProductRes prodres = productDao.getProductRes(productRes.getProdId());
//                            if (prodres != null) {
//                                if (!productRes.getHashCode().equals(prodres.getHashCode())) {
//                                    productModule.addProduct(productRes);
//                                }
//                            } else {
//                                productModule.addProduct(productRes);
//
//                                ArrayList<SiteProduct> Productsite = productDao.getProductSiteList(productRes.getProdId());
//                                for (SiteProduct prodsite : Productsite) {
//                                    MechantSite mechantSite = merchant_siteModule.getMerchant_site(prodsite.getSiteId());
//                                    if (mechantSite != null) {
//                                        merchant_SiteDao.deleteMerch_deltime(mechantSite.getSiteId());
//                                        merchant_siteModule.addMerchant_site(mechantSite);
//
//                                        MZMerchant merchant = merchantModule.getMerchant(mechantSite.getMerchantId());
//                                        if (merchant != null)
//                                            merchantModule.addMerchant(merchant);
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    j++;
//                }
//            }
//
//            return null;
//        }
//
//        @Override
//        protected synchronized void onPostExecute(Void result) {
//            Log.i("TAG", "onPostExecute");
//            // new Thread(myThread).start();
//
//            progress.setVisibility(View.INVISIBLE);
//            list.setClickable(true);
//            list.setEnabled(true);
//
//            prodList = null;
//            prodList = productDao.getProductList(searchtxt);
//
//            list.setAdapter(new ProductBaseAdapter(getcontext, prodList,"P"));
//            ListPulldownHelper.getListViewSize(list);
//            if (CompyURLList != null)
//                CompyURLList.clear();
//
//            list.setSelection(listprod);
//
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            Log.i("TAG", "onProgressUpdate");
//        }
//    }
//
//    public class AsyncfilterCallWS extends AsyncTask<String, Void, Void> {
//
//        @Override
//        //Make Progress Bar visible
//        protected synchronized void onPreExecute() {
//            Log.i("TAG", "onPostExecute");
//            progress.setVisibility(View.VISIBLE);
//            list.setClickable(false);
//            list.setEnabled(false);
//        }
//
//        @Override
//        protected synchronized Void doInBackground(String... params) {
//            // TODO Auto-generated method stub
//            Log.i("TAG", "doInBackground");
//
//            if (CompyURLList != null) {
//
//                try {
//                    Filterresult = (ArrayList) ObjectSerializer.deserialize(settings.getString("Filterresult", ObjectSerializer.serialize(new ArrayList())));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                int j = 0;
//                for (CompanyProduct compyurl : CompyURLList) {
//                    if (j != 0) {
//
//                        if (Filterresult.size() != 0) {
//                            if (!Filterresult.contains(compyurl.getProdId()))
//                                Filterresult.add(compyurl.getProdId());
//                        } else {
//                            Filterresult.add(compyurl.getProdId());
//                        }
//
//                        ProductRes productRes = productModule.getProduct(compyurl.getProdId(),latitude,longitude);
//                        if (productRes != null) {
//                            ProductRes prodres = productDao.getProductRes(productRes.getProdId());
//                            if (prodres != null) {
//                                if (!productRes.getHashCode().equals(prodres.getHashCode())) {
//                                    productModule.addProduct(productRes);
//                                }
//                            } else {
//                                productModule.addProduct(productRes);
//
//                                ArrayList<SiteProduct> Productsite = productDao.getProductSiteList(productRes.getProdId());
//                                for (SiteProduct prodsite : Productsite) {
//                                    MechantSite mechantSite = merchant_siteModule.getMerchant_site(prodsite.getSiteId());
//                                    if (mechantSite != null) {
//                                        merchant_SiteDao.deleteMerch_deltime(mechantSite.getSiteId());
//                                        merchant_siteModule.addMerchant_site(mechantSite);
//
//                                        MZMerchant merchant = merchantModule.getMerchant(mechantSite.getMerchantId());
//                                        if (merchant != null)
//                                            merchantModule.addMerchant(merchant);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    j++;
//                }
//            }
//
//            specList = specialModule.getSpecial(companyId);
//
//            if (specList != null) {
//                int j = 0;
//                specialDao.deactivespecial();
//                for (SpecialRes specurl : specList) {
//                    if (j != 0) {
//                        SpecialRes specli = specialDao.getSpecialRes(specurl.getSpecialId());
//                        if (specli != null) {
//                            specialDao.updatespecial(specurl.getSpecialId());
//                            if (!specli.getHashCode().equals(specurl.getHashCode())) {
//                                specialModule.addSpecial(specurl);
//                            }
//                        } else {
//                            specialModule.addSpecial(specurl);
//                        }
//                    }
//                    j++;
//                }
//            }
//
//            return null;
//        }
//
//        @Override
//        protected synchronized void onPostExecute(Void result) {
//            Log.i("TAG", "onPostExecute");
//            progress.setVisibility(View.INVISIBLE);
//            mSwipyRefreshLayout.setRefreshing(false);
//            filterindicat.setVisibility(View.VISIBLE);
//
//            try {
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putString("Filterresult", ObjectSerializer.serialize(Filterresult));
//                editor.commit();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            list.setClickable(true);
//            list.setEnabled(true);
//
//            prodList = null;
//            if (filterCateList.size() != 0 && filterLocList.size() != 0 && filterMerchList.size() != 0)
//                if (filterCateList.get(0) != 0 || filterLocList.get(0) != 0 || filterMerchList.get(0) != 0) {
//                    prodList = productDao.getProductList(Filterresult);
//                } else {
//                    prodList = productDao.getProductList();
//                }
//            else
//                prodList = productDao.getProductList();
//
//            list.setAdapter(new ProductBaseAdapter(getcontext, prodList,"P"));
//            ListPulldownHelper.getListViewSize(list);
//            if (flag.equals("pulldown")) {
//                list.setSelection(prodList.size());
//            }
//            if (CompyURLList != null)
//                CompyURLList.clear();
//
//            specList = null;
//            specList = specialDao.getSpecialResList();
//            specialList.setAdapter(new SpecialAdapter(getcontext, specList));
//            ListPulldownHelper.getListViewSize(specialList);
//
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            Log.i("TAG", "onProgressUpdate");
//
//        }
//
//    }
//
//
//    public Productfilter setPojo(int offset, int size) {
//
//        Productfilter productfilter = new Productfilter();
//        productfilter.setCompanyId(companyId);
//        productfilter.setOffset(offset);
//        if (Filterresult.size() != 0)
//            productfilter.setSize(Filterresult.size());
//        else
//            productfilter.setSize(size);
//
//
//        if (filterCateList.get(0) == 0)
//            productfilter.setCategoryList(null);
//        else {
//            ArrayList<CategoryRes> categoryReslist = new ArrayList<>();
//            ArrayList<Integer> list = new ArrayList<>();
//            list.addAll(filterCateList);
//            list.add(0, 0);
//            for (Integer s : list) {
//                CategoryRes categoryRes = new CategoryRes();
//                categoryRes.setCategoryId(s);
//                categoryReslist.add(categoryRes);
//            }
//            productfilter.setCategoryList(categoryReslist);
//        }
//
//        if (filterLocList.get(0) == 0)
//            productfilter.setLocationList(null);
//        else {
//            ArrayList<LocationRes> locationReslist = new ArrayList<>();
//            ArrayList<Integer> list = new ArrayList<>();
//            list.addAll(filterLocList);
//            list.add(0, 0);
//            for (Integer s : list) {
//                LocationRes locationRes = new LocationRes();
//                locationRes.setLocationId(s);
//                locationReslist.add(locationRes);
//            }
//            productfilter.setLocationList(locationReslist);
//        }
//
//        if (filterMerchList.get(0) == 0)
//            productfilter.setMerchantList(null);
//        else {
//            ArrayList<MZMerchant> merchantReslist = new ArrayList<>();
//            ArrayList<Integer> list = new ArrayList<>();
//            list.addAll(filterMerchList);
//            list.add(0, 0);
//            for (Integer s : list) {
//                MZMerchant merchant = new MZMerchant();
//                merchant.setMerchantId(s);
//                merchantReslist.add(merchant);
//            }
//            productfilter.setMerchantList(merchantReslist);
//        }
//
//        return productfilter;
//    }

}