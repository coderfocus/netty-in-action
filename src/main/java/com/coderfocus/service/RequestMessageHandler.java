package com.coderfocus.service;

import com.coderfocus.model.RequestMessage;
import com.coderfocus.model.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class RequestMessageHandler extends SimpleChannelInboundHandler<RequestMessage> {
    /**
     * <strong>Please keep in mind that this method will be renamed to
     * {@code messageReceived(ChannelHandlerContext, I)} in 5.0.</strong>
     * <p>
     * Is called for each message of type {@link I}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception is thrown if an error occurred
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage msg) throws Exception {
        System.out.println("server received: " + msg);

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setStreamId(msg.getStreamId());
        responseMessage.setOpCode(msg.getOpCode());
        responseMessage.setData("ResponseMessage");

        ctx.writeAndFlush(responseMessage);
    }
}
