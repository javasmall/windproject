package com.bigsai.wind.controller;

import com.bigsai.wind.parse.pageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class urlController {
    private Logger log= LoggerFactory.getLogger(urlController.class);
    @Autowired(required = false)
    private pageUtils pageUtils;
    @Value("${wind.path}")
    String path;


    @GetMapping("download")
    public String download()
    {
        try {
            pageUtils.download();
            return "下载成功至"+path;
        }
        catch (Exception e)
        {
            log.info(e.toString());
            return "error";
        }
    }
    @GetMapping("analyse")
    public String analyse()
    {
        try {
            pageUtils.readFile();
            return "解析储存成功";
        }
        catch (Exception e)
        {
            log.info(e.toString());
            return "error";
        }

    }
}
