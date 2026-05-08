package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
