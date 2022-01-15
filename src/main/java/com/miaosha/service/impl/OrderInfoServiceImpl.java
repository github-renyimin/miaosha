package com.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.miaosha.entity.OrderInfo;
import com.miaosha.entity.SequenceInfo;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.mapper.OrderInfoMapper;
import com.miaosha.service.ItemService;
import com.miaosha.service.ItemStockService;
import com.miaosha.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miaosha.service.SequenceInfoService;
import com.miaosha.service.UserInfoService;
import com.miaosha.service.model.ItemModel;
import com.miaosha.service.model.OrderInfoModel;
import com.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yeme
 * @since 2021-03-14
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private ItemService itemService;

    @Resource
    private ItemStockService itemStockService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private SequenceInfoService sequenceInfoService;

    @Override
    @Transactional
    public OrderInfoModel createOrder(Integer userId, Integer itemId, Integer amount, Integer promoId) throws BusinessException {
        // 1. 校验下单状态，下单商品是否存在，用户是否合法，购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }
        UserModel userModel = userInfoService.getUserById(userId);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
        }
        if (amount <= 0 || amount > 99) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "数量信息不正确");
        }

        // 校验活动信息
        if (promoId != null) {
            if (itemModel.getPromoModel() == null || promoId.intValue() != itemModel.getPromoModel().getId().intValue()) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动信息不正确");
            } else if (itemModel.getPromoModel().getStatus().intValue() != 2) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "活动还未开始");
            }
        }

        // 2. 落单减库存（优点是不会超卖，缺点是未支付订单占库存） or 支付减库存
        boolean affectedRow = itemService.decreaseStock(itemId, amount);
        if (!affectedRow) {
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        // 3. 订单入库
        OrderInfoModel orderInfoModel = new OrderInfoModel();
        orderInfoModel.setUserId(userId);
        orderInfoModel.setItemId(itemId);
        orderInfoModel.setAmount(amount);
        if (promoId != null) {
            orderInfoModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            orderInfoModel.setItemPrice(itemModel.getPrice());
        }
        orderInfoModel.setOrderPrice(orderInfoModel.getItemPrice().multiply(new BigDecimal(amount)));
        orderInfoModel.setPromoId(promoId);

        orderInfoModel.setId(generateOrderNo());

        OrderInfo orderInfo = convertEntityFromModel(orderInfoModel);

        orderInfoMapper.insert(orderInfo);

        // 4. 添加商品销量
        itemService.increaseSales(itemId, amount);

        return orderInfoModel;
    }

    private OrderInfo convertEntityFromModel(OrderInfoModel OrderInfoModel) {
        if (OrderInfoModel == null) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(OrderInfoModel, orderInfo);
        return orderInfo;
    }

    /**
     * 当前自增序列最大支持6位，可通过循环初始值解决
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNo() {
        // 生成订单流水号，订单号16位
        // 前8位，年月日
        // 中间6位，自增序列
        // 后2位，分库分表位（00-99）
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);

        // 中间6位为自增序列,数据库自增序列
        SequenceInfo sequenceInfo =
                sequenceInfoService.getOne(
                        new QueryWrapper<SequenceInfo>().lambda()
                                .eq(SequenceInfo::getName, "order_info").last("for update"));
        int sequence = sequenceInfo.getCurrentValue();
        sequenceInfo.setCurrentValue(sequenceInfo.getCurrentValue() + sequenceInfo.getStep());
        sequenceInfoService.update(sequenceInfo,
                new UpdateWrapper<SequenceInfo>().lambda().eq(SequenceInfo::getName, sequenceInfo.getName()));

        String seqStr = String.valueOf(sequence);
        for (int i = 0; i < 6 - seqStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequence);
        // 最后2位为分库分表位，暂时写死
        stringBuilder.append("00");

        return stringBuilder.toString();
    }
}
