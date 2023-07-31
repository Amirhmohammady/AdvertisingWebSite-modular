package com.mycompany.advertising.service;

import com.mycompany.advertising.api.SmsService;
import com.mycompany.advertising.api.UserService;
import com.mycompany.advertising.api.VerificationTokenService;
import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.api.dto.VerificationTokenDto;
import com.mycompany.advertising.api.exceptions.CreateTokenException;
import com.mycompany.advertising.repository.UserRepository;
import com.mycompany.advertising.repository.VerificationTokenRepository;
import com.mycompany.advertising.repository.entity.UserTo;
import com.mycompany.advertising.service.mapper.UserMapper;
import com.mycompany.advertising.service.mapper.VerificationTokenMapper;
import com.mycompany.advertising.service.events.OnSigningSendSmsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 12/29/2021.
 */
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    UserMapper userMapper;
    @Autowired
    VerificationTokenMapper tokenMapper;
    @Autowired
    SmsService smsService;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VerificationTokenRepository verificationTokenRepository;
    @Value("${token.expire.minutes}")
    private int expiretockentime;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public VerificationTokenDto findByToken(String token) {
        return tokenMapper.map(verificationTokenRepository.findByToken(token));
    }

    @Override
    public VerificationTokenDto findByUser(UserDto user) {
        return tokenMapper.map(verificationTokenRepository.findByUser(userMapper.map(user)));
    }

    @Override
    public void deleteAllExpiredTokenSince(LocalDateTime now) {
        verificationTokenRepository.deleteAllExpiredTokenSince(now);
    }

    @Override
    public VerificationTokenDto findByUser_Username(String phonenumber) {
        return tokenMapper.map(verificationTokenRepository.findByUser_Username(phonenumber));
    }

    @Override
    @Transactional
    public long deleteByUser(UserDto user) {
        return verificationTokenRepository.deleteByUser(userMapper.map(user));
    }

    @Override
    public List<VerificationTokenDto> findByExpiryDateLessThan(LocalDateTime date) {
        return tokenMapper.map(verificationTokenRepository.findByExpiryDateLessThan(date));
    }

    @Override
    public void saveVerificationToken(UserDto user) {
        eventPublisher.publishEvent(new OnSigningSendSmsEvent(userMapper.map(user)));
        /*eventPublisher.publishEvent(new OnSigningSendSmsEvent(user));
        if (user.getEnabled()) throw new CreateTokenException("user is activated");
        if (verificationTokenRepository.existsByUser(user))
            throw new CreateTokenException("token is exist and sms will not send");
        VerificationTokenTo token = new VerificationTokenTo(new DecimalFormat(
                "000000").format(new Random().nextInt(999999)),
                user,
                LocalDateTime.now().plusMinutes(expiretockentime));//(System.currentTimeMillis() + (1000 * 60 * expiretockentime)));
        //Amir todo remove try/catch
        try {
            smsService.sendSms("your vrification code is: " + token.getToken(), user.getUsername());
        } catch (SendSmsException se) {

        }
        verificationTokenRepository.save(token);
        logger.info("token " + token.getToken() + " successfully send to " + user.getUsername());*/
    }

    @Override
    public void saveVerificationToken(String phonenumber) {
//        phonenumber = userService.getCorrectFormatPhoneNo(phonenumber);
        Optional<UserTo> user = userRepository.findByUsername(phonenumber);
        if (!user.isPresent()) throw new CreateTokenException("user is not exist");
        saveVerificationToken(userMapper.map(user.get()));
    }
}
