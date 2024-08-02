package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    //新增套餐
    void saveWithDish(SetmealDTO setmealDTO);
    //分页查询所有套餐
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
    //批量删除套餐
    void deleteBatch(List<Long> ids);
    //根据id查询套餐
    SetmealVO getByIdWithDish(Long id);
    //修改套餐和套餐对应的菜品
    void updateWithDish(SetmealDTO setmealDTO);

    void startOrStop(Integer status, Long id);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

}
