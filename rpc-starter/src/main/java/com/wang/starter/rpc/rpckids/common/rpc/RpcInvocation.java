package com.wang.starter.rpc.rpckids.common.rpc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * <p>Package:com.wang.starter.rpc.rpckids.common.rpc</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/13 2:55
 */
@Data
public class RpcInvocation {

    private Map<String,Object> attachMents = new HashMap<>();

    private Class<?> invokeInterface;

    private Method method;

    private Object[] args;

    private String requestId;

}
