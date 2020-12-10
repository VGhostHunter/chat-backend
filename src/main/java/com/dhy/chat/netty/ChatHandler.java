package com.dhy.chat.netty;

import com.dhy.chat.dto.message.ChatMsgDto;
import com.dhy.chat.entity.ChatMsg;
import com.dhy.chat.enums.MsgActionEnum;
import com.dhy.chat.utils.JwtTokenUtil;
import com.dhy.chat.utils.SpringUtils;
import com.dhy.chat.web.config.properties.AppProperties;
import com.dhy.chat.web.service.IChatMsgService;
import com.dhy.chat.web.service.IPushService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gexin.rp.sdk.base.IPushResult;
import io.jsonwebtoken.Claims;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author vghosthunter
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final IChatMsgService chatMsgService;
    private final IPushService pushService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AppProperties appProperties;

    public ChatHandler() {
        chatMsgService = (IChatMsgService) SpringUtils.getBean("chatMsgService");
        pushService = (IPushService) SpringUtils.getBean("pushServiceImpl");
        jwtTokenUtil = (JwtTokenUtil) SpringUtils.getBean("jwtTokenUtil");
        appProperties = (AppProperties) SpringUtils.getBean("appProperties");
    }

    /**
     * 用于记录和管理所有客户端的channel
     */
    private static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static HashMap<String, Channel> manager = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String content = msg.text();
        Channel currentChannel = ctx.channel();
        DataContent dataContent = objectMapper.readValue(content, DataContent.class);

        MsgActionEnum action = dataContent.getAction();

        switch (action) {
            case CONNECT:
                connect(currentChannel, dataContent);
                break;
            case CHAT:
                chat(currentChannel, dataContent);
                break;
            case SIGNED:
                sign(currentChannel, dataContent);
                break;
            case KEEPALIVE:
                keepalive(currentChannel);
                break;
            default:
                break;
        }
    }

    private void sign(Channel currentChannel, DataContent dataContent) {
        // 签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的签收状态[已签收]
        // 扩展字段在signed类型的消息中，代表需要去签收的消息id，逗号间隔
        List<String> msgIdList = Arrays.asList(dataContent.getSignIds().split(","));
        if (msgIdList.size() > 0) {
            // 批量签收
            chatMsgService.signChatMsg(msgIdList);
        }
    }

    private void chat(Channel currentChannel, DataContent dataContent) throws JsonProcessingException {
        // 聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态[未签收]
        ChatMsg chatMsg = new ChatMsg();
        BeanUtils.copyProperties(dataContent.getChatMsg(), chatMsg);
        // 保存消息到MongoDb，并且标记为 未签收
        ChatMsgDto dto = chatMsgService.saveChatMsg(chatMsg);

        // 发送消息
        // 从全局用户Channel关系中获取接受方的channel
        Channel receiverChannel = getChanel(chatMsg.getAcceptUserId());
        if (receiverChannel == null) {
            // channel为空代表用户离线，推送消息
            push(dto);
        } else {
            // 当receiverChannel不为空的时候，从ChannelGroup去查找对应的channel是否存在
            Channel findChannel = users.find(receiverChannel.id());
            if (findChannel != null) {
                // 用户在线
                try {
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(chatMsg)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    throw e;
                }
            } else {
                push(dto);
            }
        }
    }

    private void push(ChatMsgDto dto) {
        IPushResult ret = pushService.pushMessageToSingle(dto);
        if(ret == null) {
            Channel channel = getChanel(dto.getSendUserId());
            Channel sendChannel = users.find(channel.id());
            try {
                sendChannel.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString("Send Msg Failed")));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private void connect(Channel currentChannel, DataContent dataContent) {
        //认证
        // 截取JWT前缀
        String token = dataContent.getToken().replace(appProperties.getJwt().getTokenPrefix(), "");
        // 解析JWT
        Claims claims = jwtTokenUtil.parseClaims(token, jwtTokenUtil.getKey())
                .orElseThrow();
        // 获取用户名
        String userId = claims.getId();
        //当websocket 第一次open的时候，初始化channel，把用的channel和userId关联起来
        putChanel(userId, currentChannel);
        // debug
        output();
    }

    private void keepalive(Channel currentChannel) {
        // 心跳类型的消息
        log.debug("收到来自channel为 {} 的心跳包...", currentChannel);
    }

    /**
     * 当客户端连接服务端之后（打开连接）
     * 获取客户端的channel，并且放到ChannelGroup中去进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String channelId = ctx.channel().id().asShortText();
        log.debug("客户端被移除，channelId为：" + channelId);

        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        users.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }

    private void putChanel(String senderId, Channel channel) {
        manager.put(senderId, channel);
    }

    private Channel getChanel(String senderId) {
        return manager.get(senderId);
    }

    /**
     * test
     */
    private void output() {
        for (Channel c : users) {
            log.debug(c.id().asLongText());
        }

        for (HashMap.Entry<String, Channel> entry : manager.entrySet()) {
            log.debug("UserId: " + entry.getKey()
                    + ", ChannelId: " + entry.getValue().id().asLongText());
        }
    }
}
