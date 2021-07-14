package com.coderfocus.deco.server;

import com.coderfocus.model.RequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class RequestMessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.decode(in);
        out.add(requestMessage);
    }
}
