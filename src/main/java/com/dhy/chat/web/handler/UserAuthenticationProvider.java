//package com.dhy.chat.web.handler;
//
//import com.dhy.chat.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.LockedException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class UserAuthenticationProvider implements AuthenticationProvider {
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        // 获取表单输入中返回的用户名
//        String userName = (String) authentication.getPrincipal();
//        // 获取表单中输入的密码
//        String password = (String) authentication.getCredentials();
//        // 查询用户是否存在
//        UserDetails userInfo = userDetailsService.loadUserByUsername(userName);
//        if (userInfo == null) {
//            throw new UsernameNotFoundException("用户名不存在");
//        }
//        // 我们还要判断密码是否正确，这里我们的密码使用BCryptPasswordEncoder进行加密的
//        if (!new BCryptPasswordEncoder().matches(password, userInfo.getPassword())) {
//            throw new BadCredentialsException("密码不正确");
//        }
//
//        // 进行登录
//        return new UsernamePasswordAuthenticationToken(userInfo, password, ((User)userInfo).getAuthorities());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return true;
//    }
//}