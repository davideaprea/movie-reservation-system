package com.mrs.app.security.mapper;

import com.mrs.app.security.dto.UserCreateResponse;
import com.mrs.app.security.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserCreateResponse toResponse(User user);
}
