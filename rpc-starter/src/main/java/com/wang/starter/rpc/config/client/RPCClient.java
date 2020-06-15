package com.wang.starter.rpc.config.client;

import com.wang.starter.rpc.common.RequestId;
import com.wang.starter.rpc.common.rpc.RpcClientDecoder;
import com.wang.starter.rpc.common.rpc.RpcClientEncoder;
import com.wang.starter.rpc.common.rpc.RpcInvocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RPCClient {

    private String ip;

    private int port;

    private Bootstrap bootstrap;

    private EventLoopGroup group;

    private ClientMessageCollector collector;

    private boolean started;

    private boolean stopped;

    public RPCClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.init();
    }

    public RPCClient rpc(String type, Class<?> reqClass) {
//        registry.register(type, reqClass);
        return this;
    }

    public <T> RpcFuture<T> sendRpcInvocationAsync(RpcInvocation rpcInvocation) {
        if (!started) {
            connect();
            started = true;
        }
        rpcInvocation.setRequestId(RequestId.next());
        return collector.send(rpcInvocation);
    }


    public <T> T send(RpcInvocation rpcInvocation) {
        RpcFuture<T> future = sendRpcInvocationAsync(rpcInvocation);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RPCException(e);
        }
    }

    public void init() {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup(1);
        bootstrap.group(group);
        collector = new ClientMessageCollector(this);
        bootstrap.channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipe = ch.pipeline();
                pipe.addLast(new ReadTimeoutHandler(60));

                //inbound
                pipe.addLast(new RpcClientDecoder());

                //outbound
                pipe.addLast(new RpcClientEncoder());

                //inbound
                pipe.addLast(collector);
            }

        });
        bootstrap.option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true);
    }

    public void connect() {
        bootstrap.connect(ip, port).syncUninterruptibly();
    }

    public void reconnect() {
        if (stopped) {
            return;
        }
        bootstrap.connect(ip, port).addListener(future -> {
            if (future.isSuccess()) {
                return;
            }
            if (!stopped) {
                group.schedule(() -> {
                    reconnect();
                }, 1, TimeUnit.SECONDS);
            }
            log.error("connect {}:{} failure", ip, port, future.cause());
        });
    }

    public void close() {
        stopped = true;
        collector.close();
        group.shutdownGracefully(0, 5000, TimeUnit.SECONDS);
    }

}
