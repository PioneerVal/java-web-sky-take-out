package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user/addressBook")
@Slf4j
public class addressBookController {
    @Autowired
    private AddressBookService addressBookService;
    /**
     * 查询当前用户的收货地址列表
     * @return
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list(){
        //获取当前登录用户的id
        Long userId = BaseContext.getCurrentId();
        log.info("获取当前用户的收货地址，当前用户id：{}", userId);
        List<AddressBook> addressBooks = addressBookService.list(userId);
        return Result.success(addressBooks);
    }

    @PostMapping
    public Result addAddressBook( @RequestBody AddressBook addressBook){
        //前端未传用户id，需要获取后添加到addressBook中
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("添加地址：{}", addressBook);
        addressBookService.addAddressBook(addressBook);
        return Result.success();
    }
}
