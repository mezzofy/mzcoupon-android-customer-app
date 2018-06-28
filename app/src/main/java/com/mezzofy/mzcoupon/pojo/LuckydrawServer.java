package com.mezzofy.mzcoupon.pojo;

import java.util.ArrayList;

/**
 * Created by udhayinforios on 1/3/16.
 */
public class LuckydrawServer {

    private ResultRes response;
    private ArrayList<LuckyDrawRes> result;

    public ResultRes getResponse() {
        return response;
    }

    public void setResponse(ResultRes response) {
        this.response = response;
    }

    public ArrayList<LuckyDrawRes> getResult() {
        return result;
    }

    public void setResult(ArrayList<LuckyDrawRes> result) {
        this.result = result;
    }
}
