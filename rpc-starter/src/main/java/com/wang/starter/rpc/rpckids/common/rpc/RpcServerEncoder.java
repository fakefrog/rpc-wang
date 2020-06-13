package com.wang.starter.rpc.rpckids.common.rpc;

import com.alibaba.fastjson.JSON;
import com.wang.starter.rpc.rpckids.common.Charsets;
import com.wang.starter.rpc.rpckids.common.MessageOutput;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * <p>Package:com.wang.starter.rpc.rpckids.common.rpc</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/13 16:31
 */
public class RpcServerEncoder extends MessageToMessageEncoder<RpcResult> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcResult rpcResult, List<Object> out){
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer();
        writeStr(buf, JSON.toJSONString(rpcResult));
        out.add(buf);
    }

    private void writeStr(ByteBuf buf, String s) {
        buf.writeInt(s.length());
        buf.writeBytes(s.getBytes(Charsets.UTF8));
    }

}
