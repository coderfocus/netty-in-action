package com.coderfocus.service;

import com.coderfocus.deco.*;
import com.coderfocus.deco.server.RequestMessageDecoder;
import com.coderfocus.deco.server.ResponseMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.logging.Level;

public class EchoService {
    public static void main(String[] args) throws Exception {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        serverBootstrap.group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .localAddress(6667)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new OrderFrameDecoder());
                        socketChannel.pipeline().addLast(new OrderFrameEncoder());

                        socketChannel.pipeline().addLast(new ResponseMessageEncoder());
                        socketChannel.pipeline().addLast(new RequestMessageDecoder());

                        socketChannel.pipeline().addLast(new RequestMessageHandler());
                    }
                });

        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        channelFuture.channel().closeFuture().sync();
    }
}
