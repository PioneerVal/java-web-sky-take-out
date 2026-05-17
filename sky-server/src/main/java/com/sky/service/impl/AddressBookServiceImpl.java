package com.sky.service.impl;

import com.sky.constant.IsDefaultConstant;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;


    @Override
    public List<AddressBook> list(Long userId) {
        return addressBookMapper.selectAllByUserId(userId);
    }

    @Override
    public void addAddressBook(AddressBook addressBook) {
        addressBookMapper.insert(addressBook);
    }

    @Override
    @Transactional
    public void setDefault(AddressBook addressBook) {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        //1.将当前用户的所有地址修改为非默认
        addressBookMapper.setIsNotDefault(userId);
        //2.将当前地址修改为默认
        if(addressBook.getId() == null){
            //若传来的id为null，则抛出业务异常
            log.info("地址不存在");
            throw new AddressBookBusinessException("地址不存在");
        }
        addressBook.setUserId(userId);
        addressBook.setIsDefault(IsDefaultConstant.DEFAULT_YES);
        addressBookMapper.updateById(addressBook);

    }

    @Override
    public AddressBook getAddressBookById(Long id) {

        return addressBookMapper.selectAddressBookById(id);
    }

    @Override
    public void removeAddressById(Long id) {
        addressBookMapper.deleteById(id);
    }

    @Override
    public void updateById(AddressBook addressBook) {
        addressBookMapper.updateById(addressBook);
    }
}

