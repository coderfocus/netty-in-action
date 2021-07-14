package com.coderfocus.deco.server;

import com.coderfocus.model.RequestMessage;
import com.coderfocus.model.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ResponseMessageEncoder extends MessageToMessageEncoder<ResponseMessage> {

    /**
     * Encode from one message to an other. This method will be called for each written message that can be handled
     * by this encoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToMessageEncoder} belongs to
     * @param msg the message to encode to an other one
     * @param out the {@link List} into which the encoded msg should be added
     *            needs to do some kind of aggregation
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessage msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        msg.encode(buffer);
        out.add(buffer);
    }
}
