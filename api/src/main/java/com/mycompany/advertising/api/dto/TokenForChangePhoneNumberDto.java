package com.mycompany.advertising.api.dto;

import java.time.LocalDateTime;

/**
 * Created by Amir on 12/18/2021.
 */
public class TokenForChangePhoneNumberDto {
    private Long id;
    private String token;
    private String newPhoneNumber;
    private UserDto user;
    private LocalDateTime expiryDate;
    public TokenForChangePhoneNumberDto() {

    }

    public TokenForChangePhoneNumberDto(String token, UserDto user, LocalDateTime expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    public String getNewPhoneNumber() {
        return newPhoneNumber;
    }

    public void setNewPhoneNumber(String newPhoneNumber) {
        this.newPhoneNumber = newPhoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
