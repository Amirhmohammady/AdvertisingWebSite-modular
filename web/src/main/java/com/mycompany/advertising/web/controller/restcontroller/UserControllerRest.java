package com.mycompany.advertising.web.controller.restcontroller;

/**
 * Created by Amir on 6/28/2023.
 */

import com.mycompany.advertising.api.*;
import com.mycompany.advertising.api.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping("/api/v1")
public class UserControllerRest {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    AdvCategoryService advCategoryService;
    @Autowired
    AdvertiseService advertiseService;
    @Autowired
    AdminMessageService adminMessageService;
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationFacadeService authenticationFacade;

    @GetMapping("/whoami")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_OWNER"})
    public ResponseEntity<UserDto> whoAmI() {
        return new ResponseEntity<>(authenticationFacade.getCurrentUser(), HttpStatus.OK);
    }

}

