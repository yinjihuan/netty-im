package com.netty.im.client.handler;

import java.util.concurrent.TimeUnit;

import com.netty.im.client.ImClientApp;
import com.netty.im.client.core.ImConnection;
import com.netty.im.core.message.MessageProto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;

public class ClientPoHandlerProto extends ChannelInboundHandlerAdapter {
	private ImConnection imConnection = new ImConnection();

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		MessageProto.Message message = (MessageProto.Message) msg;
		System.out.println("client:" + message.getContent());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.err.println("掉线了...");
		//使用过程中断线重连
		final EventLoop eventLoop = ctx.channel().eventLoop();
		eventLoop.schedule(new Runnable() {
			@Override
			public void run() {
				imConnection.connect(ImClientApp.HOST, ImClientApp.PORT);
			}
		}, 1L, TimeUnit.SECONDS);
		super.channelInactive(ctx);
	}

}
