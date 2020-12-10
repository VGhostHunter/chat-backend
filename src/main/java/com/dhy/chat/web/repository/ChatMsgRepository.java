package com.dhy.chat.web.repository;

import com.dhy.chat.dto.message.ChatMsgDto;
import com.dhy.chat.entity.ChatMsg;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMsgRepository extends MongoRepository<ChatMsg, String> {

    /**
     * getUserMsgPage
     * @param msg  msg
     * @param sendUserId sendUserId
     * @param acceptUserId acceptUserId
     * @param pageable pageable
     * @return Slice<ChatMsgDto>
     */
    @Query("{ $or: [{ 'msg': {'$regex': ?0}, 'sendUserId': ?1, 'acceptUserId': ?2 }, { 'msg': {'$regex': ?0}, 'acceptUserId': ?1, 'sendUserId': ?2 }]}")
    Slice<ChatMsgDto> getUserMsgPage(String msg, String sendUserId, String acceptUserId, Pageable pageable);

    /**
     *
     * getByIdIn
     * @param signIds signIds
     * @return List<ChatMsg>
     */
    List<ChatMsg> getByIdIn(List<String> signIds);
}