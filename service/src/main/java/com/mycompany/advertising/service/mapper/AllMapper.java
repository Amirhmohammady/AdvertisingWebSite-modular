package com.mycompany.advertising.service.mapper;

/**
 * Created by Amir on 7/7/2023.
 */

import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.repository.entity.UserTo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AllMapper {
    AllMapper INSTANCE = Mappers.getMapper(AllMapper.class);
    UserDto map(UserTo inp);
}
