package com.sky.controller.user;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/shop")
@Slf4j
public class UserShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取起售停售状态
     * @return
     */
    @GetMapping("/status")
    public Result<Integer> getStatus(){

        Integer shopStatus = (Integer)redisTemplate.opsForValue().get("SHOP_STATUS");
        if(shopStatus == null){
            //如果获取不到，则默认为打烊
            shopStatus = 0;
        }
        log.info("获取店铺状态,当前店铺：{}",shopStatus == 1 ? "营业中" : "打烊了");
        return Result.success(shopStatus);
    }
}
