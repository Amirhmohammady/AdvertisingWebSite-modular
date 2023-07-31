package com.mycompany.advertising.service.mapper;

import com.mycompany.advertising.api.dto.VerificationTokenDto;
import com.mycompany.advertising.repository.entity.VerificationTokenTo;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Created by Amir on 7/7/2023.
 */
@Mapper
public interface VerificationTokenMapper {
    VerificationTokenTo map(VerificationTokenDto inp);
    VerificationTokenDto map(VerificationTokenTo inp);
    List<VerificationTokenDto> map(List<VerificationTokenTo> inp);
}
