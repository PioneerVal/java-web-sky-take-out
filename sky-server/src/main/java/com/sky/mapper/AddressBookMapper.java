package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    // 根据用户id查询所有地址
    @Select("select * from address_book where user_id = #{userID}")
    List<AddressBook> selectAllByUserId(Long userId);

    //新增地址
    @Select("insert into address_book (user_id, consignee, phone, sex, province_code, province_name, city_code, city_name, district_code, district_name, detail, label)" +
            " values (#{userId}, #{consignee}, #{phone}, #{sex}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}, #{districtCode},#{districtName},#{detail},#{label})")
    void insert(AddressBook addressBook);

    //根据用户id将所有地址设置为非默认
    @Update("update address_book set is_default = 0 where user_id = #{userId}")
    void setIsNotDefault(Long userId);

    //根据id将地址设置为默认
    @Update("update address_book set is_default = 1 where id = #{id}")
    void setDefaultAddress(Long id);

    //根据id查询地址
    @Select("select * from address_book where id = #{id}")
    AddressBook selectAddressBookById(Long id);

    //根据id删除地址
    @Update("delete from address_book where id = #{id}")
    void deleteById(Long id);

    //根据id修改地址
    void updateById(AddressBook addressBook);
}
