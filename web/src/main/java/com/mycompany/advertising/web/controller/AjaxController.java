package com.mycompany.advertising.web.controller;

import com.mycompany.advertising.api.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

/**
 * Created by Amir on 5/8/2021.
 */
@RestController
@RequestMapping("/ajax")
public class AjaxController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private UserService userService;

    //@ResponseBody use it for @controller classes
    @GetMapping("/checkemail/{email}")
    public ResponseEntity<Object> emailStatus(@PathVariable String email) throws JSONException {
        JSONObject entity = new JSONObject();
        entity.put("isEmailExist", userService.isEmailExist(email));
        logger.trace("request for " + email + " existance and returned " + entity.toString());
        return new ResponseEntity<>(entity.toString(), HttpStatus.OK);
    }

    @GetMapping("/checkphonenumber/{phonenumber}")
    public ResponseEntity<Object> phoneNoStatus(@PathVariable String phonenumber) throws JSONException {
        JSONObject entity = new JSONObject();
        entity.put("phoneNoStatus", userService.getUserStatuseByPhoneNumber(phonenumber));
        logger.trace("request for " + phonenumber + " status and returned " + entity.toString());
        return new ResponseEntity<>(entity.toString(), HttpStatus.OK);
    }
}
