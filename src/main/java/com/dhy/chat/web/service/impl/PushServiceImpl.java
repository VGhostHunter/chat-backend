package com.dhy.chat.web.service.impl;

import com.dhy.chat.dto.message.ChatMsgDto;
import com.dhy.chat.web.config.properties.ChatAppProperties;
import com.dhy.chat.web.service.IPushService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import org.springframework.stereotype.Service;

/**
 * @author vghosthunter
 */
@Service
public class PushServiceImpl implements IPushService {

    private final ChatAppProperties chatAppProperties;

    private final ObjectMapper objectMapper;

    private final IGtPush push;

    public PushServiceImpl(ChatAppProperties chatAppProperties,
                           ObjectMapper objectMapper) {
        this.chatAppProperties = chatAppProperties;
        this.objectMapper = objectMapper;
        push = new IGtPush(chatAppProperties.getUrl(), chatAppProperties.getAppKey(), chatAppProperties.getMasterSecret());
    }

    @Override
    public IPushResult pushMessageToSingle(ChatMsgDto message) {
        TransmissionTemplate template = buildTransmissionTemplate();
        String content = null;
        try {
            content = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        template.setTransmissionContent(content);
        template.setAPNInfo(getAPNPayload(message.getMsg(), "Chat"));

        // 单推消息类型
        SingleMessage singleMessage = new SingleMessage();
        singleMessage.setData(template);
        // 判断客户端是否wifi环境下推送。1为仅在wifi环境下推送，0为不限制网络环境，默认不限
        singleMessage.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(chatAppProperties.getAppId());
        target.setClientId(message.getClientId());

        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(singleMessage, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(singleMessage, target, e.getRequestId());
        }
        return ret;
    }

    private TransmissionTemplate buildTransmissionTemplate() {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(chatAppProperties.getAppId());
        template.setAppkey(chatAppProperties.getAppKey());
        template.setTransmissionType(2);

        return template;
    }

    private APNPayload getAPNPayload(String message, String title) {
        APNPayload payload = new APNPayload();
        payload.setAutoBadge("+1");
        payload.setContentAvailable(1);
        //ios 12.0 以上可以使用 Dictionary 类型的 sound
        payload.setSound("default");

        APNPayload.DictionaryAlertMsg msg = new APNPayload.DictionaryAlertMsg();

        msg.setBody(message);
        msg.setTitle(title);
        payload.setAlertMsg(msg);

        return payload;
    }
}
