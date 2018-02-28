package com.netty.im.core.message;

import com.netty.im.core.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Message> {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception {
		byte[] datas = ByteUtils.objectToByte(message);
		out.writeBytes(datas);
		ctx.flush();
	}
	
}
