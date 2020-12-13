package com.dhy.chat.web.config;

import com.dhy.chat.ChatAppSeeder;
import com.dhy.chat.entity.User;
import com.dhy.chat.web.filter.AuditFilter;
import com.dhy.chat.web.filter.JwtAuthenticationTokenFilter;
import com.dhy.chat.web.filter.RestAuthenticationFilter;
import com.dhy.chat.web.handler.*;
import com.dhy.chat.web.repository.AuditLogRepository;
import com.dhy.chat.web.service.user.impl.UserDetailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * @author vghosthunter
 * 开启权限注解,默认是关闭的
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 自定义登录成功处理器
     */
    private final UserLoginSuccessHandler userLoginSuccessHandler;
    /**
     * 自定义登录失败处理器
     */

    private final AuditLogRepository auditLogRepository;

    private final UserLoginFailureHandler userLoginFailureHandler;
    /**
     * 自定义注销成功处理器
     */
    private final UserLogoutSuccessHandler userLogoutSuccessHandler;
    /**
     * 自定义暂无权限处理器
     */
    private final UserAuthAccessDeniedHandler userAuthAccessDeniedHandler;

    private final ObjectMapper objectMapper;

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    private final UserDetailServiceImpl userDetailsService;

    private final Environment environment;

    /**
     * 自定义未登录的处理器
     */
    private final UserAuthenticationEntryPointHandler userAuthenticationEntryPointHandler;

    public SecurityConfig(UserLoginSuccessHandler userLoginSuccessHandler,
                          AuditLogRepository auditLogRepository,
                          UserLoginFailureHandler userLoginFailureHandler,
                          UserLogoutSuccessHandler userLogoutSuccessHandler,
                          UserAuthAccessDeniedHandler userAuthAccessDeniedHandler,
                          ObjectMapper objectMapper,
                          JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                          UserDetailServiceImpl userDetailsService,
                          Environment environment,
                          UserAuthenticationEntryPointHandler userAuthenticationEntryPointHandler) {
        this.userLoginSuccessHandler = userLoginSuccessHandler;
        this.auditLogRepository = auditLogRepository;
        this.userLoginFailureHandler = userLoginFailureHandler;
        this.userLogoutSuccessHandler = userLogoutSuccessHandler;
        this.userAuthAccessDeniedHandler = userAuthAccessDeniedHandler;
        this.objectMapper = objectMapper;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.userDetailsService = userDetailsService;
        this.environment = environment;
        this.userAuthenticationEntryPointHandler = userAuthenticationEntryPointHandler;
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 加密方式
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public RestAuthenticationFilter restAuthenticationFilter() throws Exception {
//        RestAuthenticationFilter filter = new RestAuthenticationFilter(objectMapper);
//        filter.setAuthenticationSuccessHandler(userLoginSuccessHandler);
//        filter.setAuthenticationFailureHandler(userLoginFailureHandler);
//        filter.setAuthenticationManager(authenticationManager());
//        filter.setFilterProcessesUrl("/api/user/login");
//        return filter;
//    }

    @Bean
    public AuditFilter auditFilter() {
        return new AuditFilter(auditLogRepository);
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                if(SecurityContextHolder.getContext().getAuthentication() != null &&
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
                    var userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
                    return Optional.ofNullable(userId);
                }
                return Optional.empty();
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
        //配置其他自定义的provider
    }

    /**
     * 我们在 Spring Boot 中有几种其他方式配置 CORS
     * 参见 https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-cors
     * Mvc 的配置方式见 WebMvcConfig 中的代码
     *
     * @return CorsConfigurationSource
     */
//    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许跨域访问的主机
        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:62519"));
        } else {
            configuration.setAllowedOrigins(Collections.singletonList("https://chat.vghost.top"));
        }
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.addExposedHeader("X-Authenticate");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 配置security的控制逻辑
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(req -> req.antMatchers("/api/user/create",
                "/api/user/login",
                "/api/user/token",
                "/api/user/refresh",
                "/api/user/totp",
                "/api/user/verify").permitAll()
                    .antMatchers(HttpMethod.GET, "/swagger-ui.html",
                            "/swagger-ui/*",
                            "/swagger-resources/**",
                            "/v2/api-docs",
                            "/v3/api-docs",
                            "/webjars/**",
                            "/favicon.ico").permitAll()
                    .antMatchers("/api/**").hasAuthority(ChatAppSeeder.GENERAL_USER)
                    .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(userAuthenticationEntryPointHandler)
                        .accessDeniedHandler(userAuthAccessDeniedHandler))
                // 基于Token不需要session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterAt(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 添加审计过滤器
                .addFilterBefore(auditFilter(), FilterSecurityInterceptor.class)
                // 添加JWT过滤器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 禁用缓存
                .headers(HeadersConfigurer::cacheControl)
                //配置登出地址
                .logout(logout -> logout.logoutUrl("/user/logout")
                        //配置用户登出自定义处理类
                        .logoutSuccessHandler(userLogoutSuccessHandler)
                )
                // 开启跨域
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .cors(cors -> Customizer.withDefaults())
                // 取消跨站请求伪造防护
                .csrf(AbstractHttpConfigurer::disable);
    }
}