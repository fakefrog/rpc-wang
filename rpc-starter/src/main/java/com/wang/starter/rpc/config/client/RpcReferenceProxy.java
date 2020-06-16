package com.wang.starter.rpc.config.client;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p>Package:com.wang.starter.rpc.config.client</p>
 * <p>Description: </p>
 * <p>Company: com.2dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/16 18:53
 */
public class RpcReferenceProxy implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -4467164789570764661L;

    @SuppressWarnings("unchecked")
    public <T> T newProxy(Class<T> myInterfaces) {
        ClassLoader classLoader = myInterfaces.getClassLoader();
        Class<?>[] interfaces = new Class[]{myInterfaces};
        //JdkProxy proxy =new JdkProxy();
        return (T) Proxy.newProxyInstance(classLoader, interfaces, this);

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return "123";
    }

}
