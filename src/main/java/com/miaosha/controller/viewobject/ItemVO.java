package com.miaosha.controller.viewobject;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ItemVO {

    /**
     * 商品id
     */
    private Integer id;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品价格不能为空")
    private String title;

    /**
     * 商品价格
     */
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格必须大于0")
    private BigDecimal price;

    /**
     * 商品库存
     */
    @NotNull(message = "库存不能不填")
    private Integer stock;

    /**
     * 描述信息
     */
    @NotBlank(message = "商品描述信息不能为空")
    private String description;

    /**
     * 商品销量
     */
    private Integer sales;

    /**
     * 商品图片
     */
    @NotBlank(message = "图片信息不能为空")
    private String imgUrl;

    /**
     * 秒杀活动状态
     * 1：未开始， 2：进行中
     */
    private Integer promoStatus;

    /**
     * 秒杀价格
     */
    private BigDecimal promoPrice;

    /**
     * 秒杀活动id
     */
    private Integer promoId;

    /**
     * 秒杀开始时间
     */
    private String startTime;

}
