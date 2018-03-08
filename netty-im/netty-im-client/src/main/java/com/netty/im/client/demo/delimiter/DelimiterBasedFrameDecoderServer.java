package com.netty.im.client.demo.delimiter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 特殊分隔符解码
 * @author yinjihuan
 *
 */
public class DelimiterBasedFrameDecoderServer {
	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
        		.channel(NioServerSocketChannel.class)
        		.childHandler(new ChannelInitializer<SocketChannel>() { 
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                    	ch.pipeline().addLast(new DelimiterBasedFrameDecoder(10240, Unpooled.copiedBuffer("_".getBytes())));
    					ch.pipeline().addLast("decoder", new StringDecoder());
    					ch.pipeline().addLast("encoder", new StringEncoder());
    					ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
    						@Override
    					    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    							System.err.println("server:" + msg.toString());
    							ctx.writeAndFlush(msg.toString() + "你好" + "_");
    					    }
						});
                    }
                })
        		.option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        
        try {
			ChannelFuture f = bootstrap.bind(2222).sync();
			 f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
	}
}
