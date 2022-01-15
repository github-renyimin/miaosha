package com.miaosha.service;

import com.miaosha.entity.Promo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.miaosha.service.model.PromoModel;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yeme
 * @since 2021-03-15
 */
public interface PromoService extends IService<Promo> {

    /**
     * 获取秒杀活动信息
     * @param id
     * @return
     */
    PromoModel getOneByItemId(Integer id);
}
