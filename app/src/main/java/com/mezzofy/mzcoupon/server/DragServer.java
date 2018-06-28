package com.mezzofy.mzcoupon.server;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mezzofy.mzcoupon.apputills.CommonModule;
import com.mezzofy.mzcoupon.apputills.JSONSTRINGS;
import com.mezzofy.mzcoupon.pojo.BaconServer;
import com.mezzofy.mzcoupon.pojo.LuckyServer;
import com.mezzofy.mzcoupon.pojo.LuckydrawServer;
import com.mezzofy.mzcoupon.pojo.MerchantServer;
import com.mezzofy.mzcoupon.pojo.ProductServer;
import com.mezzofy.mzcoupon.pojo.PromotionServer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

/**
 * Created by udhayinforios on 13/6/16.
 */
public class DragServer {

    private Gson gson = new Gson();

    public DragServer(Context context) {

    }


    public PromotionServer getdragtype(String code) {

        PromotionServer wallet = null;

//        String User = JSONSTRINGS.getJSONFromUrl(CommonModule.getUrlpath5() + "promotion/" + code);
//        if (User != null)
//            User.replaceAll("\"", "'");
//        wallet = gson.fromJson(User, PromotionServer.class);
//        if (wallet != null && wallet.getResponse().getResponse_code().equals("0")) {
//            DrawRes res = gson.fromJson(wallet.getPromotion().getPromotion_tag(), DrawRes.class);
//            if (res != null)
//                wallet.getPromotion().setPromotiontag(res);
//        } else {
//            wallet = null;
//        }

        return wallet;
    }


    public LuckyServer getinstantdraw(String code, String mail) {

        LuckyServer wallet = null;

        String User = JSONSTRINGS.getJSONFromUrl("instantdraw/" + code + "?email=" + mail);
        wallet = gson.fromJson(User, LuckyServer.class);
//        if(wallet==null ||wallet.getResponse().getResponse_code().equals("1")){
//            wallet = null;
//        }

        return wallet;
    }

    public PromotionServer getdragtypebyBeaconId(String code) {

        PromotionServer wallet = null;

//        String User = JSONSTRINGS.getJSONFromUrl(CommonModule.getUrlpath5() + "promotionbydevice/" + code);
//        if (User != null)
//            User.replaceAll("\"", "'");
//        wallet = gson.fromJson(User, PromotionServer.class);
//        if (wallet != null && wallet.getResponse().getResponse_code().equals("0")) {
//            DrawRes res = gson.fromJson(wallet.getPromotion().getPromotion_tag(), DrawRes.class);
//            if (res != null)
//                wallet.getPromotion().setPromotiontag(res);
//        } else {
//            wallet = null;
//        }

        return wallet;
    }

    public LuckyServer getluckydraw(String code, String mail) {

        LuckyServer wallet = null;

//        String User = JSONSTRINGS.getJSONFromUrl(CommonModule.getUrlpath5() + "luckydraw/" + code + "?email=" + mail);
//        wallet = gson.fromJson(User, LuckyServer.class);
//        if(wallet==null ||!wallet.getResponse().getResponse_msg().equals("You already participate.")){
//            wallet = null;
//        }

        return wallet;
    }

    public LuckyServer getLuckyCoupon(String code) {

        LuckyServer wallet = null;

//        String User = JSONSTRINGS.getJSONFromUrl(CommonModule.getUrlpath5() + "productID/" + code);
//        wallet = gson.fromJson(User, LuckyServer.class);
//        if (wallet == null || wallet.getResponse().getResponse_code().equals("1")) {
//            wallet = null;
//        }

        return wallet;
    }


    public LuckydrawServer getLuckydrawList(int user_id) {

        LuckydrawServer wallet = null;

//        String User = JSONSTRINGS.getJSONFromUrl(CommonModule.getUrlpath5() + "getluckydraw/" + user_id);
//        wallet = gson.fromJson(User, LuckydrawServer.class);
//
//        if (wallet != null && wallet.getResponse().getResponse_code().equals("0")) {
//
//            if (wallet.getResult() != null && wallet.getResult().size() > 0)
//                wallet.getResult().remove(0);
//        } else {
//            wallet = null;
//        }

        return wallet;
    }

    public LuckyServer getLuckydrawdetail(String code) {

        LuckyServer wallet = null;
//
//        String User = JSONSTRINGS.getJSONFromUrl(CommonModule.getUrlpath5() + "luckydraw_detail/" + code);
//        wallet = gson.fromJson(User, LuckyServer.class);
//        if (wallet == null || wallet.getResponse().getResponse_code().equals("1")) {
//            wallet = null;
//        }

        return wallet;
    }

    public LuckyServer getinstantdrawdetail(String code) {

        LuckyServer wallet = null;

//        String User = JSONSTRINGS.getJSONFromUrl(CommonModule.getUrlpath5() + "instantdraw_detail/" + code);
//        wallet = gson.fromJson(User, LuckyServer.class);
//        if (wallet == null || wallet.getResponse().getResponse_code().equals("1")) {
//            wallet = null;
//        }

        return wallet;
    }

    public ProductServer getproductdetail(String code) {

        ProductServer wallet = null;

//        String User = JSONSTRINGS.getJSONFromUrl(CommonModule.getUrlpath6() + "product_by_code?productId=" + code);
//        wallet = gson.fromJson(User, ProductServer.class);

        return wallet;
    }


    public MerchantServer getmerchantdetail(String code) {

        MerchantServer wallet = null;

//        String User = JSONSTRINGS.getJSONFromUrl(CommonModule.getUrlpath6() + "merchant_code/" + code);
//        wallet = gson.fromJson(User, MerchantServer.class);

        return wallet;
    }


    public PromotionServer postBeaconList(BaconServer beacon_List) {

        PromotionServer resp = null;
        try {

            URL url = new URL( "promotion_devices/");
            OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(CommonModule.getKey(), CommonModule.getSecretkey());

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new SigningInterceptor(consumer))
                    .build();

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(beacon_List).toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                String responseBodyString = response.body().string();
                Response newResponse = response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();

                String json = newResponse.body().string();
                if (json != null)
                    json.replaceAll("\"", "'");
                resp = gson.fromJson(json, PromotionServer.class);
//                if (resp != null && resp.getResponse().getResponse_code().equals("0")) {
//                    DrawRes res = gson.fromJson(resp.getPromotion().getPromotion_tag(), DrawRes.class);
//                    if (res != null)
//                        resp.getPromotion().setPromotiontag(res);
//                } else {
//                    resp = null;
//                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        return resp;
    }

}
