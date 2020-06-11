package com.wang.rpc.rpc.starter;

import com.wang.rpc.rpc.starter.rpckids.server.RPCServer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * <p>Package:com.wang.rpc.rpc.starter</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 0:07
 */
@Configuration
@EnableConfigurationProperties(RpcProperties.class)
public class RpcConfiguration {

    @Resource
    private RpcProperties rpcProperties;

    @Bean
    public RPCServer helloService() {
        RPCServer rpcServer = new RPCServer(rpcProperties.getIp(), rpcProperties.getPort(),
                rpcProperties.getIoThreads(), rpcProperties.getWorkerThreads());
        return rpcServer;
    }

}
