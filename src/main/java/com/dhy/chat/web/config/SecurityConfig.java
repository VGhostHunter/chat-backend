package com.dhy.chat.web.config;

import com.dhy.chat.ChatAppSeeder;
import com.dhy.chat.web.filter.JwtAuthenticationTokenFilter;
import com.dhy.chat.web.filter.RestAuthenticationFilter;
import com.dhy.chat.web.handler.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
/**
 * @author vghosthunter
 * 开启权限注解,默认是关闭的
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 自定义登录成功处理器
     */
    private final UserLoginSuccessHandler userLoginSuccessHandler;
    /**
     * 自定义登录失败处理器
     */

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

    /**
     * 自定义未登录的处理器
     */
    private final UserAuthenticationEntryPointHandler userAuthenticationEntryPointHandler;

    public SecurityConfig(UserLoginSuccessHandler userLoginSuccessHandler,
                          UserLoginFailureHandler userLoginFailureHandler,
                          UserLogoutSuccessHandler userLogoutSuccessHandler,
                          UserAuthAccessDeniedHandler userAuthAccessDeniedHandler,
                          ObjectMapper objectMapper,
                          JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                          UserAuthenticationEntryPointHandler userAuthenticationEntryPointHandler) {
        this.userLoginSuccessHandler = userLoginSuccessHandler;
        this.userLoginFailureHandler = userLoginFailureHandler;
        this.userLogoutSuccessHandler = userLogoutSuccessHandler;
        this.userAuthAccessDeniedHandler = userAuthAccessDeniedHandler;
        this.objectMapper = objectMapper;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
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

    @Bean
    public RestAuthenticationFilter restAuthenticationFilter() throws Exception {
        RestAuthenticationFilter filter = new RestAuthenticationFilter(objectMapper);
        filter.setAuthenticationSuccessHandler(userLoginSuccessHandler);
        filter.setAuthenticationFailureHandler(userLoginFailureHandler);
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/api/user/login");
        return filter;
    }

    /**
     * 配置security的控制逻辑
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(req -> req.antMatchers("/api/user/create", "/api/user/login", "/favicon.ico").permitAll()
                    .antMatchers(HttpMethod.GET, "/swagger-ui.html",
                            "/swagger-ui/*",
                            "/swagger-resources/**",
                            "/v2/api-docs",
                            "/v3/api-docs",
                            "/webjars/**").permitAll()
                    .antMatchers("/api/**").hasAuthority(ChatAppSeeder.GENERAL_USER)
                    .anyRequest().authenticated()
                )
                .addFilterAt(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                //配置未登录自定义处理类
                .httpBasic(basic -> basic.authenticationEntryPoint(userAuthenticationEntryPointHandler))
//                配置登录地址
//                .formLogin()
//                .loginProcessingUrl("/user/login")
//                配置登录成功自定义处理类
//                .successHandler(userLoginSuccessHandler)
//                配置登录失败自定义处理类
//                .failureHandler(userLoginFailureHandler)
//                .and()
                //配置登出地址
                .logout(logout -> logout.logoutUrl("/user/logout")
                        //配置用户登出自定义处理类
                        .logoutSuccessHandler(userLogoutSuccessHandler)
                )
                //配置没有权限自定义处理类
                .exceptionHandling(
                        ex -> ex.accessDeniedHandler(userAuthAccessDeniedHandler)
                )
                // 开启跨域
                .cors(Customizer.withDefaults())
                // 取消跨站请求伪造防护
                .csrf(AbstractHttpConfigurer::disable);

        // 基于Token不需要session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用缓存
        http.headers().cacheControl();
        // 添加JWT过滤器
        http.addFilter(jwtAuthenticationTokenFilter);
    }
}