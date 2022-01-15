package com.miaosha.service.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ItemModel {

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
     * 聚合模型，包含未开始或进行中和活动
     */
    private PromoModel promoModel;

}
