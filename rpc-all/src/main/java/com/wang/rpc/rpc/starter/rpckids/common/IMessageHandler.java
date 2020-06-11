package com.wang.rpc.rpc.starter.rpckids.common;

import io.netty.channel.ChannelHandlerContext;

@FunctionalInterface
public interface IMessageHandler<T> {

	void handle(ChannelHandlerContext ctx, String requestId, T message);

}
