package com.mycompany.advertising.api;


import com.mycompany.advertising.api.dto.TokenForChangePhoneNumberDto;
import com.mycompany.advertising.api.dto.UserDto;

/**
 * Created by Amir on 12/28/2021.
 */
public interface TokenForChangePhoneNumberService {
    TokenForChangePhoneNumberDto findByUser_Username(String username);

    TokenForChangePhoneNumberDto findByNewPhoneNumber(String newPhoneNumber);

    TokenForChangePhoneNumberDto findByUser(UserDto userTo);

    boolean existsByUser_Username(String username);

    boolean existsByNewPhoneNumber(String newPhoneNumber);

    boolean existsByUser(UserDto userTo);

    void saveVerificationToken(UserDto userTo, String newphonenumber);
}
