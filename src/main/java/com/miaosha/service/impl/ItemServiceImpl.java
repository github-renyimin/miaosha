package com.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miaosha.entity.Item;
import com.miaosha.entity.ItemStock;
import com.miaosha.entity.Promo;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.mapper.ItemMapper;
import com.miaosha.mapper.ItemStockMapper;
import com.miaosha.service.ItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miaosha.service.PromoService;
import com.miaosha.service.model.ItemModel;
import com.miaosha.service.model.PromoModel;
import com.miaosha.validator.ValidationResult;
import com.miaosha.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yeme
 * @since 2021-03-13
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    @Resource
    private ValidatorImpl validator;

    @Resource
    private ItemMapper itemMapper;

    @Resource
    private ItemStockMapper itemStockMapper;

    @Resource
    private PromoService promoService;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        // 校验入参
        ValidationResult result = validator.validate(itemModel);
        if (result.hasErrors) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrorMsg());
        }

        // 写入数据库
        Item item = convertItemFromModel(itemModel);
        itemMapper.insert(item);

        itemModel.setId(item.getId());

        ItemStock itemStock = convertItemStockFromModel(itemModel);
        itemStockMapper.insert(itemStock);

        // 返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    private ItemStock convertItemStockFromModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemStock itemStock = new ItemStock();
        itemStock.setItemId(itemModel.getId());
        itemStock.setStock(itemModel.getStock());
        return itemStock;
    }

    private Item convertItemFromModel(ItemModel itemModel) {
        if (itemModel ==  null) {
            return null;
        }
        Item item = new Item();
        BeanUtils.copyProperties(itemModel, item);
        return item;
    }

    @Override
    public List<ItemModel> listItem() {
        List<Item> itemList = itemMapper.selectList(new QueryWrapper<Item>().lambda().orderByDesc(Item::getSales));
        List<ItemModel> itemModelList = itemList.stream().map(item -> {
            ItemStock itemStock = itemStockMapper.selectOne(new QueryWrapper<ItemStock>().lambda().eq(ItemStock::getItemId, item.getId()));
            ItemModel itemModel = convertModelFromEntity(item, itemStock);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        Item item = itemMapper.selectById(id);
        if (item == null) {
            return null;
        }
        // 获取库存信息
        ItemStock itemStock = itemStockMapper.selectOne(new QueryWrapper<ItemStock>().lambda().eq(ItemStock::getItemId, item.getId()));

        ItemModel itemModel = convertModelFromEntity(item, itemStock);

        // 获取活动商品信息
        PromoModel promoModel = promoService.getOneByItemId(id);
        if (promoModel != null && promoModel.getStatus() != 3) {
            itemModel.setPromoModel(promoModel);
        }

        return itemModel;

    }

    @Override
    @Transactional
    public void increaseSales(Integer id, Integer amount) {
        itemMapper.increaseSales(id, amount);
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) {
        int effectRows = itemStockMapper.decreaseStock(itemId, amount);
        return effectRows > 0;
    }

    public ItemModel convertModelFromEntity(Item item, ItemStock itemStock) {
        if (item == null) {
            return null;
        }
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(item, itemModel);
        itemModel.setStock(itemStock.getStock());

        return itemModel;
    }
}
