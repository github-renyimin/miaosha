package com.miaosha.service.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class UserModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 用户名称
     */
    @NotBlank(message = "用户名不能为空")
    private String name;

    /**
     * 1代表男性，2代表女性，0代表未知
     */
    @NotBlank(message = "性别不能不填写")
    private Byte gender;

    /**
     * 年龄
     */
    @NotBlank(message = "年龄不能不填写")
    @Min(value = 0, message = "年龄必须大于0岁")
    @Max(value = 150, message = "年龄必须小于150岁")
    private Integer age;

    /**
     * 手机号，作为登陆账号
     */
    @NotBlank(message = "手机号不能为空")
    private String telphone;

    /**
     * byphone, bywechat, byalipay
     */
    private String registerMode;

    /**
     * 第三方账号
     */
    private String thirdPartyId;

    /**
     * 密码(加密)
     */
    @NotBlank(message = "密码不能为空")
    private String encrptPassword;


}
