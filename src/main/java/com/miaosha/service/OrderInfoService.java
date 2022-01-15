package com.miaosha.service;

import com.miaosha.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.miaosha.error.BusinessException;
import com.miaosha.service.model.OrderInfoModel;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yeme
 * @since 2021-03-14
 */
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 创建订单
     * @param userId
     * @param itemId
     * @param amount
     * @param promoId
     * @return
     */
    OrderInfoModel createOrder(Integer userId, Integer itemId, Integer amount, Integer promoId) throws BusinessException;

}
