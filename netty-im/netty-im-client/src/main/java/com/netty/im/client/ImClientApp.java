package com.netty.im.client;

import java.util.UUID;

import com.netty.im.client.core.ImConnection;
import com.netty.im.core.message.Message;
import com.netty.im.core.message.MessageProto;

import io.netty.channel.Channel;

/**
 * IM 客户端启动入口
 * @author yinjihuan
 */
public class ImClientApp {
	public static final String HOST = "127.0.0.1";
	public static int PORT = 2222;
	public static void main(String[] args) {
		Channel channel = new ImConnection().connect(HOST, PORT);
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		// protobuf
		MessageProto.Message message = MessageProto.Message.newBuilder().setId(id).setContent("hello yinjihuan").build();
		channel.writeAndFlush(message);
		//对象传输数据
		/*Message message = new Message();
		message.setId(id);
		message.setContent("hello yinjihuan");
		channel.writeAndFlush(message);*/
		//字符串传输数据
		//channel.writeAndFlush("yinjihuan");
	}
}
