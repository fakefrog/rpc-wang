package com.wang.starter.rpc.rpckids.common.rpc;

import java.util.HashMap;
import java.util.Map;

public class RpcRegistry {

    private Map<Class<?>, Object> registryClasses = new HashMap<>();

    public void register(Class<?> type, Object object) {
        registryClasses.put(type, object);
    }

    public Object get(Class<?> type) {
        return registryClasses.get(type);
    }

}
