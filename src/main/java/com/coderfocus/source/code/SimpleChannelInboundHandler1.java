package com.coderfocus.source.code;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SimpleChannelInboundHandler1 extends SimpleChannelInboundHandler<Long>  {


    private final TypeParameterMatcher matcher;

    public SimpleChannelInboundHandler1() {
        this.matcher = TypeParameterMatcher.get(Long.class);
    }


    public void test(){
              Integer i= Integer.valueOf(1);
        System.out.println(this.matcher.match(i));

        Long lon= Long.valueOf(1);
        System.out.println(this.matcher.match(lon));
    }

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
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {

    }

}
