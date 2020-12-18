package com.dhy.chat.web.repository;

import com.dhy.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    /**
     * findOptionalByUsername
     * @param username
     * @return
     */
    Optional<User> findOptionalByUsername(String username);

    /**
     * findDistinctClientIdByIdIn
     * @param userIds userIds
     * @return
     */
    @Query("select distinct u.clientId from User u where u.id in ?1")
    Set<String> findDistinctClientIdByIdIn(List<String> userIds);
}
