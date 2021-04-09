package com.dhy.chat.web.service.msg.impl;

import com.dhy.chat.dto.message.ChatMsgDto;
import com.dhy.chat.dto.message.GetUserChatMsgListDto;
import com.dhy.chat.dto.PageResult;
import com.dhy.chat.entity.ChatMsg;
import com.dhy.chat.exception.BusinessException;
import com.dhy.chat.mapper.ChatMsgMapper;
import com.dhy.chat.web.repository.ChatMsgRepository;
import com.dhy.chat.web.service.msg.IChatMsgService;
import com.dhy.chat.web.service.user.IUserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ChatMsgServiceImpl implements IChatMsgService {

    private final ChatMsgRepository chatMsgRepository;
    private final IUserService userService;
    private final ChatMsgMapper chatMsgMapper;

    public ChatMsgServiceImpl(ChatMsgRepository chatMsgRepository,
                              IUserService userService,
                              ChatMsgMapper chatMsgMapper) {
        this.chatMsgRepository = chatMsgRepository;
        this.userService = userService;
        this.chatMsgMapper = chatMsgMapper;
    }


    @Override
    public PageResult<ChatMsgDto> getChatMsgList(GetUserChatMsgListDto input, AuthenticatedPrincipal principal) throws AuthenticationException {
        PageRequest pageRequest = PageRequest.of(input.getPageNum() - 1, input.getPageSize(), Sort.by(Sort.Direction.DESC, "creationTime"));

        String msg = input.getFilter() == null ? "" : input.getFilter();

        Slice<ChatMsgDto> page = chatMsgRepository.getUserMsgPage(msg, principal.getName(), input.getUserId(), pageRequest);

        return PageResult.convertSlice(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatMsgDto saveChatMsg(ChatMsg chatMsg) {
        String acceptUserId = chatMsg.getAcceptUserId();
        List<String> userIds = new ArrayList<>();
        userIds.add(acceptUserId);
        Set<String> clientIds = userService.getUserClientIds(userIds);
        if(clientIds.size() > 0) {
            ChatMsgDto chatMsgDto = chatMsgMapper.toDto(chatMsg);
            chatMsgDto.setClientId(clientIds.iterator().next());

            chatMsg.setClientId(chatMsgDto.getClientId());
            chatMsgRepository.save(chatMsg);
            return chatMsgDto;
        } else {
            throw new BusinessException("user client not found");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signChatMsg(List<String> signIds) {
        List<ChatMsg> chatMsgs = chatMsgRepository.getByIdIn(signIds);

        chatMsgs.forEach(x -> {
            x.setReadFlag(true);
        });

        chatMsgRepository.saveAll(chatMsgs);
    }
}
