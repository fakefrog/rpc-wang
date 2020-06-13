package com.wang.starter.rpc.rpckids.server;

import com.wang.starter.rpc.rpckids.common.IMessageHandler;
import com.wang.starter.rpc.rpckids.common.MessageHandlers;
import com.wang.starter.rpc.rpckids.common.MessageInput;
import com.wang.starter.rpc.rpckids.common.MessageRegistry;
import com.wang.starter.rpc.rpckids.common.rpc.RpcInvocation;
import com.wang.starter.rpc.rpckids.common.rpc.RpcRegistry;
import com.wang.starter.rpc.rpckids.common.rpc.RpcResult;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Sharable
@Slf4j
public class ServerMessageCollector extends ChannelInboundHandlerAdapter {

    private ThreadPoolExecutor executor;

    /**
     * 可以做安全控制
     */
    private MessageHandlers handlers = new MessageHandlers();

    private MessageRegistry registry = new MessageRegistry();

    private RpcRegistry rpcRegistry = new RpcRegistry();

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public ServerMessageCollector(ThreadPoolExecutor executor) {
        this.executor = executor;
        handlers.defaultHandler(new DefaultHandler());
    }

    public ServerMessageCollector register(String type, Class<?> reqClass, IMessageHandler<?> handler) {
        try {
            readWriteLock.writeLock().lock();
            registry.register(type, reqClass);
            handlers.register(type, handler);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return this;
    }

    public ServerMessageCollector registerBean(Object bean) {
        Class<?> ainterface = null;
        try {
            ainterface = bean.getClass().getInterfaces()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        rpcRegistry.register(ainterface, bean);
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
        log.debug("connection comes");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("connection leaves");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof MessageInput) {
            this.executor.execute(() -> this.handleMessage(ctx, (MessageInput) msg));
        } else if (msg instanceof RpcInvocation) {
            this.executor.execute(() -> this.handleRpcMessage(ctx, (RpcInvocation) msg));
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

    private void handleRpcMessage(ChannelHandlerContext ctx, RpcInvocation rpcInvocation) {
        try {
            readWriteLock.readLock().lock();
            // 业务逻辑在这里
            Object object = rpcRegistry.get(rpcInvocation.getInvokeInterface());
            if (object == null) {
                //报错
                //因为没有数据
                ctx.close();
                return;
            }
            try {
                Object result = rpcInvocation.getMethod().invoke(object, rpcInvocation.getArgs());
                RpcResult rpcResult = new RpcResult();
                rpcResult.setResult(result);
                rpcResult.setRequestId(rpcInvocation.getRequestId());
                rpcResult.setAttachments(rpcInvocation.getAttachments());
                ctx.writeAndFlush(rpcResult);
            } catch (Exception e) {
                log.error("error", e);
                ctx.close();
            }
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn("connection error", cause);
    }

}
