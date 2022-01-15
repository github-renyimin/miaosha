package com.miaosha.mapper;

import com.miaosha.entity.ItemStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yeme
 * @since 2021-03-13
 */
public interface ItemStockMapper extends BaseMapper<ItemStock> {

    int decreaseStock(Integer itemId, Integer amount);

}
