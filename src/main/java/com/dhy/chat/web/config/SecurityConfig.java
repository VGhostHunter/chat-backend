package com.dhy.chat.web.config;

import com.dhy.chat.ChatAppSeeder;
import com.dhy.chat.web.filter.JwtAuthenticationTokenFilter;
import com.dhy.chat.web.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


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

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /**
     * 自定义未登录的处理器
     */
    private final UserAuthenticationEntryPointHandler userAuthenticationEntryPointHandler;

    public SecurityConfig(UserLoginSuccessHandler userLoginSuccessHandler,
                          UserLoginFailureHandler userLoginFailureHandler,
                          UserLogoutSuccessHandler userLogoutSuccessHandler,
                          UserAuthAccessDeniedHandler userAuthAccessDeniedHandler,
                          JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                          UserAuthenticationEntryPointHandler userAuthenticationEntryPointHandler) {
        this.userLoginSuccessHandler = userLoginSuccessHandler;
        this.userLoginFailureHandler = userLoginFailureHandler;
        this.userLogoutSuccessHandler = userLogoutSuccessHandler;
        this.userAuthAccessDeniedHandler = userAuthAccessDeniedHandler;
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

    /**
     * 配置security的控制逻辑
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //不进行权限验证的请求或资源(从配置文件中读取)
                .antMatchers("/api/user/create", "/api/user/login", "/favicon.ico").permitAll()
                .antMatchers(HttpMethod.GET, "/swagger-ui.html",
                        "/swagger-ui/*",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/webjars/**").permitAll()
                //其他的需要登陆后才能访问
                .anyRequest().hasAuthority(ChatAppSeeder.GENERAL_USER)
                .and()
                //配置未登录自定义处理类
                .httpBasic().authenticationEntryPoint(userAuthenticationEntryPointHandler)
                .and()
                //配置登录地址
                .formLogin()
                .loginProcessingUrl("/user/login")
                //配置登录成功自定义处理类
                .successHandler(userLoginSuccessHandler)
                //配置登录失败自定义处理类
                .failureHandler(userLoginFailureHandler)
                .and()
                //配置登出地址
                .logout()
                .logoutUrl("/user/logout")
                //配置用户登出自定义处理类
                .logoutSuccessHandler(userLogoutSuccessHandler)
                .and()
                //配置没有权限自定义处理类
                .exceptionHandling().accessDeniedHandler(userAuthAccessDeniedHandler)
                .and()
                // 开启跨域
                .cors()
                .and()
                // 取消跨站请求伪造防护
                .csrf().disable();

        // 基于Token不需要session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用缓存
        http.headers().cacheControl();
        // 添加JWT过滤器
        http.addFilter(jwtAuthenticationTokenFilter);
    }
}