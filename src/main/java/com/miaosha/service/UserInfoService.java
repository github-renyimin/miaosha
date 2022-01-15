package com.miaosha.service;

import com.miaosha.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.miaosha.error.BusinessException;
import com.miaosha.service.model.UserModel;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuwen
 * @since 2021-03-10
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 根据用户id获取用户信息
     * @param id
     * @return
     */
    UserModel getUserById(Integer id);

    /**
     * 注册用户
     * @param userModel
     */
    void register(UserModel userModel) throws BusinessException;

    /**
     * 用户登陆验证
     * @param telephone
     * @param password
     * @return
     */
    UserModel validateLogin(String telephone, String password) throws BusinessException;

}
