package com.miaosha.controller.viewobject;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuwen
 * @since 2021-03-10
 */
@Data
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 1代表男性，2代表女性，0代表未知
     */
    private Byte gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 手机号，作为登陆账号
     */
    private String telphone;

}
