package com.dhy.chat.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        // 判断evt是否是IdleStateEvent（用于触发用户事件，包含 读空闲/写空闲/读写空闲 ）
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent)evt;

            if (event.state() == IdleState.READER_IDLE) {
                log.debug("{} 进入读空闲...", ctx.channel().id());
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.debug("{} 进入写空闲...", ctx.channel().id());
            } else if (event.state() == IdleState.ALL_IDLE) {
//                Channel channel = ctx.channel();
//                // 关闭无用的channel，以防资源浪费
//                channel.close();
            }
        }

    }
}
