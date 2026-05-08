package com.sky.service.impl;

import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

