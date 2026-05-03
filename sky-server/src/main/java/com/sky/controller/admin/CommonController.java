package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传:{}",file);
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //截取文件扩展名
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //拼接文件名，使用UUID生成，避免文件名重复
        String objectName = UUID.randomUUID().toString() + extension;
        log.info("文件上传路径:{}",objectName);
        try{
            //调用文件上传工具类，并获取返回文件绝对访问路径
            String fileLocation = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(fileLocation);
        }catch (IOException exception){
            log.error("文件上传失败：{}",exception.getMessage());
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);


    }
}
