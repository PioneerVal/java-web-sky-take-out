package com.sky.controller.user;

import com.sky.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class userController {

    @GetMapping
    public Result login(){

        return Result.success();
    }
}
