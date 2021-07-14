package com.coderfocus.deco.client;

import com.coderfocus.model.RequestMessage;
import com.coderfocus.model.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ResponseMessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.decode(in);
        out.add(responseMessage);
    }

}
