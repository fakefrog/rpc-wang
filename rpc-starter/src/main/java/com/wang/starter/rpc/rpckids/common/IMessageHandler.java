package com.wang.starter.rpc.rpckids.common;

import io.netty.channel.ChannelHandlerContext;

@FunctionalInterface
public interface IMessageHandler<T> {

	void handle(ChannelHandlerContext ctx, String requestId, T message);

}
