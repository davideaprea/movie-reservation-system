package com.mrs.security.mapper;

import com.mrs.security.dto.UserCreateResponse;
import com.mrs.security.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserCreateResponse toResponse(User user);
}
