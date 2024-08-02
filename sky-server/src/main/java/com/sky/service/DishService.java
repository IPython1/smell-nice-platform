package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    //新增菜品和对应的口味数据
    void saveWithFlavor(DishDTO dishDTO);

    //菜品分页查询
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
    //菜品的批量删除
    void deleteBatch(List<Long> ids);
    //根据id查询菜品和对应的口味数据
    DishVO getByIdWithFlavor(Long id);
    //根据id修改菜品基本信息和对应的口味数据
    void updateWithFlavor(DishDTO dishDTO);
    //根据套餐分类id查询菜品
    List<Dish> list(Long categoryId);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
    /**
     * 启用、禁用菜品
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
}
