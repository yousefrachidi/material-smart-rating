package com.vimalcvs.materialrating.data;

public class FeedBack {

    String body;
    boolean isCheck = false;

    public FeedBack() {
    }

    public FeedBack(String body ) {
        this.body = body;
    }

    public FeedBack(String body, boolean isCheck) {
        this.body = body;
        this.isCheck = isCheck;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
