package com.mezzofy.mzcoupon.module;

import android.content.Context;
import android.view.View;

import com.estimote.sdk.Beacon;
import com.mezzofy.mzcoupon.Activity.DragActivity;
import com.mezzofy.mzcoupon.Activity.WelcomeActivity;
import com.mezzofy.mzcoupon.Dao.DragDao;
import com.mezzofy.mzcoupon.Entity.CustomerEntity;
import com.mezzofy.mzcoupon.apputills.BeconProperties;
import com.mezzofy.mzcoupon.pojo.DrawRes;


import java.util.List;

/**
 * Created by udhayinforios on 23/2/16.
 */

public class DragModule {

    private Context context;
//    private WalletDao walletDao;

    private DragDao daodrag;
//    private DragServer serverdrag;
    private WelcomeActivity objwelcome;
    private DragActivity objdrag;

    public DragModule(Context context) {
        this.context = context;
//        walletDao = new WalletDao(context);
//        serverdrag = new DragServer(context);
        daodrag = new DragDao(context);
        objdrag = new DragActivity();
        objwelcome = new WelcomeActivity();
    }


    public List<Beacon> getbeaconList() {

        BeconProperties objbeacon = new BeconProperties();
        List<Beacon> beaconList = objbeacon.getBeaconList();

        return beaconList;
    }


    public List<Beacon> getdragbeaconList() {

        BeconProperties objbeacon = new BeconProperties();
        List<Beacon> beaconList = objbeacon.getdragBeaconList();

        return beaconList;
    }

    public List<Beacon> returnbeaconList(List<Beacon> beaconList) {

        objwelcome.updatebeaconList(beaconList);

        return beaconList;
    }

    public List<Beacon> returndragbeaconList(List<Beacon> beaconList) {

        objdrag.updatebeaconList(beaconList);
        return beaconList;
    }


    public void stopBeacon() {

        BeconProperties.stopBeacon();

    }

    public void startBeacon() {

        BeconProperties.startBeacon(context);

    }


    public DrawRes addluckydrag(View view, String code) {

//        PromotionServer promotion = serverdrag.getdragtype(code);
        DrawRes objdraw = null;
//        if (promotion != null) {
//            if (promotion.getPromotion().getPromotiontag() != null) {
//
//                objdraw = getluckyDraw(view, promotion.getPromotion().getPromotiontag());
//            }
//        } else {
//            objdraw = null;
//        }

        return objdraw;
    }


