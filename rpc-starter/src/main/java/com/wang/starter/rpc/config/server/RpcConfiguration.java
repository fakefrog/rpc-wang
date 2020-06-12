package com.wang.starter.rpc.config.server;

import com.wang.starter.rpc.rpckids.server.ServerMessageCollector;
import com.wang.starter.rpc.rpckids.server.RPCServer;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

/**
 * <p>Package:com.wang.starter.starter.starter</p>
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
    public RPCServer rpcServer() {
        RPCServer rpcServer = new RPCServer(rpcProperties.getIp(), rpcProperties.getPort(),
                rpcProperties.getIoThreads(), messageCollector());
        rpcServer.start();
        return rpcServer;
    }

    @Bean
    public RpcBeanPostProcessor rpcBeanPostProcessor() {
        return new RpcBeanPostProcessor(messageCollector());
    }

    @Bean
    public ServerMessageCollector messageCollector() {
        return new ServerMessageCollector(rpcThreadPoolExecutor());
    }

    @Bean
    public ThreadPoolExecutor rpcThreadPoolExecutor() {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1000);
        return new ThreadPoolExecutor(1, rpcProperties.getWorkerThreads(), 30, TimeUnit.SECONDS, queue, new RpcThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
