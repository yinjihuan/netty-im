package com.netty.im.client.core;

import java.util.concurrent.TimeUnit;

import com.netty.im.client.handler.ClientPoHandlerProto;
import com.netty.im.core.message.MessageProto;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;

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
					// 使用框架自带的对象编解码器
                	/*ch.pipeline().addLast(
                			new ObjectDecoder(
                					ClassResolvers.cacheDisabled(
                							this.getClass().getClassLoader()
                					)
                			)
                	);
                	ch.pipeline().addLast(new ObjectEncoder());*/
					// 实体类传输数据，protobuf序列化
					ch.pipeline().addLast("ping", new IdleStateHandler(60, 20, 60 * 10, TimeUnit.SECONDS));
                	ch.pipeline().addLast("decoder",  
                            new ProtobufDecoder(MessageProto.Message.getDefaultInstance()));  
                	ch.pipeline().addLast("encoder",  
                            new ProtobufEncoder());  
                	ch.pipeline().addLast(new ClientPoHandlerProto());
					/*ch.pipeline().addLast("decoder", new KryoDecoder());
					ch.pipeline().addLast("encoder", new KryoEncoder());*/
					//ch.pipeline().addLast(new ClientPoHandler());
					
					//字符串传输数据
					/*ch.pipeline().addLast("decoder", new StringDecoder());
					ch.pipeline().addLast("encoder", new StringEncoder());
					ch.pipeline().addLast(new ClientStringHandler());*/
				}
			});

			ChannelFuture f = b.connect(host, port);
			f.addListener(new ConnectionListener());
			channel = f.channel();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
