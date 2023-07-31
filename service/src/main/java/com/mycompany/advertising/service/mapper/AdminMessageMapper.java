package com.mycompany.advertising.service.mapper;

import com.mycompany.advertising.api.dto.AdminMessageDto;
import com.mycompany.advertising.repository.entity.AdminMessageTo;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * Created by Amir on 7/7/2023.
 */
@Mapper
public interface AdminMessageMapper {
    AdminMessageTo map(AdminMessageDto inp);
    AdminMessageDto map(AdminMessageTo inp);
    List<AdminMessageDto> map(List<AdminMessageTo> inp);

    default Page<AdminMessageDto> map(Page<AdminMessageTo> inp) {
        List<AdminMessageDto> adminMessageDtos = map(inp.getContent());
        return new PageImpl<>(adminMessageDtos, inp.getPageable(), inp.getTotalElements());
    }
}
