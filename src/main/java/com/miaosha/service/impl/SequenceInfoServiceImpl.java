package com.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miaosha.entity.SequenceInfo;
import com.miaosha.mapper.SequenceInfoMapper;
import com.miaosha.service.SequenceInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yeme
 * @since 2021-03-14
 */
@Service
public class SequenceInfoServiceImpl extends ServiceImpl<SequenceInfoMapper, SequenceInfo> implements SequenceInfoService {

    @Resource
    private SequenceInfoMapper sequenceInfoMapper;

    @Override
    public SequenceInfo selectSequenceByName(String name) {
        return sequenceInfoMapper.selectOne(new QueryWrapper<SequenceInfo>().lambda().eq(SequenceInfo::getName, name));
    }
}
