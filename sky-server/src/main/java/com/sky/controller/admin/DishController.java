package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @Author:杰杰睡不醒
 * @Date:2024/7/26 17:38
 * @Description:菜品管理
 **/
@RestController
@Slf4j
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    //新增菜品
    @ApiOperation("新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){//封装json格式的数据
        log.info("新增菜品:{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        //请理缓存数据
        redisTemplate.delete("dish_"+dishDTO.getCategoryId());
        return Result.success();
    }
    //菜品分页查询
    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询:{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }
    //批量删除菜品
    @ApiOperation("批量删除菜品")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){//mvc框架 自动解析1，2，3 把 id提取出来 放进集合中
        log.info("批量删除菜品:{}",ids);
        dishService.deleteBatch(ids);
//        //将所有菜品缓存数据请理
//        Set keys=redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);
        cleanCache("dish_*");
        return Result.success();
    }
    //根据id查询菜品
    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品:{}",id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    //修改菜品
    @ApiOperation("修改菜品")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品:{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);
        //将所有菜品缓存数据请理
        cleanCache("dish_*");
        return Result.success();
    }
    //起售停售
    @ApiOperation("启用禁用菜品")
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("启用禁用菜品:{},{}",status,id);
        dishService.startOrStop(status,id);
        //将所有菜品缓存数据请理
        Set keys=redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return Result.success();
    }
    //根据套餐分类查询菜品
    @ApiOperation("根据套餐分类id查询菜品")
    @GetMapping("/list")
    public Result<List<Dish>> list(Long categoryId){
        log.info("根据套餐分类查询菜品:{}",categoryId);
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }
      /*
      * 清理缓存数据
      *
      * */
    private void cleanCache(String pattern){
        Set keys=redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
