package com.mycompany.advertising.service;

import com.mycompany.advertising.api.exceptions.TooManyLoginAttempt;
import com.mycompany.advertising.api.locker.annotations.LockApiByVariable;
import com.mycompany.advertising.api.locker.annotations.TimeLimiter;
import com.mycompany.advertising.repository.UserRepository;
import com.mycompany.advertising.repository.entity.UserTo;
import com.mycompany.advertising.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Amir on 6/6/2020.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    @LockApiByVariable(timeLimiter = @TimeLimiter(maxRequest = 2, inSeconds = 16), variableName = "phonenumber", excptionType = TooManyLoginAttempt.class, exceptionMsg = "too many attempt to login")
    public UserDetails loadUserByUsername(String phonenumber) throws UsernameNotFoundException {
        Optional<UserTo> user = userRepository.findByUsername(phonenumber);
        if (user.isPresent()) {
            return userMapper.map(user.get());
        } else {
            throw new UsernameNotFoundException("Phone Number " + phonenumber + " not found");//String.format("Username[%s] not found"));
        }
    }
}
