package com.dhy.chat.web.service;

import com.dhy.chat.dto.ChatMsgDto;
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
