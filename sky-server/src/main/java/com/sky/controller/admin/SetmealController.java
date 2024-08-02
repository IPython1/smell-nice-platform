package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:杰杰睡不醒
 * @Date:2024/7/27 09:37
 * @Description:套餐管理
 **/
@Api(tags = "套餐管理")
@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(value = "setmealCache", key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }
    //分页查询
    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }
    //批量删除套餐
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    @CacheEvict(value = "setmealCache", allEntries = true)
    public Result delete(@RequestParam List<Long> ids) {//mvc框架 自动解析1，2，3 把 id提取出来 放进集合中
        log.info("批量删除套餐{}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }
    //根据id查询套餐
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("根据id查询套餐{}", id);
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }
    //修改套餐
    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(value = "setmealCache", allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐{}", setmealDTO);
        setmealService.updateWithDish(setmealDTO);
        return Result.success();
    }
    //启用禁用套餐
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用套餐")
    @CacheEvict(value = "setmealCache", allEntries = true)
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id) {
        log.info("启用禁用套餐{}", status);
        setmealService.startOrStop(status, id);
        return Result.success();
    }
}
