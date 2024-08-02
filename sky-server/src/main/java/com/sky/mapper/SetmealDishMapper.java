package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    //根据菜品id查找套餐id  可能查到多个
//    @Select("select setmeal_id from setmeal_dish where dish_id in (#{dishIds})")
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
    //批量插入套餐和菜品的关联关系
    void insertBatch(List<SetmealDish> setmealDishes);
    //根据套餐id删除套餐和菜品的关联关系
    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteBySetmealIds(Long id);
    //根据套餐id查询套餐和菜品的关联关系
    @Select("select * from setmeal_dish where setmeal_id = #{SetmealId}")
    List<SetmealDish> getSetmealIdsBySetmealId(Long SetmealId);

}
