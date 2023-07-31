package com.mycompany.advertising.service.scheduler;

import com.mycompany.advertising.api.StorageService;
import com.mycompany.advertising.api.UserService;
import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.api.enums.Role;
import com.mycompany.advertising.service.locker.LockerApiStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;


/**
 * Created by Amir on 10/30/2021.
 */

@Configuration
@EnableScheduling
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    LockerApiStorageService lockerApiService;
    @Autowired
    StorageService storageService;
    @Autowired
    private UserService userservice;
    /*    @Scheduled(cron = "0 * * * * ?")
        public void doEveryMinute() {
            //deleteUserTocken
            userservice.deleteAllExiredToken(new Date(System.currentTimeMillis()));
            logger.info("all expired token deleted");
        }*/
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${admin.phone.number}")
    private String adminPhoneNumber;
    @Value("${admin.username}")
    private String adminUserNmae;

    //@Scheduled(fixedDelay = 60*60*1000)
    @Scheduled(cron = "0 0 * * * ?")
    public void doEveryHoure() {
        //deleteUserTocken
        userservice.deleteAllExiredToken(LocalDateTime.now());
        logger.info("all expired token deleted");
        lockerApiService.deleteAllExpiredLocker();
        logger.info("all expired saved method in memoty");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void doEveryDay() {
        System.out.println("doEveryDay");
        storageService.deleteUnusedImages();
        System.out.println("unused images deleted");
    }

    @EventListener
    public void initializeServer(ApplicationReadyEvent event) {
        createAdminAtStartup();
    }

    private void createAdminAtStartup(){
        UserDto user = new UserDto();
        user.setPassword(adminPassword);
        user.setUsername(adminPhoneNumber);
        user.setProfilename(adminUserNmae);
        user.setAuthority(Role.ROLE_ADMIN, Role.ROLE_USER);
        try {
            userservice.createUser(user);
            userservice.activateUser(adminPhoneNumber);
            logger.info("Admin User created at server start up");
        } catch (Exception e) {
            logger.info("Error! can not creating admin user at server startup." + e.getMessage());
        }
    }
}
