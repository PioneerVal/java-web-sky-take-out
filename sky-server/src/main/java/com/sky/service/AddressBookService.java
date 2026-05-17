package com.sky.service;

import com.sky.entity.AddressBook;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AddressBookService {

    /**
     * 查询当前用户的收货地址列表
     * @return List<AddressBook>
     */
    List<AddressBook> list(Long userId);

    /**
     * 新增地址
     * @param addressBook
     */
    void addAddressBook(AddressBook addressBook);

    /**
     * 设置默认地址
     * @param id
     */
    void setDefault(AddressBook addressBook);

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    AddressBook getAddressBookById(Long id);

    /**
     * 删除地址
     * @param id
     */
    void removeAddressById(Long id);

    /**
     * 修改地址
     * @param addressBook
     */
    void updateById(AddressBook addressBook);
}