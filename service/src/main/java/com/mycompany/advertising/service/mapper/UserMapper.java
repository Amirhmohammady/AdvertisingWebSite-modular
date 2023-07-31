package com.mycompany.advertising.service.mapper;

import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.repository.entity.UserTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by Amir on 7/6/2023.
 */
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDto map(UserTo inp);
    UserTo map(UserDto inp);
}
