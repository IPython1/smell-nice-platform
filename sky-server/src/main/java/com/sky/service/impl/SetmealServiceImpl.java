package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:杰杰睡不醒
 * @Date:2024/7/27 09:39
 * @Description:
 **/
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    //新增套餐和对应的套餐包含的菜品
    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
        //新增套餐
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        //像套餐表插入一条数据
        setmealMapper.insert(setmeal);//返回主键值

        //接受insert返回的套餐主键值
        Long setmealId = setmeal.getId();

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes!=null&&setmealDishes.size()>0){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
            //向套餐菜品表插入n条数据
            setmealDishMapper.insertBatch(setmealDishes);
        }

    }
    //分页查询套餐
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        //将page 和 pagesize传入进pagehelper插件中 帮我们自动计算
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }
    //批量删除套餐
    @Override
    public void deleteBatch(List<Long> ids) {
        //判断当前套餐是否能删除
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus()== StatusConstant.ENABLE){
//                throw new RuntimeException("套餐正在售卖中，不能删除");
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        //套餐内包含未起售的商品 无法售卖

        for (Long id : ids){
            //根据套餐id批量删除套餐数据
            setmealMapper.deleteBatch(id);
            //根据套餐id批量删除套餐菜品关联数据
            setmealDishMapper.deleteBySetmealIds(id);
        }

    }
    //根据套餐id查询套餐数据
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        //查套餐表
        Setmeal setmeal = setmealMapper.getById(id);
        //查套餐菜品关联表
        List<SetmealDish> setmealDishes = setmealDishMapper.getSetmealIdsBySetmealId(id);
        //拷贝属性进vo
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Override
    public void updateWithDish(SetmealDTO setmealDTO) {
        //修改套餐表基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        //先删除原来套餐包含的菜品 再插入新的菜品 即完成了修改
        setmealDishMapper.deleteBySetmealIds(setmealDTO.getId());
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes!=null&&setmealDishes.size()>0){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealDTO.getId());
            });
            //向套餐菜品关联表中添加数据
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }
    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

}
