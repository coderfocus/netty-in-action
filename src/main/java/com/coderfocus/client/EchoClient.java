package com.coderfocus.client;

import com.coderfocus.deco.*;
import com.coderfocus.deco.client.RequestMessageEncoder;
import com.coderfocus.deco.client.ResponseMessageDecoder;
import com.coderfocus.deco.client.StringToRequestMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoClient {
    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .remoteAddress("127.0.0.1",6667)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new OrderFrameDecoder());
                        socketChannel.pipeline().addLast(new OrderFrameEncoder());

                        socketChannel.pipeline().addLast(new RequestMessageEncoder());
                        socketChannel.pipeline().addLast(new ResponseMessageDecoder());

                        socketChannel.pipeline().addLast(new StringToRequestMessageEncoder());
                        socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        socketChannel.pipeline().addLast(new ClientMessageHandler());
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect().sync();
        channelFuture.channel().closeFuture().sync();
    }
}
