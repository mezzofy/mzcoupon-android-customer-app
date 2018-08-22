package com.mezzofy.mzcoupon.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import com.mezzofy.mzcoupon.Entity.CampGrpEntity;
import com.mezzofy.mzcoupon.Entity.CampaignEntity;
import com.mezzofy.mzcoupon.Entity.SiteEntity;
import com.mezzofy.mzcoupon.Entity.Size;
import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.Entity.CampaignGroupmEntity;
import com.mezzofy.mzcoupon.Entity.CampaignmEntity;
import com.mezzofy.mzcoupon.Entity.CampaignSearchmEntity;
import com.mezzofy.mzcoupon.module.Campaign_Module;

import com.mezzofy.mzcoupon.Adapter.ProductBaseAdapter;
import com.mezzofy.mzcoupon.Adapter.SpecialAdapter;
import com.mezzofy.mzcoupon.apputills.CommonUtils;
import com.mezzofy.mzcoupon.apputills.ListPulldownHelper;
import com.mezzofy.mzcoupon.apputills.ObjectSerializer;
import com.mezzofy.mzcoupon.pojo.CompanyProduct;
import com.mezzofy.mzcoupon.pojo.Productfilter;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mezzofy.com.libmzcoupon.utills.APIServerException;

public class CampaignMainList_Activity extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnClickListener {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 20000;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    double mlatitude=0.0; // latitude
    double mlongitude=0.0; // longitude
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    public AlertDialog Dialog;
    SharedPreferences.Editor editor;

    private boolean doubleBackToExitPressedOnce = false;

    String flag = "pulldown";


    int listprod;
    String companyId;
    int totalCount = 0, count = 0;

    private SearchView.OnQueryTextListener searchList;
    SharedPreferences settings;

    private ProgressBar progress;

    private ArrayList<String> filterCateList = new ArrayList<String>();
    private ArrayList<String> filterLocList = new ArrayList<String>();
    private ArrayList<Integer> filterMerchList = new ArrayList<Integer>();
    private ArrayList<Integer> Filterresult = new ArrayList<>();
    private ListView list;
    private List<CompanyProduct> CompyURLList = null;

    private TextView filterindicat;
    private TextView eventindicator;
    private static TextView favindicator;

    private ListView specialList;
    private SwipyRefreshLayout mSwipyRefreshLayout;

    private ArrayList<CampaignGroupmEntity> CampaignGroupList = null;

    private Gson gson = new Gson();

    private RelativeLayout layout;

    private CommonUtils common;

    private boolean hotdeals = false;

    int Pages=1,limit=0,offset=20;
    private Size size;

    String CamIdforDetail=null;

