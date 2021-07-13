package com.coderfocus.client;

import com.coderfocus.service.EchoServiceHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {
    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .remoteAddress("127.0.0.1",6666)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new EchoClientHandler());
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect().sync();
        channelFuture.channel().closeFuture().sync();
    }
}
