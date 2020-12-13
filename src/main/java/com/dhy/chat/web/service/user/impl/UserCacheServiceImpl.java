package com.dhy.chat.web.service.user.impl;

import com.dhy.chat.entity.User;
import com.dhy.chat.utils.CryptoUtil;
import com.dhy.chat.utils.TotpUtil;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author vghosthunter
 */
@Service
public class UserCacheServiceImpl implements IUserCacheService {

    private final RedissonClient redisson;
    private final CryptoUtil cryptoUtil;
    private final TotpUtil totpUtil;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String CACHE_MFA = "CACHE_MFA";

    public UserCacheServiceImpl(RedissonClient redisson, CryptoUtil cryptoUtil, TotpUtil totpUtil) {
        this.redisson = redisson;
        this.cryptoUtil = cryptoUtil;
        this.totpUtil = totpUtil;
    }

    @Override
    public String cacheUser(User user) {
        var mfaId = cryptoUtil.randomAlphanumeric(12);
        log.debug("生成 mfaId: {}", mfaId);
        RMapCache<String, User> cache = redisson.getMapCache(CACHE_MFA);
        if (!cache.containsKey(mfaId)) {
            cache.put(mfaId, user, totpUtil.getTimeStepInLong(), TimeUnit.SECONDS);
        }
        return mfaId;
    }

    public Optional<User> retrieveUser(String mfaId) {
        log.debug("输入参数 mfaId: {}", mfaId);
        RMapCache<String, User> cache = redisson.getMapCache(CACHE_MFA);
        if (cache.containsKey(mfaId)) {
            log.debug("找到 mfaId {}", mfaId);
            return Optional.of(cache.get(mfaId));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> verifyTotp(String mfaId, String code) {
        log.debug("输入参数 mfaId: {}, code: {}", mfaId, code);
        RMapCache<String, User> cache = redisson.getMapCache(CACHE_MFA);
        if (!cache.containsKey(mfaId) || cache.get(mfaId) == null) {
            return Optional.empty();
        }
        var cachedUser = cache.get(mfaId);
        log.debug("找到用户 {}", cachedUser);
        try {
            var isValid = totpUtil.validateTotp(totpUtil.decodeKeyFromString(cachedUser.getMfaKey()), code);
            log.debug("code {} 的验证结果为 {}", code, isValid);
            if (!isValid) {
                return Optional.empty();
            }
            cache.remove(mfaId);
            log.debug("移除 mfaId: {}", mfaId);
            return Optional.of(cachedUser);
        } catch (InvalidKeyException e) {
            log.error("Key is invalid {}", e.getLocalizedMessage());
        }
        return Optional.empty();
    }
}
