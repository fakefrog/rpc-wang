package com.wang.starter.rpc.config.client;

import com.wang.common.demo.domain.ExpResponse;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Package:com.wang.starter.rpc.config.client</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/16 22:45
 */
public class RpcClientRegistry {

    private static ConcurrentHashMap<String, RPCClient> rpcClientMap = new ConcurrentHashMap<>();

    public static synchronized RPCClient addRpcClient(String host, int port) {
        String key = host + "_" + port;
        RPCClient rpcClient = rpcClientMap.computeIfAbsent(key, k -> new RPCClient(host, port));
        return rpcClient;
    }

}
