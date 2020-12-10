package com.dhy.chat.netty;

import com.dhy.chat.dto.message.ChatMsgDto;
import com.dhy.chat.enums.MsgActionEnum;

import java.io.Serializable;

public class DataContent implements Serializable {

    private static final long serialVersionUID = 2625502713525797298L;

    /** 动作类型 */
    private MsgActionEnum action;

    /** 用户的聊天内容 */
    private ChatMsgDto chatMsg;

    /** 签收的id 以 , 分割 */
    private String signIds;

    /** token 创建连接时使用 */
    private String token;

    public MsgActionEnum getAction() {
        return action;
    }

    public void setAction(MsgActionEnum action) {
        this.action = action;
    }

    public ChatMsgDto getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(ChatMsgDto chatMsg) {
        this.chatMsg = chatMsg;
    }

    public String getSignIds() {
        return signIds;
    }

    public void setSignIds(String signIds) {
        this.signIds = signIds;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
