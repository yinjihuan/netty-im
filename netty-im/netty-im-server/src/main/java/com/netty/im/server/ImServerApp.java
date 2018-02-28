package com.netty.im.server;

import com.netty.im.server.core.ImServer;

/**
 * IM服务启动入口
 * @author yinjihuan
 */
public class ImServerApp {
	public static void main(String[] args) {
		int port = 2222;
		new ImServer().run(port);
	}
}
