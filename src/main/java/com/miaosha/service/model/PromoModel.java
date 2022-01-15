package com.miaosha.service.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yeme
 * @since 2021-03-15
 */
@Data
public class PromoModel {

    /**
     * 活动id
     */
    private Integer id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 商品id
     */
    private Integer itemId;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 秒杀金额
     */
    private BigDecimal promoItemPrice;

    /**
     * 活动状态
     * 1：未开始 2：进行中 3：已经结束
     */
    private Integer status;


}
