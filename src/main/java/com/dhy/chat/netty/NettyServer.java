package com.dhy.chat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static class SingletonNettyServer {
        static final NettyServer INSTANCE = new NettyServer();
    }

    public static NettyServer getInstance() {
        return SingletonNettyServer.INSTANCE;
    }

    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap server;
    private ChannelFuture future;

    private NettyServer() {
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettyServerInitializer());
    }

    public void start(int port) {
        this.future = server.bind(port);
        log.info("netty websocket server on port {} 启动完成...", port);
    }
}
