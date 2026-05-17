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

    /**
     * 添加地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public Result addAddressBook( @RequestBody AddressBook addressBook){
        //前端未传用户id，需要获取后添加到addressBook中
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("添加地址：{}", addressBook);
        addressBookService.addAddressBook(addressBook);
        return Result.success();
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public Result setDefault(@RequestBody AddressBook addressBook){
        log.info("设置默认地址，地址id：{}", addressBook.getId());
        addressBookService.setDefault(addressBook);
        return Result.success();
    }
    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getAddressBook (@PathVariable Long id){
        log.info("根据id查询地址，地址id：{}", id);
        AddressBook addressBook = addressBookService.getAddressBookById(id);
        return Result.success(addressBook);
    }

    /**
     * 删除地址
     * @param id
     * @return
     */
    @DeleteMapping
    public Result remove(@RequestParam Long id){
        log.info("根据id删除地址，地址id：{}", id);
        addressBookService.removeAddressById(id);
        return Result.success();
    }

    /**
     * 修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    public Result update( @RequestBody AddressBook addressBook){
        log.info("修改地址：{}", addressBook);
        addressBookService.updateById(addressBook);
        return Result.success();
    }
}
