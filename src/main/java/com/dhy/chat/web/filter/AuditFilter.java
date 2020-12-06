package com.dhy.chat.web.filter;

import com.dhy.chat.entity.AuditLog;
import com.dhy.chat.entity.User;
import com.dhy.chat.web.repository.AuditLogRepository;
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

    public AuditFilter(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        AuditLog auditLog = new AuditLog();
        auditLog.setMethod(request.getMethod());
        auditLog.setPath(request.getRequestURI());
        auditLogRepository.save(auditLog);

        request.setAttribute("auditLogId", auditLog.getId());

        filterChain.doFilter(request, response);

        String id = (String) request.getAttribute("auditLogId");
        AuditLog a = auditLogRepository.findById(id).get();
        a.setStatus(response.getStatus());

        auditLogRepository.save(a);
    }
}
