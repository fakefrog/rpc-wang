package com.wang.server;

import com.wang.starter.rpc.EnableRpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRpc
public class RpcDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcDemoApplication.class, args);
    }

}
