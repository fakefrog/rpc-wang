package com.wang.common.demo;

import com.wang.starter.rpc.rpckids.demo.ExpRequest;
import com.wang.starter.rpc.rpckids.demo.ExpResponse;

/**
 * <p>Package:com.wang.starter.service</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 2:44
 */
public interface ITestService {

    String fly();

    ExpResponse exp(ExpRequest message);


}
