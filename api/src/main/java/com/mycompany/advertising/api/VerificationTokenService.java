package com.mycompany.advertising.api;

import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.api.dto.VerificationTokenDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Amir on 12/29/2021.
 */
public interface VerificationTokenService {
    VerificationTokenDto findByToken(String token);

    VerificationTokenDto findByUser(UserDto user);

    void deleteAllExpiredTokenSince(LocalDateTime now);

    VerificationTokenDto findByUser_Username(String phonenumber);

    long deleteByUser(UserDto user);

    List<VerificationTokenDto> findByExpiryDateLessThan(LocalDateTime date);

    public void saveVerificationToken(UserDto user);

    public void saveVerificationToken(String phonenumber);
}
