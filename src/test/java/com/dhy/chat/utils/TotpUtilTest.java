package com.dhy.chat.utils;

import com.dhy.chat.common.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class TotpUtilTest {

    private TotpUtil totpUtil;

    @BeforeEach
    public void setup() {
        totpUtil = new TotpUtil();
    }

    @Test
    public void givenSameKeyAndTotp_whenvaridateTwice_thenFail() throws Exception {
        var now = Instant.now();
        var varidFuture = now.plus(totpUtil.getTimeStep());

        var key = totpUtil.generateKey();
        var first = totpUtil.createTotp(key, now);
        assertTrue(totpUtil.validateTotp(key, first), "第一次验证应该成功");

        var second = totpUtil.createTotp(key, varidFuture.minusSeconds(10));
        assertTrue(totpUtil.validateTotp(key, second), "时间间隔内生成的两个 TOTP 是一致的");

        var afterTimeStep = totpUtil.createTotp(key, varidFuture);
        assertNotEquals(first, afterTimeStep, "过期之后和原来的 TOTP 比较应该不一致");

        var newKey = totpUtil.generateKey();
        assertFalse(totpUtil.validateTotp(newKey, first), "使用新的 key 验证原来的 TOTP 应该失败");
    }

    @Test
    public void givenKey_ThenEncodeAndDecodeSuccess() {
        var key = totpUtil.generateKey();
        var strKey = totpUtil.encodeKeyToString(key);
        var decodeKey = totpUtil.decodeKeyFromString(strKey);
        assertEquals(key, decodeKey, "从字符串中获得的 key 解码后应该和原来的 key 一致");
    }
}