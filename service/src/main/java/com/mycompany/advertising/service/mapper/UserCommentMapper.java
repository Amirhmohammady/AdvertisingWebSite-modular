package com.mycompany.advertising.service.mapper;

import com.mycompany.advertising.api.dto.UserCommentDto;
import com.mycompany.advertising.repository.entity.UserCommentTo;
import org.mapstruct.Mapper;

/**
 * Created by Amir on 7/7/2023.
 */
@Mapper
public interface UserCommentMapper {
    UserCommentDto map(UserCommentTo inp);
    UserCommentTo map(UserCommentDto inp);
}
