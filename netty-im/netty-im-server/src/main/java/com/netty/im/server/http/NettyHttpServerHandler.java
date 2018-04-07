package com.netty.im.server.http;

import com.netty.im.core.util.ByteUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;

public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HttpRequest) {
			DefaultHttpRequest request = (DefaultHttpRequest) msg;
			System.out.println("URI:" + request.getUri());
			System.err.println(msg);
		}
		
		if (msg instanceof HttpContent) {
			LastHttpContent httpContent = (LastHttpContent) msg;
			ByteBuf byteData = httpContent.content();
			if (byteData instanceof EmptyByteBuf) {
				System.out.println("Content：无数据");
			} else {
				String content = new String(ByteUtils.objectToByte(byteData));
				System.out.println("Content:" + content);
			}
		}
		
		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, 
				HttpResponseStatus.OK,
				Unpooled.wrappedBuffer("欢迎来到猿天地".getBytes("utf-8")));
		response.headers().set(Names.CONTENT_TYPE, "text/plain;charset=UTF-8");
		response.headers().set(Names.CONTENT_LENGTH, response.content().readableBytes());
		response.headers().set(Names.CONNECTION, Values.KEEP_ALIVE);
		ctx.write(response);
		ctx.flush();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		cause.printStackTrace();
	}
}
