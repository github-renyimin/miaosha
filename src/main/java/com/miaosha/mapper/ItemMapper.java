package com.miaosha.mapper;

import com.miaosha.entity.Item;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yeme
 * @since 2021-03-13
 */
public interface ItemMapper extends BaseMapper<Item> {

    void increaseSales(Integer id, Integer amount);
}
