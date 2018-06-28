package com.mezzofy.mzcoupon.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.SystemRequirementsChecker;
import com.google.gson.Gson;
import com.mezzofy.mzcoupon.Dao.DragDao;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;

import com.mezzofy.mzcoupon.R;
import com.mezzofy.mzcoupon.apputills.CommonUtils;
import com.mezzofy.mzcoupon.module.Customer_Module;
import com.mezzofy.mzcoupon.module.DragModule;
import com.mezzofy.mzcoupon.pojo.DrawRes;
import com.squareup.seismic.ShakeDetector;

import java.util.ArrayList;
import java.util.List;

public class DragActivity extends Fragment implements View.OnClickListener, ShakeDetector.Listener, AdapterView.OnItemClickListener {

    private boolean doubleBackToExitPressedOnce = false;
    private AlertDialog Dialog;


    private ImageView luckydraw;
    private EditText code;
    private DragDao daodrag;

    private CustomerEntity user;

    private ListView PullRefreshListView;

    private ProgressBar progress;

    boolean resval = false;

    boolean viewInitialisation = false;

    ShakeDetector sd;
    SensorManager sensorManager;
    View view;


    //private ImageView beacon;

//    private AsyncCallWSDetail onload;

    private List<Beacon> beaconList = new ArrayList<>();

