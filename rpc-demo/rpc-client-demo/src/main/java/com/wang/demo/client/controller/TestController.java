package com.wang.demo.client.controller;

import com.wang.demo.demo.ITestService;
import com.wang.demo.demo.TestService;
import com.wang.demo.rpc.config.annotation.RpcReference;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Package:com.wang.demo.client</p>
 * <p>Description: </p>
 * <p>Company: com.2dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 10:19
 */
@RestController
public class TestController {

    @RpcReference
    private ITestService testService;

    @RequestMapping("/test")
    public String rpcFlows(){
        return "";
    }
}
