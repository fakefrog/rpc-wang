package com.wang.starter.rpc;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * <p>Package:com.wang.starter.rpc</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 3:12
 */
@Component
public class TestBean implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(12345);
    }

}
