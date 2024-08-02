package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    //插入菜品数据
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);
    //分页查询菜品
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
    //根据id 查询菜品
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);
    //根据id删除菜品
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);
    //根据id批量删除菜品
    void deleteByIds(List<Long> ids);
    //根据id动态修改菜品
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);
    //根据分类id查询菜品
    @AutoFill(value = OperationType.INSERT)

    @Select("select * from dish where category_id = #{categoryId} and status = 1")
    List<Dish> list1(Long categoryId);
    /**
     * 动态条件查询菜品
     *
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);


}
