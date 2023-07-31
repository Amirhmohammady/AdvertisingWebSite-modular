package com.mycompany.advertising.api;


import com.mycompany.advertising.api.exceptions.SendSmsException;

/**
 * Created by Amir on 9/16/2021.
 */
public interface SmsService {
    void sendSms(String message, String phonenumber) throws SendSmsException;
    /*FarazSmsResponse sendTocken(String phonenumber) throws CreateTokenException, PhoneNumberFormatException;
    FarazSmsResponse sendTokenForEditNumber(UserTo userTo, String newphonenumber) throws PhoneNumberFormatException, CreateTokenException;*/
}
