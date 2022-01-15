package com.miaosha.service.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yiminren
 * 交易模型
 */
@Data
public class OrderInfoModel {

    /**
     * 订单id
     */
    private String id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 商品id
     */
    private Integer itemId;

    /**
     * 商品购买单价
     */
    private BigDecimal itemPrice;

    /**
     * 购买数量
     */
    private Integer amount;

    /**
     * 订单金额
     */
    private BigDecimal orderPrice;

    /**
     * 活动id
     */
    private Integer promoId;



}
