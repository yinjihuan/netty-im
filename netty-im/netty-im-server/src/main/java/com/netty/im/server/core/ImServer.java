package com.netty.im.server.core;

import com.netty.im.core.message.MessageDecoder;
import com.netty.im.core.message.MessageEncoder;
import com.netty.im.server.handler.ImServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * IM服务启动
 * @author yinjihuan
 *
 */
public class ImServer {
	
	public void run(int port) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
        		.channel(NioServerSocketChannel.class)
        		.childHandler(new ChannelInitializer<SocketChannel>() { 
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                    	ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
    					ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
    					ch.pipeline().addLast("decoder", new MessageDecoder());
    					ch.pipeline().addLast("encoder", new MessageEncoder());
                    	ch.pipeline().addLast(new ImServerHandler());
                    }
                })
        		.option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        
        try {
			ChannelFuture f = bootstrap.bind(port).sync();
			 f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
	}
	
}
