package com.coderfocus.client;

import com.coderfocus.model.RequestMessage;
import com.coderfocus.model.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ClientMessageHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    @Override
    public  void channelActive(ChannelHandlerContext ctx) throws Exception {
//        RequestMessage requestMessage = new RequestMessage();
//        requestMessage.setStreamId(123);
//        requestMessage.setOpCode(456);
//        requestMessage.setData("hello1");
//        ctx.writeAndFlush(requestMessage);

        ctx.writeAndFlush("hello coderfocus");
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage responseMessage) throws Exception {
        System.out.println("client received: " + responseMessage);
    }
}
