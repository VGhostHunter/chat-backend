package com.dhy.chat.web.service;

import com.dhy.chat.dto.ChatMsgDto;
import com.dhy.chat.dto.GetUserChatMsgListDto;
import com.dhy.chat.dto.PageResult;
import com.dhy.chat.entity.ChatMsg;
import org.springframework.security.core.AuthenticatedPrincipal;

import javax.security.sasl.AuthenticationException;
import java.util.List;

public interface IChatMsgService {

    /**
     * @param input
     * @param principal
     * @return
     */
    PageResult<ChatMsgDto> getChatMsgList(GetUserChatMsgListDto input, AuthenticatedPrincipal principal) throws AuthenticationException;

    /**
     * @param chatMsg
     * @return
     */
    ChatMsgDto saveChatMsg(ChatMsg chatMsg);

    /**
     * @param signIds
     */
    void signChatMsg(List<String> signIds);
}
