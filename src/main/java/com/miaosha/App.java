package com.miaosha;

import com.miaosha.controller.viewobject.UserInfoVO;
import com.miaosha.entity.UserInfo;
import com.miaosha.mapper.UserInfoMapper;
import com.miaosha.service.UserInfoService;
import com.miaosha.service.model.UserModel;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Hello world!
 */
@SpringBootApplication(scanBasePackages = "com")
@RestController
@MapperScan("com.miaosha.mapper")
public class App {

    @Resource
    private UserInfoMapper userInfoMapper;

    @RequestMapping("/")
    public String home() {
//        return "Hello World!";
        UserInfo userInfo = userInfoMapper.selectById(1);
        if (userInfo == null) {
            return "用户对象不存在";
        } else {
            return userInfo.getName();
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
        SpringApplication.run(App.class);
    }

    @Resource
    private UserInfoService userInfoService;

    @RequestMapping("/get")
    @ResponseBody
    public UserInfoVO getUser(@RequestParam("id") Integer id) {
        UserModel userModel = userInfoService.getUserById(id);
        return convertFromModel(userModel);
    }

    private UserInfoVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }

        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userModel, userInfoVO);
        return userInfoVO;
    }


}
