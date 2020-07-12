package com.dhy.chat.web.dao;

import com.dhy.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, String> {

    /**
     * findByUsername
     * @param username username
     * @return User
     */
    User findByUsername(String username);
}