    private ArrayList<DrawRes> promoList;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.drag_activity, null);

        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            viewInitialisation();

            sensorManager = (SensorManager) getActivity().getApplicationContext().getSystemService(Activity.SENSOR_SERVICE);
            sd = new ShakeDetector(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void viewInitialisation() {
        if(viewInitialisation)
            return;


        ImageView qrcode = (ImageView) view.findViewById(R.id.qr_code);
        qrcode.setOnClickListener(this);
        luckydraw = (ImageView) view.findViewById(R.id.lucky_draw);
        luckydraw.setOnClickListener(this);
        PullRefreshListView = (ListView) view.findViewById(R.id.listView);
        PullRefreshListView.setOnItemClickListener(this);

        progress = (ProgressBar) view.findViewById(R.id.progress1);
        code = (EditText) view.findViewById(R.id.text);

        promoList = new ArrayList<>();
        Customer_Module daouser = new Customer_Module(getActivity());

        Applaunch.moddrag = new DragModule(getActivity());
        daodrag = new DragDao(getActivity());

        try {
            user = daouser.getUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewInitialisation = true;

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.lucky_draw:
                if (code.getText().toString().length() > 0) {
                    addLuckyDraw(luckydraw, code.getText().toString());
                } else {
                    CommonUtils.Snackbar(luckydraw, getString(R.string.code_alert));
                }
                break;
            case R.id.qr_code:
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
                break;
//            case R.id.whatsnew:
//                Intent intent1 = new Intent(DragActivity.this, EventActivity.class);
//                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent1);
//                overridePendingTransition(0, 0);
//                /break;
            default:
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        try {
            SystemRequirementsChecker.checkWithDefaultDialogs(getActivity());

            viewInitialisation();

            Applaunch.moddrag.stopBeacon();
            Applaunch.moddrag.startBeacon();
            beaconList = Applaunch.moddrag.getdragbeaconList();

            if(sd != null)
                sd.start(sensorManager);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        try {

            Applaunch.moddrag.stopBeacon();

            if(sd != null)
                sd.stop();

        } catch (Exception ex) {
        }

        super.onPause();
    }


    @Override
    public void onStop() {

        try {
            Applaunch.moddrag.stopBeacon();

            if(sd != null)
                sd.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }


    @Override
    public void hearShake() {

        beaconList = Applaunch.moddrag.getbeaconList();
        if (beaconList != null && beaconList.size() > 0) {

            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);

            if(sd != null)
                sd.stop();

            DrawRes res = Applaunch.moddrag.getBeaconOnshake(beaconList, luckydraw);
            if (res != null) {
                luckyDrawonclick(res);
                beaconList = new ArrayList<>();
            } else {

                if(sd != null)
                    sd.start(sensorManager);

                CommonUtils.Snackbar(luckydraw, getString(R.string.invalid_code));
            }
        } else {
            CommonUtils.Snackbar(luckydraw, getString(R.string.try_again));
        }
    }

    public void addLuckyDraw(View view, String code) {

        DrawRes objdraw = Applaunch.moddrag.addluckydrag(view, code);
        if (objdraw != null) {
            luckyDrawonclick(objdraw);
        } else {
            CommonUtils.Snackbar(luckydraw, getString(R.string.invalid_code));
        }
    }

    public void luckyDrawonclick(DrawRes objdraw) {

        if (objdraw != null && objdraw.getTitle() != null) {

//            promoList = daodrag.getPromocodeList();
//
//            if (promoList != null && promoList.size() > 0) {
//                PullRefreshListView.setVisibility(View.VISIBLE);
//                PullRefreshListView.setAdapter(new DragListAdapter(getActivity(), promoList));
//            }
//
//            if (objdraw.getTitle().equals("MZMerchant Promotion")) {
//                Intent intent = new Intent(getActivity(), DragMerchantActivity.class);
//                intent.putExtra("objpromo", objdraw);
//                intent.putExtra("flag", "drag");
//                startActivity(intent);
//
//            } else {
//                Intent intent = new Intent(getActivity(), DragDetailActivity.class);
//                intent.putExtra("objpromo", objdraw);
//                intent.putExtra("flag", "drag");
//                startActivity(intent);
//            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DrawRes ret = promoList.get(position);

//        if (ret != null && ret.getTitle().equals("MZMerchant Promotion")) {
//            Intent intent = new Intent(getActivity(), DragMerchantActivity.class);
//            intent.putExtra("objpromo", ret);
//            intent.putExtra("flag", "drag");
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent(getActivity(), DragDetailActivity.class);
//            intent.putExtra("objpromo", ret);
//            intent.putExtra("flag", "drag");
//            startActivity(intent);
//        }
    }

//    private class AsyncCallWSDetail extends AsyncTask<Integer, Void, Void> {
//
//        int position;
//
//        @Override
//        //Make Progress Bar visible
//        protected synchronized void onPreExecute() {
//            Log.i("TAG", "onPostExecute");
//            progress.setVisibility(View.VISIBLE);
//            PullRefreshListView.setClickable(false);
//            PullRefreshListView.setEnabled(false);
//        }
//
//        @Override
//        protected synchronized Void doInBackground(Integer... params) {
//            // TODO Auto-generated method stub
//            Log.i("TAG", "doInBackground");
//
//            promoList = Applaunch.moddrag.getpromocode();
//
//            return null;
//        }
//
//
//        @Override
//        protected synchronized void onPostExecute(Void result) {
//            Log.i("TAG", "onPostExecute");
//            // new Thread(myThread).start();
//            progress.setVisibility(View.GONE);
//            PullRefreshListView.setClickable(true);
//            PullRefreshListView.setEnabled(true);
//
//            if (promoList != null && promoList.size() > 0) {
//                PullRefreshListView.setVisibility(View.VISIBLE);
//                PullRefreshListView.setAdapter( new DragListAdapter(DragActivity.this, promoList));
//                beacon.setVisibility(View.GONE);
//            } else {
//                beacon.setVisibility(View.VISIBLE);
//            }
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            Log.i("TAG", "onProgressUpdate");
//        }
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("SCAN_RESULT");
                Gson gson = new Gson();
                if (result != null) {
                    result.replaceAll("\"", "'");

                    try {
                        DrawRes res = gson.fromJson(result, DrawRes.class);
//                        resval = daodrag.addPromo(res);
                        DrawRes objdraw = Applaunch.moddrag.getluckyDraw(luckydraw, res);
                        luckyDrawonclick(objdraw);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CommonUtils.Snackbar(luckydraw, getString(R.string.invalid_code));
                    }
                }
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //handle cancel
        }
    }

    public void updatebeaconList(List<Beacon> list) {
        beaconList = list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}