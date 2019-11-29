package com.learn.page.util;


import java.util.HashMap;

public class ResponseUtil extends HashMap {

    public static  String SUCCESS_CODE="200";
    public static String ERROR_CODE="500";
    public static String DATA_KEY = "data";
    public static String MSG_KEY = "msg";

    private ResponseUtil(){

    }

    public ResponseUtil set(String key, Object object){
        super.put(key,object);
        return  this;
    }

    private  static ResponseUtil ok(){
        return new ResponseUtil();
    }

    public static ResponseUtil success(){

        return ResponseUtil.ok().set("code", ResponseUtil.SUCCESS_CODE).set(ResponseUtil.MSG_KEY,"操作成功");
    }

    public static ResponseUtil success(String msg){

        return ResponseUtil.ok().set("code", ResponseUtil.SUCCESS_CODE).set(ResponseUtil.MSG_KEY,msg);
    }

    public static ResponseUtil success(String msg, Object object){

        return ResponseUtil.ok().set("code", ResponseUtil.SUCCESS_CODE).set(ResponseUtil.MSG_KEY,msg).set(ResponseUtil.DATA_KEY,object);
    }

    public ResponseUtil data(Object obj){
        return this.set("data",obj);
    }

    public static ResponseUtil error(){
        return ResponseUtil.ok().set(ResponseUtil.MSG_KEY,"操作失败").set("code", ResponseUtil.ERROR_CODE);
    }

    public static ResponseUtil error(String msg){
        return ResponseUtil.ok().set(ResponseUtil.MSG_KEY,msg).set("code", ResponseUtil.ERROR_CODE);
    }

    public static ResponseUtil error(String msg, Object object){
        return ResponseUtil.ok().set(ResponseUtil.MSG_KEY,msg).set(ResponseUtil.DATA_KEY,object).set("code", ResponseUtil.ERROR_CODE);
    }
}
