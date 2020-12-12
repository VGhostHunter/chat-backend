package com.dhy.chat.web.service.impl;

import com.dhy.chat.entity.User;
import com.dhy.chat.web.repository.UserRepository;
import com.dhy.chat.web.service.user.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceImplTest {

    @Autowired
    private IUserService userService;
    @Autowired
    private UserRepository userRepository;


    @Test
    public void getUserClientIds() {
        User user1 = new User();
        user1.setClientId("123456");
        user1.setUsername("张三");
        user1.setPassword("111");

        User user2 = new User();
        user2.setClientId("123222");
        user2.setUsername("张撒");
        user2.setPassword("111");

        User user3 = new User();
        user3.setClientId("123222");
        user3.setUsername("张撒2");
        user3.setPassword("111");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        List<String> userIds = new ArrayList<>();
        userIds.add(user1.getId());
        userIds.add(user2.getId());
        userIds.add(user3.getId());
        Set<String> ids =  userService.getUserClientIds(userIds);

        assertNotNull(ids);
        assertEquals(2, ids.size());
        assertTrue(ids.contains(user1.getClientId()));
        assertTrue(ids.contains(user2.getClientId()));
    }
}