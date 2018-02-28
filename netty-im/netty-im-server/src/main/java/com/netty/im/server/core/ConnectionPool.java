package com.netty.im.server.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端连接管理
 * @author yinjihuan
 *
 */
public class ConnectionPool {
	
	private static Map<String, ChannelHandlerContext> pool = new ConcurrentHashMap<String, ChannelHandlerContext>();
	
	public static ChannelHandlerContext getChannel(String clientId) {
		if (clientId == null) {
			return null;
		}
		return pool.get(clientId);
	}
	
	public static ChannelHandlerContext putChannel(String clientId, ChannelHandlerContext channel) {
		return pool.put(clientId, channel);
	}
	
	public static Set<String> getClients() {
		return pool.keySet();
	}
	
	public static List<ChannelHandlerContext> getChannels() {
		List<ChannelHandlerContext> channels = new ArrayList<>();
		Set<String> keys = pool.keySet();
		for (String key : keys) {
			channels.add(pool.get(key));
		}
		return channels;
	}
	
}
