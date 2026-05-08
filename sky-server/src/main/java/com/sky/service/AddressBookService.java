package com.sky.service;

import com.sky.entity.AddressBook;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AddressBookService {

    //根据id查询收货地址
    List<AddressBook> list(Long userId);

    /**
     * 新增地址
     * @param addressBook
     */
    void addAddressBook(AddressBook addressBook);
}