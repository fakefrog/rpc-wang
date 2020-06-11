package com.wang.rpc.rpc.starter.rpckids.server;

import com.wang.rpc.rpc.starter.rpckids.common.IMessageHandler;
import com.wang.rpc.rpc.starter.rpckids.common.MessageInput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;

public class DefaultHandler implements IMessageHandler<MessageInput> {

	private final static Logger LOG = LoggerFactory.getLogger(DefaultHandler.class);

	@Override
	public void handle(ChannelHandlerContext ctx, String requesetId, MessageInput input) {
		LOG.error("unrecognized message type {} comes", input.getType());
		ctx.close();
	}

}
