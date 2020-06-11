package com.wang.starter.rpc.rpckids.server;

import com.wang.starter.rpc.rpckids.common.IMessageHandler;
import com.wang.starter.rpc.rpckids.common.MessageDecoder;
import com.wang.starter.rpc.rpckids.common.MessageEncoder;
import com.wang.starter.rpc.rpckids.common.MessageHandlers;
import com.wang.starter.rpc.rpckids.common.MessageRegistry;
//import com.wang.starter.starter.starter.rpckids.demo.ExpRequest;
//import com.wang.starter.service.ExpRequestHandler;
//import com.wang.starter.service.FibRequestHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class RPCServer {

    private final static Logger LOG = LoggerFactory.getLogger(RPCServer.class);

    private String ip;

    private int port;

    private int ioThreads;

    private EventLoopGroup group;

    private MessageCollector collector;

    private Channel serverChannel;

    public RPCServer(String ip, int port, int ioThreads, MessageCollector collector ) {
        this.ip = ip;
        this.port = port;
        this.ioThreads = ioThreads;
        this.collector = collector;
    }

    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        group = new NioEventLoopGroup(ioThreads);
        bootstrap.group(group);
        MessageEncoder encoder = new MessageEncoder();
        bootstrap.channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ChannelPipeline pipe = ch.pipeline();
                pipe.addLast(new ReadTimeoutHandler(60));
                pipe.addLast(new MessageDecoder());
                pipe.addLast(encoder);
                pipe.addLast(collector);
            }
        });
        bootstrap.option(ChannelOption.SO_BACKLOG, 100).option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.SO_KEEPALIVE, true);
        serverChannel = bootstrap.bind(this.ip, this.port).channel();
        LOG.warn("server started @ {}:{}\n", ip, port);
    }

    public void stop() {
        // 先关闭服务端套件字
        serverChannel.close();
        // 再斩断消息来源，停止io线程池
        group.shutdownGracefully();
        // 最后停止业务线程
        collector.closeGracefully();
    }


/*    public static void main(String[] args) {
        RPCServer server = new RPCServer("localhost", 8888, 2, 16);
        server.service("fib", Integer.class, new FibRequestHandler()).service("exp", ExpRequest.class,
                new ExpRequestHandler());
        server.start();
    }*/
}