    String searchtxt;


//MZCampaign
    private Campaign_Module campaignModule;
    private ArrayList<CampaignmEntity> campaignModelList=new ArrayList<CampaignmEntity>();
    private List<CampaignmEntity> serverModellist=new ArrayList<CampaignmEntity>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.product, null);

        try {

            buildGoogleApiClient();

            common = new CommonUtils();
            layout = (RelativeLayout) view.findViewById(R.id.signin_page);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            companyId = settings.getString("company_Id", null);

//            coupon = (TextView) findViewById(R.id.coupon);

            campaignModule=new Campaign_Module(getActivity());

            Bundle extras1 = getActivity().getIntent().getExtras();

            progress = (ProgressBar) view.findViewById(R.id.progress1);
            mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
            list = (ListView) view.findViewById(R.id.couponlist);
//            specialList = (ListView) view.findViewById(R.id.speciallist);

            eventindicator = (TextView) view.findViewById(R.id.eventindicator1);
            filterindicat = (TextView) view.findViewById(R.id.filterindicat);
            favindicator=(TextView) view.findViewById(R.id.favindicator);


//            specialList.setSelector(R.drawable.listselector);

//            specialList.setOnItemClickListener(new OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    position = (int) id;
//                    CampaignGroupmEntity album = CampaignGroupList.get(position);
//
//                    Intent intent = new Intent(getActivity(), SpecialDetailActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("tabName", getString(R.string.menu_Special));
//                    intent.putExtra("currTab", 0);
//                    intent.putExtra("CampgrpId", album.getCampaigngroup().getCampgrpId());
//                    intent.putExtra("CampgrpImageurl", album.getCampaigngroup().getCampgrpImageurl());
//                    intent.putExtra("CampgrpName", album.getCampaigngroup().getCampgrpName());
//                    startActivity(intent);
//                }
//            });


            list.setSelector(R.drawable.listselector);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    CamIdforDetail=campaignModelList.get(position).getCampaign().getCampaignId();

                    AsyncstartCampaignDetail onstart = new AsyncstartCampaignDetail();
                    onstart.execute();

                }
            });



            ImageView imgfav = (ImageView) view.findViewById(R.id.imageView1);
            imgfav.setOnClickListener(this);

            ImageView imgfli = (ImageView) view.findViewById(R.id.imageViewflit2);
            imgfli.setOnClickListener(this);

            ImageView map = (ImageView) view.findViewById(R.id.map);
            map.setOnClickListener(this);

            ImageView event = (ImageView) view.findViewById(R.id.event);
            event.setOnClickListener(this);

            Button deals = (Button) view.findViewById(R.id.hotdeals);

            deals.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                   filterCateList = new ArrayList<String>();
                   filterLocList = new ArrayList<String>();

                    if (!hotdeals) {
                        hotdeals = true;
                        AsyncOnhotdeals asyncOnhotdeals = new AsyncOnhotdeals();
                        asyncOnhotdeals.execute();
                    } else {
                        hotdeals = false;
                        AsyncOnload asyncOnload = new AsyncOnload();
                        asyncOnload.execute();
                    }
                }
            });

            Bundle extras = getActivity().getIntent().getExtras();
            if (extras != null)
                listprod = extras.getInt("listprod");



            filterindicat.setOnClickListener(this);
            if (filterCateList.size() != 0 || filterLocList.size() != 0) {
                filterindicat.setVisibility(View.VISIBLE);
            }

            try {
                filterCateList = (ArrayList) ObjectSerializer.deserialize(settings.getString("sendfilterCateList", ObjectSerializer.serialize(new ArrayList())));
                filterLocList = (ArrayList) ObjectSerializer.deserialize(settings.getString("sendfilterLocList", ObjectSerializer.serialize(new ArrayList())));
            } catch (IOException e) {
                e.printStackTrace();
            }

            setfavCount();


            mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection direction) {
                    try {
                        if (direction == SwipyRefreshLayoutDirection.TOP) {

                            Pages=1;
                            limit=0;

                            try {
                                campaignModelList = campaignModule.getCampaignList(limit,offset);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            mSwipyRefreshLayout.setRefreshing(false);

                        }
                        if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                            flag = "pulldown";
                            size=(Size) ObjectSerializer.deserialize(settings.getString("size", ObjectSerializer.serialize(new Size())));

                            if(++Pages<=size.getPagesize()) {
                                limit+=offset;
                                Asyncstart onstart = new Asyncstart();
                                onstart.execute();
                            }
                            else
                                mSwipyRefreshLayout.setRefreshing(false);


                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });




            final SearchView mySearchView = (SearchView) view.findViewById(R.id.searchly);
            searchList = new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    // TODO Auto-generated method stub
                    Log.d("input", query);

                    mySearchView.clearFocus();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // TODO Auto-generated method stub
                    Log.d("inputkey", newText);

                    if(newText.length()>=3) {
                        Pages = 1;
                        limit=0;
                        offset=20;
                        AsyncOnSearch task = new AsyncOnSearch();
                        task.execute(newText);
                    }

                    return false;
                }
            };

            SearchView.OnCloseListener onCloseListener = new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            };

            mySearchView.setOnQueryTextListener(searchList);
            mySearchView.setOnCloseListener(onCloseListener);
            mySearchView.setSubmitButtonEnabled(true);

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
                intent = new Intent(getActivity(), FavouriteActivity.class);
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

            case R.id.map:
                intent = new Intent(getActivity(), SitelocationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (mCurrentLocation != null) {
                    intent.putExtra("Latitude", mCurrentLocation.getLatitude());
                    intent.putExtra("Longitude", mCurrentLocation.getLongitude());

//                    intent.putExtra("Latitude",1.557595);
//                    intent.putExtra("Longitude", 103.78853);
                }
                intent.putExtra("flag", "back");
                startActivity(intent);
                break;

            case R.id.event:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
        editor.apply();

        Intent intent = new Intent(getActivity(), FilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public class AsyncOnSearch extends AsyncTask<String, Void, Void> {


        List<CampaignmEntity> searchlist;

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");

        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");
            try {
                searchtxt = params[0];

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();

               try {
                   campaignModelList =new  ArrayList<CampaignmEntity>();
                   campaignModelList=campaignModule.getCampaignListSearch("A",searchtxt,limit,offset);


                   if (campaignModelList != null && campaignModelList.size() > 0) {
                       list.setAdapter(new ProductBaseAdapter(getActivity(), campaignModelList, "P"));
                       ListHelper.getListViewSize(list);
                   }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }





    public class Asyncstart extends AsyncTask<String, Void, Void> {
        List<CampaignGroupmEntity> campaignGroupModels;
        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            list.setClickable(false);
            list.setEnabled(false);
//            specialList.setClickable(false);
//            specialList.setEnabled(false);
            progress.setVisibility(View.VISIBLE);
            //coupon.setVisibility(View.GONE);
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            //CampaignDetail
            Log.d("mlatitude  mlongitude--","     "+mlatitude);

            if(filterCateList!=null && filterCateList.size()>0 || filterLocList!=null && filterLocList.size()>0)
            {

                CampaignSearchmEntity campaignSearchModel= new CampaignSearchmEntity();
                List<SiteEntity> listsitedate=null;
                if(filterLocList.size()>0) {
                    listsitedate=new ArrayList<>();
                    for (String s : filterLocList) {
                        SiteEntity siteData = new SiteEntity();
                        siteData.setSiteId(s);
                        listsitedate.add(siteData);
                    }
                    campaignSearchModel.setSites(listsitedate);
                }
//                List<CampGrpEntity> listcampgrpdata=null;
//                if(filterCateList.size()>0) {
//                    listcampgrpdata=new ArrayList<>();
//                    for (String s : filterCateList) {
//                        CampGrpEntity campGrpData = new CampGrpEntity();
//                        campGrpData.setCampgrpId(s);
//                        listcampgrpdata.add(campGrpData);
//                    }
//                    campaignSearchModel.setGroups(listcampgrpdata);
//                }


                campaignSearchModel.setPage(Pages);
                campaignSearchModel.setLatitude(mlatitude);
                campaignSearchModel.setLongitude(mlongitude);

                try {
                    serverModellist = campaignModule.getCampaignsFilterfromServerAPI(campaignSearchModel);
                } catch (APIServerException e) {
                    Toast.makeText(getActivity().getApplicationContext(),"Error--"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
            else {
                if (mlatitude > 0 || mlongitude > 0)
                    serverModellist = campaignModule.getCampaignsfromServerAPI(Pages, mlatitude, mlongitude);
                else
                    serverModellist = campaignModule.getCampaignsfromServerAPI(Pages, 0, 0);
            }



//            campaignGroupModels=campaignModule.getCampaignsGroup();



            return null;
        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();
            try {
                list.setClickable(true);
                list.setEnabled(true);
//                specialList.setClickable(true);
//                specialList.setEnabled(true);
                //coupon.setVisibility(View.VISIBLE);

                if (serverModellist != null) {
                    try {
                        boolean res= campaignModule.updateCampaignflage();
                        if(res)
                            campaignModule.addCampaignlist(serverModellist);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



                    if(serverModellist!=null && serverModellist.size()>0) {
                        try {
                            campaignModelList.addAll(campaignModule.getCampaignList(limit, offset));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


           /*     if(campaignGroupModels!=null)
                {
                    try {
                        campaignModule.addCampaignGroup(campaignGroupModels);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                try {
                    CampaignGroupList = campaignModule.getCampaignGrouplist();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/


                if(campaignModelList!=null && campaignModelList.size()>0) {
                    list.setAdapter(new ProductBaseAdapter(getActivity(), campaignModelList, "P"));
                    ListHelper.getListViewSize(list);
                }

//                if (CampaignGroupList!=null && CampaignGroupList.size() > 0) {
//                    specialList.setAdapter(new SpecialAdapter(getActivity().getApplicationContext(), CampaignGroupList));
//                    ListPulldownHelper.getListViewSize(specialList);
//                }

                progress.setVisibility(View.INVISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }

            setfavCount();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }



    public class AsyncstartCampaignDetail extends AsyncTask<String, Void, Void> {

        CampaignEntity campaignData;
        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            list.setClickable(false);
            list.setEnabled(false);
            //coupon.setVisibility(View.GONE);
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");

            //CampaignDetail
            Log.d("mlatitude  mlongitude--","     "+mlatitude);
            if(mlatitude>0 || mlongitude>0) {
                campaignData = campaignModule.getCampaignDetailfromServerAPI(CamIdforDetail, mlatitude, mlongitude);

            }

            return null;
        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();
            try {
                list.setClickable(true);
                list.setEnabled(true);

                if(campaignData!=null) {
                    CampaignmEntity campaignModel=new CampaignmEntity();
                    campaignModel.setCampaign(campaignData);
                    campaignModule.addCampaign(campaignModel);
                }

                Intent intent = new Intent(getActivity(), Campaigndetail_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("prodid",CamIdforDetail);
                intent.putExtra("listprod", list.getFirstVisiblePosition());

                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }

    public class AsyncOnload extends AsyncTask<String, Void, Void> {

        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            progress.setVisibility(View.VISIBLE);
            list.setClickable(false);
            list.setEnabled(false);
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");
            try {

                //CampaignDetail
                Log.d("mlatitude  mlongitude--","     "+mlatitude);
                if(mlatitude>0 || mlongitude>0)
                    serverModellist = campaignModule.getCampaignsfromServerAPI(1,mlatitude,mlongitude);
                else
                    serverModellist = campaignModule.getCampaignsfromServerAPI(1,0,0);

                if (serverModellist != null) {
                    try {
                        boolean res=campaignModule.updateCampaignflage();
                        Log.d("serverModellist",""+serverModellist.size());
                        if(res)
                        campaignModule.addCampaignlist(serverModellist);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();
            try {
                progress.setVisibility(View.INVISIBLE);
                list.setClickable(true);
                list.setEnabled(true);


                try {
                    campaignModelList = campaignModule.getCampaignList(limit, offset);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (CampaignMainList_Activity.this != null)
                    if (!getActivity().isFinishing()) {
                        if (campaignModelList != null && campaignModelList.size() > 0) {
                            list.setAdapter(new ProductBaseAdapter(getActivity().getApplicationContext(), campaignModelList, "P"));
                            ListPulldownHelper.getListViewSize(list);

                            if (CompyURLList != null)
                                CompyURLList.clear();

                            list.setSelection(listprod);
                        }
                    }

                setfavCount();

//                EventDao eventDao = new EventDao(getApplicationContext());
//                if (eventDao.countEvent() > 0) {
//                    eventindicator.setVisibility(View.VISIBLE);
//                    eventindicator.setText(String.valueOf(eventDao.countEvent()));
//                } else
//                    eventindicator.setVisibility(View.GONE);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }

    public static  void setfavCount(int count)
    {
        if (count > 0) {
            favindicator.setVisibility(View.VISIBLE);
            favindicator.setText(String.valueOf(count));
        } else
            favindicator.setVisibility(View.GONE);
    }


    public void setfavCount()
    {
        int count=0;
        try {
            campaignModule=new Campaign_Module(getContext());
            count=campaignModule.getFavouriteCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (count > 0) {
            favindicator.setVisibility(View.VISIBLE);
            favindicator.setText(String.valueOf(count));
        } else
            favindicator.setVisibility(View.GONE);
    }

    public Productfilter setPojo(int offset, int size) {

        Productfilter productfilter = new Productfilter();
      //  productfilter.setCompanyId(companyId);
        productfilter.setOffset(offset);
        if (Filterresult.size() != 0)
            productfilter.setSize(Filterresult.size());
        else
            productfilter.setSize(size);

        return productfilter;
    }

    public class AsyncOnhotdeals extends AsyncTask<String, Void, Void> {


        @Override
        //Make Progress Bar visible
        protected synchronized void onPreExecute() {
            Log.i("TAG", "onPostExecute");
            progress.setVisibility(View.VISIBLE);
            list.setClickable(false);
            list.setEnabled(false);
        }

        @Override
        protected synchronized Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("TAG", "doInBackground");
            try {

                serverModellist = campaignModule.gettopcoupon();
                if (serverModellist != null) {
                    try {
                        campaignModule.addToptenCampaignlist(serverModellist,1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



                try {
                    campaignModelList = campaignModule.getToptemCampaignList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
              //  Log.d("prodhot",String.valueOf(prodList.size()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected synchronized void onPostExecute(Void result) {
            Log.i("TAG", "onPostExecute");
            // new Thread(myThread).start();
            try {
                progress.setVisibility(View.INVISIBLE);
                list.setClickable(true);
                list.setEnabled(true);

                list.setAdapter(new ProductBaseAdapter(getActivity().getApplicationContext(), campaignModelList, "H"));
                ListPulldownHelper.getListViewSize(list);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("TAG", "onProgressUpdate");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }

        if (eventindicator != null) {

//            EventDao eventDao = new EventDao(getApplicationContext());
//            if (eventDao.countEvent() > 0) {
//                eventindicator.setVisibility(View.VISIBLE);
//                eventindicator.setText(String.valueOf(eventDao.countEvent()));
//            } else
//                eventindicator.setVisibility(View.GONE);
        }

        setfavCount();
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
            return;
        }

        isLocationEnabled();
        if (!isLocationEnabled()) {
            ShowGpsSettings();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);

    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
            return;
        }

        isLocationEnabled();
        if (!isLocationEnabled()) {
            ShowGpsSettings();
            return;
        }

        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            try {
                if(mCurrentLocation!=null) {
                    mlatitude = mCurrentLocation.getLatitude();
                    mlongitude = mCurrentLocation.getLongitude();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        startLocationUpdates();

    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        try {
            mlatitude = mCurrentLocation.getLatitude();
            mlongitude = mCurrentLocation.getLongitude();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }


    protected boolean isLocationEnabled() {
        String le = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(le);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    private void ShowGpsSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("GPS Settings")
                .setMessage("GPS is not enabled. Do you want to go to settings menu?")
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    dialog.cancel();
                                } catch (Exception E) {
                                    E.printStackTrace();
                                }
                            }
                        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event

                dialog.dismiss();
            }
        });


        //builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request

        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    opendialog("Read Location");
                }
            }
            if (permissions[i].equals(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    opendialog("Read Location");
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    public void opendialog(String msg) {
        try {

            AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

            if (Dialog != null && Dialog.isShowing()) {


            } else {
                alertDialogBuilder.setTitle("Permission");
                alertDialogBuilder.setMessage("This App needs " + msg + " permission please enable!");
                alertDialogBuilder.setIcon(R.drawable.appicon);
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getActivity().getApplicationContext().getPackageName()));
                            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(myAppSettings, 168);
                            dialog.cancel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Dialog = alertDialogBuilder.create();
                Dialog.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onLowMemory() {
        // use it only for older API version
        super.onLowMemory();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }


}
