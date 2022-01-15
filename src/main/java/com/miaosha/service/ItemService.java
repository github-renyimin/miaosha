package com.miaosha.service;

import com.miaosha.entity.Item;
import com.baomidou.mybatisplus.extension.service.IService;
import com.miaosha.error.BusinessException;
import com.miaosha.service.model.ItemModel;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yeme
 * @since 2021-03-13
 */
public interface ItemService extends IService<Item> {

    /**
     * 创建商品
     * @param itemModel
     * @return
     */
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    // 商品列表
    List<ItemModel> listItem();

    // 商品详情
    ItemModel getItemById(Integer id);

    /**
     * 增加销量
     * @param id
     * @param amount
     */
    void increaseSales(Integer id, Integer amount);

    /**
     * 扣减库存
     * @param itemId
     * @param amount
     * @return
     */
    boolean decreaseStock(Integer itemId, Integer amount);
}