    public DrawRes getluckyDraw(View view, DrawRes res) throws Exception {

        Customer_Module daouser = new Customer_Module(context);

        DrawRes objdraw = new DrawRes();
//        LuckyServer objinstantdraw = new LuckyServer();
        CustomerEntity user = daouser.getUser();

        objdraw.setCode(res.getCode());
        objdraw.setPromoid(res.getPromoid());
        objdraw.setType(res.getType());
        objdraw.setCreatedon(res.getCreatedon());

        if (res != null && res.getType() != null)
            if (res.getType().equals("C")) {
////                DrawRes objdrawres = daoproduct.getProductbypromocode(res.getCode());
////                if (objdrawres == null) {
//                    ProductServer server = serverdrag.getproductdetail(res.getCode());
//                    if (server != null && server.getResponse() != null && server.getResponse().getResponse_code().equals("0")) {
//
//                        objdraw.setName(server.getProduct().getProductName());
//                        objdraw.setDesc(server.getProduct().getProductDesc());
//                        if (server.getProduct().getProdImageList() != null && server.getProduct().getProdImageList().size() > 0) {
//                            if (server.getProduct().getProdImageList().size() > 1)
//                                objdraw.setImage(server.getProduct().getProdImageList().get(1).getProductImage());
//                            else
//                                objdraw.setImage(server.getProduct().getProdImageList().get(0).getProductImage());
//                        }
//                        objdraw.setId(server.getProduct().getProdId());
//                        objdraw.setTitle("MZCoupon Promotion");
//                    } else if (server != null && server.getResponse() != null && server.getResponse().getResponse_code().equals("1")) {
//
//                        CommonUtils.showMessage(server.getResponse().getResponse_code(), view, context);
//                    }
////                }
            } else if (res.getType().equals("M")) {

//                DrawRes objdrawres = daomerchant.getMerchantbypromocode(res.getCode());

//                MerchantServer merres = serverdrag.getmerchantdetail(res.getCode());
//                if (merres != null && merres.getResponse() != null && merres.getResponse().getResponse_code().equals("0")) {
//                    objdraw.setName(merres.getMerchant().getMerchantName());
//                    objdraw.setDesc(merres.getMerchant().getMerchantDesc());
//                    objdraw.setImage(merres.getMerchant().getMerchantLogo());
//                    objdraw.setId(merres.getMerchant().getMerchantId());
//                    objdraw.setTitle("MZMerchant Promotion");
//                } else if (merres != null && merres.getResponse() != null && merres.getResponse().getResponse_code().equals("1")) {
//                    CommonUtils.showMessage(merres.getResponse().getResponse_code(), view, context);
//                }
//            } else if (res.getType().equals("I")) {
//                //SHAN
//                objinstantdraw = serverdrag.getinstantdrawdetail(res.getCode());
//                if (objinstantdraw.getResponse().getResponse_code().equals("0")) {
//                    objdraw.setTitle("Instantdraw Promotion");
//                    objdraw.setName(objinstantdraw.getResult().getName());
//                    objdraw.setDesc(objinstantdraw.getResult().getDescription());
//                    objdraw.setImage(objinstantdraw.getResult().getImagePath());
//                } else {
//                    if (objinstantdraw != null)
//                        CommonUtils.showMessage(objinstantdraw.getResponse().getResponse_code(), view, context);
//                }

              /*  if (user != null && user.getCustomer_email() != null) {
                    objinstantdraw = serverdrag.getinstantdraw(res.getPromoid(), user.getCustomer_email());

                    if (objinstantdraw != null && objinstantdraw.getResponse().getResponse_code().equals("1")) {
                        if (objinstantdraw.getResponse().getResponse_msg().equals("You already participate.")) {
                            objinstantdraw = serverdrag.getinstantdrawdetail(res.getCode());
                            if (objinstantdraw.getResponse().getResponse_code().equals("0")) {
                                objdraw.setTitle("Instantdraw Promotion");
                                objdraw.setName(objinstantdraw.getResult().getName());
                                objdraw.setDesc(objinstantdraw.getResult().getDescription());
                                objdraw.setImage(objinstantdraw.getResult().getImagePath());
                            }
                        } else if (objinstantdraw.getResponse().getResponse_msg().equals("Prmotion date mismatch") ||
                                objinstantdraw.getResponse().getResponse_msg().equals("MZCoupon not available") ||
                                objinstantdraw.getResponse().getResponse_msg().equals("Promo code wrong")) {
                            daodrag.deletepromocode(res.getPromoid());
                            objdraw = null;
                            CommonUtils.showMessage(objinstantdraw.getResponse().getResponse_code(), view, context);

                        }
                    } else {
                        objinstantdraw = serverdrag.getinstantdrawdetail(res.getCode());
                        if (objinstantdraw.getResponse().getResponse_code().equals("0")) {
                            objdraw.setTitle("Instantdraw Promotion");
                            objdraw.setName(objinstantdraw.getResult().getName());
                            objdraw.setDesc(objinstantdraw.getResult().getDescription());
                            objdraw.setImage(objinstantdraw.getResult().getImagePath());
                        } else {
                            if (objinstantdraw != null)
                                CommonUtils.showMessage(objinstantdraw.getResponse().getResponse_code(), view, context);
                        }
                    }
                } else {
                    objinstantdraw = serverdrag.getinstantdrawdetail(res.getCode());
                    if (objinstantdraw.getResponse().getResponse_code().equals("0")) {
                        objdraw.setTitle("Instantdraw Promotion");
                        objdraw.setName(objinstantdraw.getResult().getName());
                        objdraw.setDesc(objinstantdraw.getResult().getDescription());
                        objdraw.setImage(objinstantdraw.getResult().getImagePath());
                    } else {
                        if (objinstantdraw != null)
                            CommonUtils.showMessage(objinstantdraw.getResponse().getResponse_code(), view, context);
                    }
                }*/
            } else if (res.getType().equals("L")) {

//                if (user != null && user.getCustomer_email() != null) {
//
//                    objinstantdraw = serverdrag.getluckydraw(res.getPromoid(), user.getCustomer_email());
//
//                    if (objinstantdraw != null && objinstantdraw.getResponse().getResponse_code().equals("1")) {
//                        if (objinstantdraw.getResponse().getResponse_msg().equals("You already participate.")) {
//                            objinstantdraw = serverdrag.getLuckydrawdetail(res.getCode());
//                            if (objinstantdraw.getResponse().getResponse_code().equals("0")) {
//                                objdraw.setTitle("Luckydraw Promotion");
//                                objdraw.setName(objinstantdraw.getResult().getName());
//                                objdraw.setDesc(objinstantdraw.getResult().getDescription());
//                                objdraw.setImage(objinstantdraw.getResult().getImagePath());
//                            } else {
//                                if (objinstantdraw != null)
//                                    CommonUtils.showMessage(objinstantdraw.getResponse().getResponse_code(), view, context);
//                            }
//                        } else if (objinstantdraw.getResponse().getResponse_msg().equals("Prmotion date mismatch") ||
//                                objinstantdraw.getResponse().getResponse_msg().equals("Promo code wrong")) {
//                            daodrag.deletepromocode(res.getPromoid());
//                            objdraw = null;
//                            CommonUtils.showMessage(objinstantdraw.getResponse().getResponse_code(), view, context);
//                        }
//                    } else {
//                        objinstantdraw = serverdrag.getLuckydrawdetail(res.getCode());
//                        if (objinstantdraw.getResponse().getResponse_code().equals("0")) {
//                            objdraw.setTitle("Luckydraw Promotion");
//                            objdraw.setName(objinstantdraw.getResult().getName());
//                            objdraw.setDesc(objinstantdraw.getResult().getDescription());
//                            objdraw.setImage(objinstantdraw.getResult().getImagePath());
//
//                        } else {
//                            if (objinstantdraw != null)
//                                CommonUtils.showMessage(objinstantdraw.getResponse().getResponse_code(), view, context);
//                        }
//                    }
//                } else {
//                    CommonUtils.Snackbar(view, context.getString(R.string.please_signin));
//                }
//            }
//
//        if (objdraw != null && objdraw.getTitle() != null)
//            daodrag.addPromo(objdraw);
            }

        return objdraw;
    }


//    public ArrayList<DrawRes> getpromocode() {
//
//        ArrayList<DrawRes> resList = daodrag.getPromocodeList();
//
//        return resList;
//
//    }

    public DrawRes getBeaconOnshake(List<Beacon> beaconList, View view) {

//        ArrayList<BeaconRes> beacon_List = new ArrayList<>();
//        BaconServer server = new BaconServer();
        DrawRes objdraw = null;
//
//        for (Beacon beacon : beaconList) {
//            if (beacon != null) {
//                BeaconRes res = new BeaconRes();
//                res.setDeviceToken(beacon.getProximityUUID() + ":" + beacon.getMajor() + ":" + beacon.getMinor());
//                beacon_List.add(res);
//            }
//        }
//        server.setDevices(beacon_List);
//        PromotionServer promotion = serverdrag.postBeaconList(server);
//        if (promotion != null && promotion.getPromotion().getPromotiontag() != null) {
//            objdraw = getluckyDraw(view, promotion.getPromotion().getPromotiontag());
////            if (objdraw != null && objdraw.getTitle() != null)
////                daodrag.addPromo(objdraw);
//        }
        return objdraw;
    }


}

