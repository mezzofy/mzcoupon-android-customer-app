package com.mezzofy.mzcoupon.pojo;

/**
 * Created by LENOVO on 15/07/2015.
 */
public class PaypalRes {

    private String state;
    private String id;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    //    {
//        "response": {
//        "state": "approved",
//                "id": "PAY-8JW37698KA659514CKWTDHVY",
//                "create_time": "2015-07-15T10:20:07Z",
//                "intent": "sale"
//    },
//        "client": {
//        "platform": "Android",
//                "paypal_sdk_version": "2.9.6",
//                "product_name": "PayPal-Android-SDK",
//                "environment": "sandbox"
//    },
//        "response_type": "payment"
//    }
}
