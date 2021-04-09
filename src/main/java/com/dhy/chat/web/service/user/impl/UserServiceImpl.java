package com.dhy.chat.web.service.user.impl;

import com.dhy.chat.ChatAppSeeder;
import com.dhy.chat.dto.user.*;
import com.dhy.chat.entity.User;
import com.dhy.chat.enums.MfaType;
import com.dhy.chat.exception.ArgumentException;
import com.dhy.chat.exception.BusinessException;
import com.dhy.chat.mapper.UserMapper;
import com.dhy.chat.utils.JwtTokenUtil;
import com.dhy.chat.utils.LocalMessageUtil;
import com.dhy.chat.utils.TotpUtil;
import com.dhy.chat.web.config.properties.AppProperties;
import com.dhy.chat.web.repository.AuthorityRepository;
import com.dhy.chat.web.repository.UserRepository;
import com.dhy.chat.web.service.email.IEmailService;
import com.dhy.chat.web.service.sms.ISmsService;
import com.dhy.chat.web.service.user.IUserService;
import org.springframework.data.util.Pair;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

/**
 * @author vghosthunter
 */
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AppProperties appProperties;
    private final LocalMessageUtil messageUtil;
    private final TotpUtil totpUtil;
    private final IUserCacheService userCacheService;
    private final ISmsService smsService;
    private final IEmailService emailService;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           AuthorityRepository authorityRepository,
                           JwtTokenUtil jwtTokenUtil,
                           AppProperties appProperties,
                           LocalMessageUtil messageUtil,
                           TotpUtil totpUtil,
                           IUserCacheService userCacheService,
                           ISmsService smsService,
                           IEmailService emailService,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.appProperties = appProperties;
        this.messageUtil = messageUtil;
        this.totpUtil = totpUtil;
        this.userCacheService = userCacheService;
        this.smsService = smsService;
        this.emailService = emailService;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            return null;
        }

        checkUser(user);
        return userMapper.toDto(user);
    }

    private void checkUser(User user) {
        if (!user.isEnabled()) {
            throw new BusinessException(messageUtil.GetMsg("message.userIsNotEnable"));
        }
        if (!user.isAccountNonLocked()) {
            throw new BusinessException(messageUtil.GetMsg("message.userIsLocked"));
        }
        if (!user.isAccountNonExpired()) {
            throw new BusinessException(messageUtil.GetMsg("message.userAccountIsExpired"));
        }
    }

    @Override
    public Optional<User> getOptionalByUsernameAndPassword(LoginDto input) {
        return userRepository.findOptionalByUsername(input.getUsername())
                .filter(user -> passwordEncoder.matches(input.getPassword(), user.getPassword()))
                .map(user -> {
                    checkUser(user);
                    return user;
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto createUser(CreateUserDto createUserDto) {
        User isExist = userRepository.findByUsername(createUserDto.getUsername());
        if(isExist != null) {
            throw new ArgumentException(messageUtil.GetMsg("message.usernameAlreadyExists"));
        }

        return authorityRepository.findOptionalByAuthority(ChatAppSeeder.GENERAL_USER)
                .map(role -> {
                    var user = userMapper.toEntity(createUserDto);
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setAuthorities(Set.of(role));
                    user.setMfaKey(totpUtil.generateStringKey());
                    userRepository.save(user);
                    return userMapper.toDto(user);
                }).orElseThrow();
    }

    @Override
    public UserDto getById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateClientId(String id, String clientId) {
        User user = userRepository.getOne(id);
        user.setClientId(clientId);
        userRepository.save(user);
    }

    @Override
    public Set<String> getUserClientIds(List<String> userIds) {
        return userRepository.findDistinctClientIdByIdIn(userIds);
    }

    @Override
    public AuthDto login(LoginDto input) throws AuthenticationException {
        return userRepository.findOptionalByUsername(input.getUsername())
                .filter(user -> passwordEncoder.matches(input.getPassword(), user.getPassword()))
                .map(this::login)
                .orElseThrow(() -> new BadCredentialsException(messageUtil.GetMsg("message.usernameOrPasswordError")));
    }

    @Override
    public AuthDto refreshToken(String token, String refreshToken) throws AccessDeniedException {
        var jwtToken = token.replace(appProperties.getJwt().getTokenPrefix(), "");
        if(jwtTokenUtil.validateRefreshToken(refreshToken)
                &&jwtTokenUtil.validateWithoutExpiration(jwtToken)) {
            return new AuthDto(jwtTokenUtil.buildAccessTokenWithRefreshToken(refreshToken), refreshToken);
        }

        throw new AccessDeniedException(messageUtil.GetMsg("message.accessDenied"));
    }

    @Override
    public void sendTotp(SendTotpDto sendTotpDto) {
        userCacheService.retrieveUser(sendTotpDto.getMfaId())
                .flatMap(user -> totpUtil.createTotp(user.getMfaKey()).map(code -> Pair.of(user, code)))
                .ifPresentOrElse(pair -> {
                    if (sendTotpDto.getMfaType() == MfaType.SMS) {
                        smsService.send(pair.getFirst().getMobile(), pair.getSecond());
                    } else if(sendTotpDto.getMfaType() == MfaType.EMAIL){
                        emailService.send(pair.getFirst().getEmail(), pair.getSecond());
                    } else {
                        throw new ArgumentException(messageUtil.GetMsg("message.mfaTypeNotSupport"));
                    }
                }, () -> {
                    throw new ArgumentException(messageUtil.GetMsg("message.invalidMfaId"));
                });
    }

    @Override
    public AuthDto loginWithTotp(TotpVerificationDto input) {
        return userCacheService.verifyTotp(input.getMfaId(), input.getCode())
                .map(this::login)
                .orElseThrow(() -> new BadCredentialsException(messageUtil.GetMsg("message.verifyCodeWrong")));
    }

    private AuthDto login(User user) {
        return new AuthDto(jwtTokenUtil.createAccessToken(user), jwtTokenUtil.createRefreshToken(user));
    }
}
