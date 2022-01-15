package com.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miaosha.entity.Promo;
import com.miaosha.mapper.PromoMapper;
import com.miaosha.service.PromoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miaosha.service.model.PromoModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yeme
 * @since 2021-03-15
 */
@Service
public class PromoServiceImpl extends ServiceImpl<PromoMapper, Promo> implements PromoService {

    @Resource
    private PromoMapper promoMapper;

    @Override
    public PromoModel getOneByItemId(Integer itemId) {
        Promo promo = promoMapper.selectOne(new QueryWrapper<Promo>().lambda().eq(Promo::getItemId, itemId));
        PromoModel promoModel = convertModelFromEntity(promo);
        return promoModel;
    }

    private PromoModel convertModelFromEntity(Promo promo) {
        if (promo == null) {
            return null;
        }

        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promo, promoModel);
        LocalDateTime now = LocalDateTime.now();
        if (promoModel.getStartTime().isAfter(now)) {
            // 未开始
            promoModel.setStatus(1);
        } else if (promoModel.getEndTime().isBefore(now)) {
            // 已结束
            promoModel.setStatus(3);
        } else {
            promoModel.setStatus(2);
        }

        return promoModel;
    }
}
