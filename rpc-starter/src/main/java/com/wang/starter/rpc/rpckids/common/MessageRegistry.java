package com.wang.starter.rpc.rpckids.common;

import java.util.HashMap;
import java.util.Map;

public class MessageRegistry {

    private Map<String, Class<?>> registryClasses = new HashMap<>();

    public void register(String type, Class<?> clazz) {
        registryClasses.put(type, clazz);
    }

    public Class<?> get(String type) {
        return registryClasses.get(type);
    }

}
