package com.dhy.chat.web.repository;

import com.dhy.chat.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author vghosthunter
 */
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
}
