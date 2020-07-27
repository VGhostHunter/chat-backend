package com.dhy.chat;

import com.dhy.chat.netty.NettyServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${nettyserver.port}")
    private int port;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            try {
                NettyServer.getInstance().start(port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
