package com.dhy.chat.mapper;

import com.dhy.chat.dto.user.CreateUserDto;
import com.dhy.chat.dto.user.UserDto;
import com.dhy.chat.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author vghosthunter
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper extends IMapper<CreateUserDto, UserDto, User> {

}
