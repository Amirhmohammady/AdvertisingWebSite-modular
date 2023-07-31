package com.mycompany.advertising.service.events;


import com.mycompany.advertising.api.SmsService;
import com.mycompany.advertising.api.exceptions.CreateTokenException;
import com.mycompany.advertising.api.exceptions.SendSmsException;
import com.mycompany.advertising.repository.VerificationTokenRepository;
import com.mycompany.advertising.repository.entity.UserTo;
import com.mycompany.advertising.repository.entity.VerificationTokenTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Created by Amir on 9/14/2021.
 */
@Component
public class SmsSenderListener implements ApplicationListener<OnSigningSendSmsEvent> {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private SmsService smsService;
    @Value("${token.expire.minutes}")
    private int expiretockentime;

    @Override
    public void onApplicationEvent(OnSigningSendSmsEvent event) {
        logger.trace("SigningUpCompleteEvent happened");
        this.sendSms(event.getUser());
    }

    private void sendSms(UserTo user) {
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
        logger.info("token " + token.getToken() + " successfully send to " + user.getUsername());
    }
}
