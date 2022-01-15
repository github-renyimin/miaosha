package com.miaosha.service;

import com.miaosha.entity.SequenceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yeme
 * @since 2021-03-14
 */
public interface SequenceInfoService extends IService<SequenceInfo> {

    SequenceInfo selectSequenceByName(String name);

}
