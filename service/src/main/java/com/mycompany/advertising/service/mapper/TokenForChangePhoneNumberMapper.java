package com.mycompany.advertising.service.mapper;

import com.mycompany.advertising.api.dto.TokenForChangePhoneNumberDto;
import com.mycompany.advertising.repository.entity.TokenForChangePhoneNumberTo;
import org.mapstruct.Mapper;

/**
 * Created by Amir on 7/7/2023.
 */
@Mapper
public interface TokenForChangePhoneNumberMapper {
    TokenForChangePhoneNumberTo map(TokenForChangePhoneNumberDto inp);
    TokenForChangePhoneNumberDto map(TokenForChangePhoneNumberTo inp);
}
