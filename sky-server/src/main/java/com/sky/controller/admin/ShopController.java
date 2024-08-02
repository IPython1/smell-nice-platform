package com.sky.controller.admin;

import com.sky.config.RedisConfiguration;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @Author:杰杰睡不醒
 * @Date:2024/7/28 17:38
 * @Description:
 **/
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "管理端商铺相关接口")
@Slf4j
public class ShopController {
    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;
    @PutMapping("/{status}")
    @ApiOperation("设置商铺营业状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置商铺营业状态:{}",status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(KEY,status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("查询商铺营业状态")
    public Result<Integer> getStatus(){

        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取到商铺的营业状态：{}",status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }

    //
}
