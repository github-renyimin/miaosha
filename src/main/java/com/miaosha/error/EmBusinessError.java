package com.miaosha.error;

/**
 * @author yiminren
 */
public enum EmBusinessError implements CommonError {

    /**
     * 1开头，通用错误信息
     */
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),
    UNKNOWN_ERROR(10002, "未知异常"),

    /**
     * 2开头，用户信息相关错误定义
     */
    USER_NOT_EXIST(20001, "用户不存在"),
    USER_LOGIN_FAIL(20002, "手机号或密码不正确"),
    USER_NOT_LOGIN(20003, "用户还未登陆"),
    /**
     * 3开头，交易信息相关错误定义
     */
    STOCK_NOT_ENOUGH(30001, "库存数量不足"),
    ;

    EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
