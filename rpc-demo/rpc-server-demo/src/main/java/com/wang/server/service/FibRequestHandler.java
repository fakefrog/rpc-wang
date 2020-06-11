package com.wang.server.service;

import com.wang.starter.rpc.RpcComponent;
import com.wang.starter.rpc.rpckids.common.IMessageHandler;
import com.wang.starter.rpc.rpckids.common.MessageOutput;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;

/**
 * <p>Package:com.wang.starter.starter.starter.rpckids.demo</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 2:35
 */
@RpcComponent(name = "fib", requestType = Integer.class)
public class FibRequestHandler implements IMessageHandler<Integer> {

    private List<Long> fibs = new ArrayList<>();

    {
        fibs.add(1L); // fib(0) = 1
        fibs.add(1L); // fib(1) = 1
    }

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, Integer n) {
        for (int i = fibs.size(); i < n + 1; i++) {
            long value = fibs.get(i - 2) + fibs.get(i - 1);
            fibs.add(value);
        }
        ctx.writeAndFlush(new MessageOutput(requestId, "fib_res", fibs.get(n)));
    }

}
