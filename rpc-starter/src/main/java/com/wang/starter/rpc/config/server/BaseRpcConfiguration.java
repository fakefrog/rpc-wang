package com.wang.starter.rpc.config.server;

import com.wang.starter.rpc.config.annotation.RpcReference;
import com.wang.starter.rpc.config.client.RpcReferenceAnnotationBeanPostProcessor;
import com.wang.starter.rpc.config.client.RpcReferenceRegistry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Package:com.wang.starter.rpc.config.server</p>
 * <p>Description: </p>
 * <p>Company: com.2dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/16 16:59
 */
@Configuration
public class BaseRpcConfiguration {

    @Bean
    public RpcReferenceAnnotationBeanPostProcessor rpcReferenceAnnotationBeanPostProcessor(){
        return new RpcReferenceAnnotationBeanPostProcessor(RpcReference.class);
    }

    @Bean
    public RpcReferenceRegistry rpcReferenceRegistry(){
        return new RpcReferenceRegistry();
    }


}
