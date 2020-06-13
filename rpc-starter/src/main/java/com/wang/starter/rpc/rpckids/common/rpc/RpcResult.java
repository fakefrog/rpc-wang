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
 * @date 2020/6/13 3:04
 */
@Data
public class RpcResult {

    private Map<String,Object> attachments = new HashMap<>();

    private String requestId;

    private Object result;

}
