package com.example.bookstore.Mapper;

import com.example.bookstore.DTO.Request.Creation.UserCreationRequest;
import com.example.bookstore.DTO.Response.UserResponse;
import com.example.bookstore.Entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserResponse toUserResponse(AppUser appUser);

    AppUser toAppUser(UserCreationRequest request);
}
