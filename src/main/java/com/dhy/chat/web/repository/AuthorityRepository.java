package com.dhy.chat.web.repository;

import com.dhy.chat.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author vghosthunter
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    /**
     * findByAuthority
     * @param Authority
     * @return
     */
    Authority findByAuthority(String Authority);

    /**
     * findOptionalByAuthority
     * @param Authority
     * @return
     */
    Optional<Authority> findOptionalByAuthority(String Authority);
}
