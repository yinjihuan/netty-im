package com.netty.im.client.demo.object;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Object编解码示例
 * @author yinjihuan
 *
 */
public class ObjectEncoderAndDecoderClient {
	public static void main(String[] args) {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		Channel channel = null;
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("decoder", new ObjectDecoder(ClassResolvers.cacheDisabled(
							this.getClass().getClassLoader()
					)));
					ch.pipeline().addLast("encoder", new ObjectEncoder());
					ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
						@Override
					    public void channelRead(ChannelHandlerContext ctx, Object msg) {
							ObjectMessage m = (ObjectMessage) msg;
							System.err.println("client:" + m.getContent());
					    }
					});
				}
			});

			ChannelFuture f = b.connect("127.0.0.1", 2222).sync();
			channel = f.channel();
			ObjectMessage m = new ObjectMessage();
			m.setContent("hello yinjihuan");
			channel.writeAndFlush(m);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
