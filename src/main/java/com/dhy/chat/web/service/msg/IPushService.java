package com.dhy.chat.web.service.msg;

import com.dhy.chat.dto.message.ChatMsgDto;
import com.gexin.rp.sdk.base.IPushResult;

/**
 * @author vghosthunter
 */
public interface IPushService {

    /**
     * pushMessageToSingle
     * @param message message
     * @return
     */
    IPushResult pushMessageToSingle(ChatMsgDto message);

}
