package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**\
     * 根据分类id查询所关联菜品的数量
     * @param id
     * @return
     */
    @Select("select count(*) from setmeal where category_id = #{id}")
    public Long countSetmealByCategory(Long id);

    //新增套餐

    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    //分页查询
    Page<SetmealVO> selectByPage(SetmealPageQueryDTO setmealPageQueryDTO);

    //根据id查询
    @Select("select * from setmeal where id = #{id}")
    Setmeal selectById(Long id);

    //修改套餐
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    //修改套餐状态
    @Update("update setmeal set status = #{status} where id = #{id}")
    void updateStatus(Long id, Integer status);

    //根据id批量查询套餐
    List<Setmeal> selectByIds(String[] idsArray);

    //批量删除套餐
    void delete(String[] idsArray);
}
