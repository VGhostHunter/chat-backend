package com.dhy.chat.web.repository;

import com.dhy.chat.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
