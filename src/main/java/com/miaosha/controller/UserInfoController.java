package com.miaosha.controller;


import com.miaosha.controller.viewobject.UserInfoVO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBusinessError;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.UserInfoService;
import com.miaosha.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Base64.Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * VO转换
 * 业务异常处理
 * 跨域处理
 *  agix请求受信
 *  allowCredentials=true，
 *  allowHeaders
 *  DEFAULT_ALLOWED_HEADERS: 允许跨域传输所有的header参数，将用于使用token放入header域做session共享的跨域请求
 *
 * </p>
 *
 * @author yeme
 * @since 2021-03-10
 */
@RestController
@RequestMapping("/user-info")
//@CrossOrigin(originPatterns = {"*"}, allowCredentials = "true", allowedHeaders = "*")
public class UserInfoController extends BaseController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private HttpServletRequest httpServletRequest;

    @Resource
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType login(@RequestParam("telephone") String telephone,
                                  @RequestParam("password") String password) throws NoSuchAlgorithmException, BusinessException {
        if (StringUtils.isEmpty(telephone) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        UserModel userModel = userInfoService.validateLogin(telephone, this.encodeByMd5(password));
        // 登陆凭证存入session
//        httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
//        httpServletRequest.getSession().setAttribute("LOGIN_USER", userInfoModel);

        // 登录凭证和登陆信息一起存入redis
        // 生成token, uuid
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(token, userModel);
        redisTemplate.expire(token, 1, TimeUnit.HOURS);

        return CommonReturnType.create(token);
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST, consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType register(@RequestParam("telephone") String telephone,
                                     @RequestParam("otpCode") String otpCode,
                                     @RequestParam("name") String name,
                                     @RequestParam("gender") Byte gender,
                                     @RequestParam("age") int age,
                                     @RequestParam("password") String password) throws BusinessException, NoSuchAlgorithmException {
        // 验证手机号和对应的otpcode是否相符
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telephone);
        if (!StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不匹配");
        }

        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelphone(telephone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.encodeByMd5(password));

        userInfoService.register(userModel);

        return CommonReturnType.create(null);

    }

    private String encodeByMd5(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        Encoder base64Encoder = Base64.getEncoder();
        // 加密字符串
        String newstr = base64Encoder.encodeToString(md5.digest(str.getBytes()));
        return newstr;
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam("id") Integer id) throws BusinessException {
        UserModel userModel = userInfoService.getUserById(id);
        if (userModel == null) {
            userModel.setEncrptPassword("123");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        UserInfoVO userInfoVO = convertFromModel(userModel);
        return CommonReturnType.create(userInfoVO);
    }

    @RequestMapping(path = "/getotp", method = RequestMethod.POST, consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam("telephone") String telephone) {
        // 需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        // 将OTP验证码同对应的手机号关联，使用session的方式绑定他的手机号与OTPCODE
        httpServletRequest.getSession().setAttribute(telephone, otpCode);

        // 模拟通过短信通道发送数据给用户
        System.out.println("telephone = " + telephone + " & otpCode = " + otpCode);

        return CommonReturnType.create(null);
    }


    /**
     * 将核心领域模型转换成VO
     *
     * @param userModel
     * @return
     */
    private UserInfoVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userModel, userInfoVO);
        return userInfoVO;
    }

}
