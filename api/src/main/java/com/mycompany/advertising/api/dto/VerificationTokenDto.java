package com.mycompany.advertising.api.dto;


import java.time.LocalDateTime;

/**
 * Created by Amir on 5/12/2021.
 */
public class VerificationTokenDto {
    private Long id;
    private String token;
    private UserDto user;
    private LocalDateTime expiryDate;

    public VerificationTokenDto() {

    }

    public VerificationTokenDto(String token, UserDto user, LocalDateTime expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
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
