package com.wang.starter.rpc.config.client;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * <p>Package:com.wang.starter.rpc.config.client</p>
 * <p>Description: </p>
 * <p>Company: com.2dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/16 18:46
 */
public class RpcReferenceRegistry implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        //先试做一个TestService的
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        //设置bean class对象交给BeanFactory 进行创建，会根据beanclass进行对象的初始化
        beanDefinition.setBeanClass(RpcReferenceFactoryBean.class);
        //对象初始化赋值操作，给bean 的变量属性的赋值，要有属性的set方法才可以成功的赋值，底层是ArrayList
        beanDefinition.getPropertyValues().addPropertyValue("needProxyInterface", com.wang.common.demo.ITestService.class);
        beanDefinition.getPropertyValues().addPropertyValue("clazz", com.wang.common.demo.ITestService.class);
        //注册到bean工厂中将bean，这一步完成后Spring就可以初始化bean 对象了；
        registry.registerBeanDefinition("testService", beanDefinition);
    }


}
