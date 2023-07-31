package com.mycompany.advertising.web.controller;

import com.mycompany.advertising.api.UserService;
import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.web.config.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

/**
 * Created by Amir on 1/30/2020.
 */
@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    @Validated
    @ResponseBody
    public ResponseEntity<String> signUp(@RequestBody @Valid UserDto user) {
        logger.info("signup controller called with " + user.toString());
        userService.createUser(user);
        return new ResponseEntity<>("resgister successfully", HttpStatus.OK);
    }


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtUserPass authenticationRequest)
            throws Exception {
        Objects.requireNonNull(authenticationRequest.getUsername());
        Objects.requireNonNull(authenticationRequest.getPassword());

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            UserDto user = (UserDto) authenticate.getPrincipal();
            return ResponseEntity.ok(new JwtResponse(jwtTokenUtil.generateToken(user)));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
