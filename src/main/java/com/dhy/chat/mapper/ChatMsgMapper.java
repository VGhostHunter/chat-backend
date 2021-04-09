package com.dhy.chat.mapper;

import com.dhy.chat.dto.message.ChatMsgDto;
import com.dhy.chat.entity.ChatMsg;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author vghosthunter
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ChatMsgMapper extends IMapper<ChatMsgDto, ChatMsgDto, ChatMsg> {
}
