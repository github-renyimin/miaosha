package com.miaosha.error;

/**
 * @author yiminren
 */
public interface CommonError {

    int getErrCode();
    String getErrMsg();
    CommonError setErrMsg(String errMsg);
}
