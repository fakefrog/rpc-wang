package com.wang.starter.rpc.rpckids.server;

import com.wang.starter.rpc.rpckids.common.IMessageHandler;
import com.wang.starter.rpc.rpckids.common.MessageHandlers;
import com.wang.starter.rpc.rpckids.common.MessageInput;
import com.wang.starter.rpc.rpckids.common.MessageRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class MessageCollector extends ChannelInboundHandlerAdapter {

    private final static Logger LOG = LoggerFactory.getLogger(MessageCollector.class);

    private ThreadPoolExecutor executor;

    /**
     * 可以做安全控制
     */
    private MessageHandlers handlers = new MessageHandlers();

    private MessageRegistry registry = new MessageRegistry();

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public MessageCollector(ThreadPoolExecutor executor) {
        this.executor = executor;
        handlers.defaultHandler(new DefaultHandler());
    }

    public MessageCollector register(String type, Class<?> reqClass, IMessageHandler<?> handler) {
        try {
            readWriteLock.writeLock().lock();
            registry.register(type, reqClass);
            handlers.register(type, handler);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return this;
    }

    void closeGracefully() {
        this.executor.shutdown();
        try {
            this.executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {

        }
        this.executor.shutdownNow();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOG.debug("connection comes");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LOG.debug("connection leaves");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof MessageInput) {
            this.executor.execute(() -> this.handleMessage(ctx, (MessageInput) msg));
        }
    }

    private void handleMessage(ChannelHandlerContext ctx, MessageInput input) {
        try {
            readWriteLock.readLock().lock();
            // 业务逻辑在这里
            Class<?> clazz = registry.get(input.getType());
            if (clazz == null) {
                handlers.defaultHandler().handle(ctx, input.getRequestId(), input);
                return;
            }
            Object o = input.getPayload(clazz);
            @SuppressWarnings("unchecked")
            IMessageHandler<Object> handler = (IMessageHandler<Object>) handlers.get(input.getType());
            if (handler != null) {
                handler.handle(ctx, input.getRequestId(), o);
            } else {
                handlers.defaultHandler().handle(ctx, input.getRequestId(), input);
            }
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.warn("connection error", cause);
    }

}
