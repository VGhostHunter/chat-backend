package com.dhy.chat.web.filter;

import com.dhy.chat.entity.AuditLog;
import com.dhy.chat.entity.User;
import com.dhy.chat.web.repository.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author vghosthunter
 */
public class AuditFilter extends OncePerRequestFilter {

    private final AuditLogRepository auditLogRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AuditFilter(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().contains("swagger")) {
            filterChain.doFilter(request, response);
        } else {
            AuditLog auditLog = new AuditLog();
            auditLog.setMethod(request.getMethod());
            auditLog.setPath(request.getRequestURI());
            auditLog.setIpAddress(request.getHeader("X-Real-IP"));

            logger.info("X-Real-Ip{}", request.getHeader("X-Real-Ip"));
            logger.info("X-Forwarded-For{}", request.getHeader("X-Forwarded-For"));
            auditLogRepository.save(auditLog);

            request.setAttribute("auditLogId", auditLog.getId());

            filterChain.doFilter(request, response);

            String id = (String) request.getAttribute("auditLogId");
            AuditLog a = auditLogRepository.findById(id).get();
            a.setStatus(response.getStatus());

            auditLogRepository.save(a);
        }
    }
}
