package com.netty.im.server.handler;

import com.netty.im.core.message.Message;
import com.netty.im.server.core.ConnectionPool;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerPoHandler extends ChannelInboundHandlerAdapter {
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
		Message message = (Message) msg;
		if (ConnectionPool.getChannel(message.getId()) == null) {
			ConnectionPool.putChannel(message.getId(), ctx);
		}
		System.err.println("server:" + message.getId());
		ctx.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
