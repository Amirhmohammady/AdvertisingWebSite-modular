package com.mycompany.advertising.service;

import com.mycompany.advertising.api.SmsService;
import com.mycompany.advertising.api.TokenForChangePhoneNumberService;
import com.mycompany.advertising.api.UserService;
import com.mycompany.advertising.api.dto.TokenForChangePhoneNumberDto;
import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.api.enums.UserStatuseByPhoneNumber;
import com.mycompany.advertising.api.exceptions.CreateTokenException;
import com.mycompany.advertising.repository.TokenForChangePhoneNumberRepository;
import com.mycompany.advertising.repository.entity.TokenForChangePhoneNumberTo;
import com.mycompany.advertising.service.mapper.TokenForChangePhoneNumberMapper;
import com.mycompany.advertising.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Created by Amir on 12/28/2021.
 */
@Service
public class TokenForChangePhoneNumberServiceImpl implements TokenForChangePhoneNumberService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TokenForChangePhoneNumberMapper tMapper;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenForChangePhoneNumberRepository tokenForChangePhoneNumberRepository;
    @Value("${token.expire.minutes}")
    private int expiretockentime;

    @Override
    public TokenForChangePhoneNumberDto findByUser_Username(String username) {
        return tMapper.map(tokenForChangePhoneNumberRepository.findByUser_Username(username));
    }

    @Override
    public TokenForChangePhoneNumberDto findByNewPhoneNumber(String newPhoneNumber) {
        return tMapper.map(tokenForChangePhoneNumberRepository.findByNewPhoneNumber(newPhoneNumber));
    }

    @Override
    public TokenForChangePhoneNumberDto findByUser(UserDto userDto) {
        return tMapper.map(tokenForChangePhoneNumberRepository.findByUser(userMapper.map(userDto)));
    }

    @Override
    public boolean existsByUser_Username(String username) {
        return tokenForChangePhoneNumberRepository.existsByUser_Username(username);
    }

    @Override
    public boolean existsByNewPhoneNumber(String newPhoneNumber) {
        return tokenForChangePhoneNumberRepository.existsByNewPhoneNumber(newPhoneNumber);
    }

    @Override
    public boolean existsByUser(UserDto userDto) {
        return tokenForChangePhoneNumberRepository.existsByUser(userMapper.map(userDto));
    }

    @Override
    public void saveVerificationToken(UserDto userDto, String newphonenumber){
//        newphonenumber = userService.getCorrectFormatPhoneNo(newphonenumber);
        if (userService.getUserStatuseByPhoneNumber(newphonenumber) != UserStatuseByPhoneNumber.NOT_EXIST)
            throw new CreateTokenException("new phone number is exist");
        if (tokenForChangePhoneNumberRepository.existsByNewPhoneNumber(newphonenumber))
            throw new CreateTokenException("new phone number is exist");
        if (tokenForChangePhoneNumberRepository.existsByUser(userMapper.map(userDto)))
            throw new CreateTokenException("you already have a change phone number task");
        TokenForChangePhoneNumberTo tokenForChangePhoneNumber = new TokenForChangePhoneNumberTo();
        tokenForChangePhoneNumber.setToken(new DecimalFormat("000000").format(new Random().nextInt(999999)));
        smsService.sendSms("your vrification code is: " + tokenForChangePhoneNumber.getToken(), userDto.getUsername());
        tokenForChangePhoneNumber.setExpiryDate(LocalDateTime.now().plusMinutes(expiretockentime));//new Date(System.currentTimeMillis() + (1000 * 60 * expiretockentime)));
        tokenForChangePhoneNumber.setUser(userMapper.map(userDto));
        tokenForChangePhoneNumber.setNewPhoneNumber(newphonenumber);
        tokenForChangePhoneNumberRepository.save(tokenForChangePhoneNumber);
        logger.info("tocken " + tokenForChangePhoneNumber.getToken() + " sent to " + userDto.getUsername());
    }
}
