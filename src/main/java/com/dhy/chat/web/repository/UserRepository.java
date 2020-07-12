package com.dhy.chat.web.repository;

import com.dhy.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author vghosthunter
 */
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * findByUsername
     * @param username username
     * @return User
     */
    User findByUsername(String username);
}
