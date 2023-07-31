package com.mycompany.advertising.web.controller;

import com.mycompany.advertising.api.*;
import com.mycompany.advertising.api.dto.AdvertiseDto;
import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.api.enums.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Amir on 11/4/2021.
 */
@RestController
@RequestMapping("/restapi")
public class RestApi {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    UserService userservice;
    @Autowired
    VerificationTokenService verificationTokenService;
    @Autowired
    AdminMessageService adminMessageService;
    @Autowired
    AuthenticationFacadeService authenticationFacade;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdvertiseService advertiseService;

    @GetMapping("/sendsms/{phonenumber}")
    public ResponseEntity<String> sendSMS(@PathVariable String phonenumber) {
        logger.info("request for sending sms to " + phonenumber);
        verificationTokenService.saveVerificationToken(phonenumber);
        return new ResponseEntity<>("sms sent successfully", HttpStatus.OK);
    }

    @GetMapping("/activeUser/{phonenumber}/{confirmcode}")
    public ResponseEntity<String> activeUser(@PathVariable String phonenumber, @PathVariable String confirmcode) {
        logger.info("request for activating phone number " + phonenumber + " by token " + confirmcode);
        userservice.activateUser(phonenumber, confirmcode);
        return new ResponseEntity<>(phonenumber + " activated successfully", HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping(value = "/editUser")
//, headers = "Accept=application/json", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> editUser(@RequestBody Map<String, Object> body) {
        UserDto cuser = authenticationFacade.getCurrentUser();
        logger.info("try to edit user " + cuser);
        userservice.editUser(cuser, body);
        return new ResponseEntity<>("profile edited successfully", HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/userComment/{id}")
    public ResponseEntity<String> deleteUserComment(@PathVariable long id) {
        int rows = adminMessageService.deleteUserCommentById(id);
        if (rows > 0) return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
        return new ResponseEntity<>("fail to delete", HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/advertise/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<String> deleteAdvertise(@PathVariable Long id) {
        Optional<AdvertiseDto> advertiseoptl = advertiseService.getAdvertiseById(id);
        int rows = 0;
        if (advertiseoptl.isPresent()) {
            UserDto userTo = authenticationFacade.getCurrentUser();
            if (userTo != null && (userTo.hasRole(Role.ROLE_ADMIN) || advertiseoptl.get().getUserDto().getId() == userTo.getId()))
                rows = advertiseService.deleteAdvertiseById(id, userTo);
        }
        if (rows > 0) return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
        return new ResponseEntity<>("fail to delete", HttpStatus.NOT_ACCEPTABLE);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/adminMessage/{id}")
    public ResponseEntity<String> deleteAdminMessage(@PathVariable long id) {
        int rows = adminMessageService.deleteAdminMessageById(id);
        if (rows > 0) return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
        return new ResponseEntity<>("fail to delete", HttpStatus.NOT_ACCEPTABLE);
    }
}
