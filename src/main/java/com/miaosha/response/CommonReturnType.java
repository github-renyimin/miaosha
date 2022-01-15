package com.miaosha.response;

import lombok.Data;

/**
 * @author yiminren
 */
@Data
public class CommonReturnType {

    private String status;
    private Object data;

    public static CommonReturnType create(Object data) {
        return CommonReturnType.create(data, "success");
    }

    public static CommonReturnType create(Object data, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(data);
        return type;
    }
}
