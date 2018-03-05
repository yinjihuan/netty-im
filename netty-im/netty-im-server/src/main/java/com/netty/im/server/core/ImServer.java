package com.netty.im.server.core;

import com.netty.im.core.message.MessageProto;
import com.netty.im.server.handler.ServerPoHandlerProto;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

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
                    	/*ch.pipeline().addLast("decoder", new KryoDecoder());
    					ch.pipeline().addLast("encoder", new KryoEncoder());*/
                    	
                    	// 实体类传输数据，protobuf序列化
                    	ch.pipeline().addLast("decoder",  
                                new ProtobufDecoder(MessageProto.Message.getDefaultInstance()));  
                    	ch.pipeline().addLast("encoder",  
                                new ProtobufEncoder());  
                    	ch.pipeline().addLast(new ServerPoHandlerProto());
                    	//ch.pipeline().addLast(new ServerPoHandler());
                    	//字符串传输数据
    					/*ch.pipeline().addLast("decoder", new StringDecoder());
    					ch.pipeline().addLast("encoder", new StringEncoder());
    					ch.pipeline().addLast(new ServerStringHandler());*/
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
