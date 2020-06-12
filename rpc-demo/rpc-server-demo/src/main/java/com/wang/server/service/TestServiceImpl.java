package com.wang.server.service;

import com.wang.common.demo.TestService;

/**
 * <p>Package:com.wang.starter.service</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 3:00
 */
//@RpcComponent(name = "testServiceImpl")
public class TestServiceImpl implements TestService {

    @Override
    public String fly() {

        return "I believe I can fly!";

    }

}
