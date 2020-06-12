package com.wang.starter.rpc.config.server;

import com.wang.starter.rpc.config.annotation.RpcComponent;
import com.wang.starter.rpc.rpckids.common.IMessageHandler;
import com.wang.starter.rpc.rpckids.server.ServerMessageCollector;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;

import javax.annotation.Resource;

/**
 * <p>Package:com.wang.starter.starter.starter</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 3:08
 */
public class RpcBeanPostProcessor implements BeanPostProcessor {

    @Resource
    private ServerMessageCollector messageCollector;

    public RpcBeanPostProcessor(ServerMessageCollector messageCollector) {
        this.messageCollector = messageCollector;
    }

    @Nullable
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
//        if(isProxyBean()){
        clazz = AopUtils.getTargetClass(bean);
//        }
        RpcComponent annotation = clazz.getAnnotation(RpcComponent.class);
        if (annotation != null && bean instanceof IMessageHandler) {
            messageCollector.register(annotation.name(), annotation.requestType(), (IMessageHandler)bean);
        }
        return bean;
    }

}
