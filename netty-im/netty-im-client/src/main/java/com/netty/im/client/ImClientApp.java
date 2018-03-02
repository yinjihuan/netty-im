package com.netty.im.client;

import java.util.UUID;

import com.netty.im.client.core.ImConnection;
import com.netty.im.core.message.Message;

import io.netty.channel.Channel;

/**
 * IM 客户端启动入口
 * @author yinjihuan
 */
public class ImClientApp {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 2222;
		Channel channel = new ImConnection().connect(host, port);
		/*Message message = new Message();
		message.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		message.setContent("hello yinjihuan");
		channel.writeAndFlush(message);*/
		channel.writeAndFlush("yinjihuan");
	}
}
