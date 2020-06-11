package com.wang.rpc.rpc.starter;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Package:com.wang.rpc.rpc.starter</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 2:40
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface RpcComponent {
}
