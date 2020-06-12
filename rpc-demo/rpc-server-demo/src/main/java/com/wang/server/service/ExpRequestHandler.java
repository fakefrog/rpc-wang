package com.wang.server.service;

import com.wang.starter.rpc.config.annotation.RpcComponent;
import com.wang.starter.rpc.rpckids.common.IMessageHandler;
import com.wang.starter.rpc.rpckids.common.MessageOutput;
import com.wang.starter.rpc.rpckids.demo.ExpRequest;
import com.wang.starter.rpc.rpckids.demo.ExpResponse;

import io.netty.channel.ChannelHandlerContext;

/**
 * <p>Package:com.wang.starter.starter.starter.rpckids.demo</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 2:35
 */
@RpcComponent(name = "exp",requestType = ExpRequest.class)
public class ExpRequestHandler implements IMessageHandler<ExpRequest> {

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, ExpRequest message) {
        int base = message.getBase();
        int exp = message.getExp();
        long start = System.nanoTime();
        long res = 1;
        for (int i = 0; i < exp; i++) {
            res *= base;
        }
        long cost = System.nanoTime() - start;
        ctx.writeAndFlush(new MessageOutput(requestId, "exp_res", new ExpResponse(res, cost)));
    }

}