package com.netty.im.core.message;

import java.util.List;
import com.netty.im.core.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder {
	
	@Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object obj = ByteUtils.byteToObject(ByteUtils.read(in));
        out.add(obj);
    }
	
}
