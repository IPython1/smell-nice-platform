package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    //动态查询购物车数据
    List<ShoppingCart> list(ShoppingCart shoppingCart);
    //根据id修改商品数量
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);
    //插入购物车数据
    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor,number, amount, create_time) " +
    "values (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);
    //根据用户id删除购物车数据
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);
    //根据用户id和菜品id删除购物车数据
    @Delete("delete from shopping_cart where user_id = #{userId} and dish_id = #{dishId}")
    void deleteByUserDishId(Long userId, Long dishId);
    //根据用户id和套餐id删除购物车数据
    @Delete("delete from shopping_cart where user_id = #{userId} and setmeal_id = #{setmealId}")
    void deleteByUserSetmealId(Long userId, Long setmealId);
    /**
     * 批量插入购物车数据
     *
     * @param shoppingCartList
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
