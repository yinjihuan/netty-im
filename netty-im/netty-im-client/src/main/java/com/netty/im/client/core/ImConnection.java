package com.netty.im.client.core;

import com.netty.im.client.handler.ClientPoHandler;
import com.netty.im.client.handler.ClientStringHandler;
import com.netty.im.core.message.KryoDecoder;
import com.netty.im.core.message.KryoEncoder;
import com.netty.im.core.message.MessageDecoder;
import com.netty.im.core.message.MessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ImConnection {

	private Channel channel;
	
	public Channel connect(String host, int port) {
		doConnect(host, port);
		return this.channel;
	}

	private void doConnect(String host, int port) {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					//ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
					//ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
					//实体类传输数据，jdk序列化
					/*ch.pipeline().addLast("decoder", new MessageDecoder());
					ch.pipeline().addLast("encoder", new MessageEncoder());*/
					ch.pipeline().addLast("decoder", new KryoDecoder());
					ch.pipeline().addLast("encoder", new KryoEncoder());
					ch.pipeline().addLast(new ClientPoHandler());
					
					//字符串传输数据
					/*ch.pipeline().addLast("decoder", new StringDecoder());
					ch.pipeline().addLast("encoder", new StringEncoder());
					ch.pipeline().addLast(new ClientStringHandler());*/
				}
			});

			ChannelFuture f = b.connect(host, port).sync();
			channel = f.channel();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
