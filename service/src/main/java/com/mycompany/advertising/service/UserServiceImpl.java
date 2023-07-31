package com.mycompany.advertising.service;

import com.mycompany.advertising.api.TokenForChangePhoneNumberService;
import com.mycompany.advertising.api.UserService;
import com.mycompany.advertising.api.VerificationTokenService;
import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.api.dto.VerificationTokenDto;
import com.mycompany.advertising.api.enums.Role;
import com.mycompany.advertising.api.enums.UserStatuseByPhoneNumber;
import com.mycompany.advertising.api.exceptions.NotFoundException;
import com.mycompany.advertising.api.exceptions.UserAlreadyExistException;
import com.mycompany.advertising.repository.UserRepository;
import com.mycompany.advertising.repository.VerificationTokenRepository;
import com.mycompany.advertising.repository.entity.UserTo;
import com.mycompany.advertising.repository.entity.VerificationTokenTo;
import com.mycompany.advertising.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Created by Amir on 6/7/2020.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    UserMapper userMapper;
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private TokenForChangePhoneNumberService tokenForChangePhoneNumberService;
    @Value("${token.expire.minutes}")
    private int expiretockentime;

    //todo Amir
    @Override
    public void createUser(UserDto userDto) {
        //userTo.setUsername(getCorrectFormatPhoneNo(userTo.getUsername()));
        if (userRepository.existsByUsername(userDto.getUsername())) {
            logger.debug(userDto.toString() + "Phone Number is exist");
            throw new UserAlreadyExistException(userDto.getUsername()+" is exist. choose another phone number");
        } else {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userDto.setEnabled(false);
            userDto.setAuthority(Role.ROLE_USER);
            //2222222222222222222222222222
            userRepository.save(userMapper.map(userDto));
            verificationTokenService.saveVerificationToken(userDto);
            logger.debug(userDto.toString() + "saved successfully");
        }
    }

    @Override
    public void activateUser(UserDto user) {
        long deletedrows = verificationTokenRepository.deleteByUser(userMapper.map(user));
        //amir todo uncomment after adding user update
        //if (deletedrows > 0) {
        user.setEnabled(true);
        //2222222222222222
        userRepository.save(userMapper.map(user));
        logger.info("user: " + user.toString() + "activated");
        //} else logger.info("user: " + user.toString() + "coluldn,t activate");
    }

    @Override
    public void activateUser(String phonenumber) {
        Optional<UserTo> user = userRepository.findByUsername(phonenumber);
        if (user.isPresent()) {
            activateUser(userMapper.map(user.get()));
        } else logger.info("can not activate user phone number " + phonenumber + "is not exist");
    }

    @Override
    public void activateUser(String phonenumber, String confirmcode) {
//        phonenumber = getCorrectFormatPhoneNo(phonenumber);
        VerificationTokenDto token = verificationTokenService.findByUser_Username(phonenumber);
        if (token != null && token.getToken().equals(confirmcode)) {
            activateUser(phonenumber);
        }
        throw new NotFoundException("token is not correct");
    }

    @Override
    public boolean isEmailExist(String email) {
        if (userRepository.existsByEmailAndEnabled(email, true)) {
            logger.trace("Email " + email + " is exist");
            return true;
        } else {
            logger.trace("Email " + email + " is not exist");
            return false;
        }
    }

    @Override
    public boolean isPhoneNoExist(String phonenumber) {
        if (phonenumber.charAt(0) != '0') phonenumber = '0' + phonenumber;
        if (userRepository.existsByUsername(phonenumber)) {
            logger.trace("Phone number " + phonenumber + " is exist");
            return true;
        } else {
            logger.trace("Phone number " + phonenumber + " is not exist");
            return false;
        }
    }

    @Override
    public UserDto getUserByToken(String verificationToken) {
        return userMapper.map(verificationTokenRepository.findByToken(verificationToken).getUser());
    }

    @Override
    public UserDto getUserByPhoneNo(String phoneno) {
        Optional<UserTo> user = userRepository.findByUsername(phoneno);
        if (user.isPresent()) {
            return userMapper.map(user.get());
        } else {
            return null;
        }
    }

    @Override
    public void deleteAllExiredToken(LocalDateTime date) {
        List<VerificationTokenTo> verificationTokenTos = verificationTokenRepository.findByExpiryDateLessThan(date);
        if (verificationTokenTos.size() > 0) {
            List<UserTo> userTos = verificationTokenTos.stream().map((vto) -> {
                return (vto.getUser().getEnabled()) ? null : vto.getUser();
            }).filter(e -> e != null).collect(Collectors.toList());
            verificationTokenRepository.deleteAll(verificationTokenTos);
            userRepository.deleteAll(userTos);
        }
        //verificationTokenRepository.deleteAllExpiredTokenSince(date);
    }

    /*@Override
    public String getCorrectFormatPhoneNo(String phonenumber) throws PhoneNumberFormatException {
        if (phonenumber == null) throw new PhoneNumberFormatException("phone number can not be empty");
        if (phonenumber.charAt(0) != '0') phonenumber = '0' + phonenumber;
        Matcher matcher = Pattern.compile("^09[\\d]{9}$").matcher(phonenumber);
        if (!matcher.matches()) throw new PhoneNumberFormatException("phone format not correct");
        return phonenumber;
    }*/

    @Override
    public UserStatuseByPhoneNumber getUserStatuseByPhoneNumber(String phonenumber) {
//        try {
//            phonenumber = getCorrectFormatPhoneNo(phonenumber);
        UserDto user = getUserByPhoneNo(phonenumber);
        if (user == null) return UserStatuseByPhoneNumber.NOT_EXIST;
        else {
            if (user.getEnabled()) return UserStatuseByPhoneNumber.EXIST_AND_ACTIVATED;
            else if (verificationTokenRepository.existsByUser_Username(phonenumber)) {
                return UserStatuseByPhoneNumber.EXIST_BUT_NOT_ACTIVATED;
            } else {
                return UserStatuseByPhoneNumber.EXIST_BUT_TOKEN_DID_NOT_SEND;
            }
        }
/*        } catch (PhoneNumberFormatException e) {
            return UserStatuseByPhoneNumber.PHONE_FORMAT_NOT_CORRECT;
        }*/
    }

    @Override
    public void editUser(UserDto oldUser, Map<String, Object> newdata) {
        String pass = (String) newdata.get("password");
        if (!passwordEncoder.matches(pass, oldUser.getPassword()))
            throw new BadCredentialsException("password not correct");
        //String newUsername = getCorrectFormatPhoneNo((String) newdata.get("username"));
        String newUsername = (String) newdata.get("username");

        oldUser.setProfilename((String) newdata.get("profilename"));
        oldUser.setFullname((String) newdata.get("fullname"));
        String newpass = (String) newdata.get("newpass");
        if (newpass != null && !newpass.equals("")) oldUser.setPassword(passwordEncoder.encode(newpass));
        oldUser.setAboutme((String) newdata.get("aboutme"));
        oldUser.setWebsiteurl((String) newdata.get("websiteurl"));
        oldUser.setEmail((String) newdata.get("email"));
        if (!oldUser.getUsername().equals(newUsername)) {
            if (userRepository.existsByUsername(newUsername))
                throw new UserAlreadyExistException(newUsername + " is exist");
            tokenForChangePhoneNumberService.saveVerificationToken(oldUser, newUsername);
            oldUser.setEnabled(false);
        }
        userRepository.save(userMapper.map(oldUser));
    }
}
