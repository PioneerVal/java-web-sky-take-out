package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        log.info("微信用户登录{}",userLoginDTO.getCode());
        //调用微信服务器接口服务，获取当前用户openid
        //构建微信服务器接口的url和请求参数
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid",weChatProperties.getAppid());
        paramMap.put("secret",weChatProperties.getSecret());
        paramMap.put("js_code",userLoginDTO.getCode());
        paramMap.put("grant_type","authorization_code");
        //调用微信接口服务,获得返回结果
        String payload = HttpClientUtil.doGet(url, paramMap);
        JSONObject jsonObject = JSON.parseObject(payload);
        //获取openid
        String openid = jsonObject.getString("openid");
        //判断openid是否为空，如果为空则抛出业务异常
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //如果不为空，则判断当前用户是否为新用户
        User user = userMapper.getByOpenid(openid);
        //如果是新用户，则完成注册，操作user数据表
        if(user == null){
            //新用户，构建user对象并插入到数据库
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();

            userMapper.insert(user);
            log.info("新用户{}", user);
        }
        //返回user对象
        log.info("微信用户登录成功{}", user);

        return user;
    }
}
