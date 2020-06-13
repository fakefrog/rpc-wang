package com.wang.starter.rpc.rpckids.common.rpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcRegistry {

    private Map<Class<?>, Object> registryClasses = new ConcurrentHashMap<>();

    public void register(Class<?> type, Object object) {
        registryClasses.put(type, object);
    }

    public Object get(Class<?> type) {
        return registryClasses.get(type);
    }

}
