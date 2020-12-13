package com.dhy.chat.web.service.user.impl;

import com.dhy.chat.entity.User;

import java.util.Optional;

/**
 * @author vghosthunter
 */
public interface IUserCacheService {

    /**
     * cacheUser
     * @param user user
     * @return
     */
    String cacheUser(User user);

    /**
     * retrieveUser
     * @param mfaId
     * @return
     */
    Optional<User> retrieveUser(String mfaId);

    /**
     * verifyTotp
     * @param mfaId
     * @param code
     * @return
     */
    Optional<User> verifyTotp(String mfaId, String code);
}
