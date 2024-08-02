package com.sky.controller.user;

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
 * @Description:用户端 管理店铺状态接口
 **/
@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "管理端商铺相关接口")
@Slf4j
public class ShopController {
    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("/status")
    @ApiOperation("查询商铺营业状态")
    public Result<Integer> getStatus(){

        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取到商铺的营业状态：{}",status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }

    //
}
