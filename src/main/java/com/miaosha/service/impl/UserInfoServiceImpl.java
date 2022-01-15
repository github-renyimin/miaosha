package com.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miaosha.entity.UserInfo;
import com.miaosha.entity.UserPassword;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.mapper.UserInfoMapper;
import com.miaosha.mapper.UserPasswordMapper;
import com.miaosha.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miaosha.service.model.UserModel;
import com.miaosha.validator.ValidationResult;
import com.miaosha.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yuwen
 * @since 2021-03-10
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserPasswordMapper userPasswordMapper;

    @Resource
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        UserInfo userInfo = userInfoMapper.selectById(id);
        if (userInfo == null) {
            // 用户信息不存在
            return null;
        }

        QueryWrapper<UserPassword> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        UserPassword userPassword = userPasswordMapper.selectOne(queryWrapper);
        return convertFromDO(userInfo, userPassword);
    }

    @Override
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        // 校验参数
//        if (StringUtils.isEmpty(userInfoModel.getName())
//                || userInfoModel.getGender() == null
//                || userInfoModel.getAge() == null
//                || StringUtils.isEmpty((userInfoModel.getTelphone()))) {
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
        ValidationResult validate = validator.validate(userModel);
        if (validate.hasErrors) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, validate.getErrorMsg());
        }

        // model -> dataobject
        UserInfo userInfo = convertFromModel(userModel);
        try {
            userInfoMapper.insert(userInfo);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号已存在");
        }
        userModel.setId(userInfo.getId());

        UserPassword userPassword = convertPasswrodFromModel(userModel);
        userPasswordMapper.insert(userPassword);

    }

    @Override
    public UserModel validateLogin(String telephone, String password) throws BusinessException {
        UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().lambda().eq(UserInfo::getTelphone, telephone));
        if (userInfo == null) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPassword userPassword = userPasswordMapper.selectOne(new QueryWrapper<UserPassword>().lambda().eq(UserPassword::getUserId, userInfo.getId()));
        UserModel userModel = convertFromDO(userInfo, userPassword);

        if (!StringUtils.equals(userModel.getEncrptPassword(), password)) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }

        return userModel;
    }

    private UserInfo convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userModel, userInfo);

        return userInfo;
    }

    private UserPassword convertPasswrodFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserPassword userPassword = new UserPassword();
        userPassword.setEncrptPassword(userModel.getEncrptPassword());
        userPassword.setUserId(userModel.getId());
        return userPassword;
    }

    private UserModel convertFromDO(UserInfo userInfo, UserPassword userPassword) {
        if (userInfo == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userInfo, userModel);

        if (userPassword != null) {
            userModel.setEncrptPassword(userPassword.getEncrptPassword());
        }
        return userModel;
    }
}
