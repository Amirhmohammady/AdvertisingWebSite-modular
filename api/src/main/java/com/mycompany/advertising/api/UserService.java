package com.mycompany.advertising.api;

import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.api.enums.UserStatuseByPhoneNumber;
import com.mycompany.advertising.api.exceptions.CreateTokenException;
import com.mycompany.advertising.api.exceptions.PhoneNumberFormatException;
import com.mycompany.advertising.api.exceptions.SendSmsException;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by Amir on 6/7/2020.
 */
public interface UserService {
    //void svaeUser(UserTo userTo) throws UserAlreadyExistException;

    boolean isEmailExist(String email);

    boolean isPhoneNoExist(String phoneno);

    UserDto getUserByToken(String verificationToken);

    UserDto getUserByPhoneNo(String phoneno);

    //public UserTo getCurrentUser();
    void createUser(UserDto userDto);

    //@Transactional put in implementation
    void activateUser(UserDto user);

    //@Transactional put in implementation
    void activateUser(String phonenumber);

    void activateUser(String phonenumber, String confirmcode);

    //public VerificationTokenTo getVerificationToken(String VerificationToken);
    //@Transactional put in implementation
    void deleteAllExiredToken(LocalDateTime date);

    UserStatuseByPhoneNumber getUserStatuseByPhoneNumber(String phonenumber);

    void editUser(UserDto olddata, Map<String, Object> newdata) throws CreateTokenException, PhoneNumberFormatException, SendSmsException;
}
