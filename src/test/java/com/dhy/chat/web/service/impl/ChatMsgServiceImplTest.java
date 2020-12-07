package com.dhy.chat.web.service.impl;

import com.dhy.chat.dto.ChatMsgDto;
import com.dhy.chat.entity.ChatMsg;
import com.dhy.chat.enums.MsgType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChatMsgServiceImplTest {

    @Autowired
    private ChatMsgServiceImpl chatMsgService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void saveChatMsg() {
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setMsgType(MsgType.TEXT);
        chatMsg.setSendUserId("testsenduserid");
        chatMsg.setReadFlag(false);
        chatMsg.setAcceptUserId("testacceptuserid");
        ChatMsgDto msg = chatMsgService.saveChatMsg(chatMsg);

        ChatMsg byId = mongoTemplate.findById(msg.getId(), ChatMsg.class);
        assertNotNull(byId);
        assertEquals(byId.getAcceptUserId(), "testacceptuserid");
        assertFalse(byId.isReadFlag());
    }

    @Test
    void getChatMsgList() {
    }
}