package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置起售停售状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置起售停售状态:{}",status == 1 ? "营业中" : "打烊了");

        //设置状态,操作Redis数据库
        redisTemplate.opsForValue().set("SHOP_STATUS",status);
        return Result.success();
    }

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
