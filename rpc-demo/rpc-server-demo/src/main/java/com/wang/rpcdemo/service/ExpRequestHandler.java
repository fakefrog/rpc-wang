package com.wang.rpcdemo.service;

import com.wang.rpc.rpc.starter.RpcComponent;
import com.wang.rpc.rpc.starter.rpckids.common.IMessageHandler;
import com.wang.rpc.rpc.starter.rpckids.common.MessageOutput;
import com.wang.rpc.rpc.starter.rpckids.demo.ExpRequest;
import com.wang.rpc.rpc.starter.rpckids.demo.ExpResponse;

import io.netty.channel.ChannelHandlerContext;

/**
 * <p>Package:com.wang.rpc.rpc.starter.rpckids.demo</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 2:35
 */
@RpcComponent
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