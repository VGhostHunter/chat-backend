package com.dhy.chat;

import com.dhy.chat.entity.Authority;
import com.dhy.chat.web.repository.AuthorityRepository;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author vghosthunter
 */
@Component
public class ChatAppSeeder implements ApplicationRunner {

    private final AuthorityRepository authorityRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());
    public static final String GENERAL_USER = "GeneralUser";

    public ChatAppSeeder(AuthorityRepository authorityRepository) {
        Assert.notNull(authorityRepository,"authorityRepository must not be null");
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(authorityRepository.findByAuthority(GENERAL_USER) == null) {
            Authority authority = new Authority();
            authority.setAuthority(GENERAL_USER);
            authorityRepository.save(authority);
            logger.info("Seed Authority {}", GENERAL_USER);
        }
    }
}
