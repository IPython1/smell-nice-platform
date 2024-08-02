package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author:杰杰睡不醒
 * @Date:2024/7/30 18:40
 * @Description:
 **/
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    //添加购物车
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //当前添加到购物车中的商品是否已经存在了
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        //已经存在的话 只需要更新number数量即可
        if (list != null && list.size() > 0){
            ShoppingCart cart = list.get(0);
            //update shopping_cart set number = ? where id = ?
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        }else{
            //如果不存在 需要插入一条购物车数据
            //判断插入的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null){
                //本次添加到购物车的是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());

            }else{
                //本次添加到购物车的是套餐
                Long setmealId = shoppingCartDTO.getSetmealId();

                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());

            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            //插入数据
            shoppingCartMapper.insert(shoppingCart);

        }


    }
    //查看购物车
    @Override
    public List<ShoppingCart> showShoppingCart() {
        //获取当前微信用户的id
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart =  ShoppingCart.builder()
                .userId(userId)
                .build();
        List<ShoppingCart> list=shoppingCartMapper.list(shoppingCart);
        return list;
    }
    //清空购物车
    @Override
    public void cleanShoppingCart() {
        //获取当前微信用户的id
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //获取当前微信用户的id
        Long userId = BaseContext.getCurrentId();
        //判断删除的这个商品是菜品还是套餐
        Long dishId = shoppingCartDTO.getDishId();
        if (dishId != null){
            //本次删除的这个商品是菜品 或者删除的仅仅是菜品中的口味？？！！！ 前端压根不能仅仅删除口味 所以我们直接将菜品删除即可
            //根据用户的id和菜品的id  直接删除购物车中的该条数据
            shoppingCartMapper.deleteByUserDishId(userId,dishId);
        }else{
            //本次删除的这个商品的是套餐
            Long setmealId = shoppingCartDTO.getSetmealId();
            shoppingCartMapper.deleteByUserSetmealId(userId,setmealId);
        }


    }
}
