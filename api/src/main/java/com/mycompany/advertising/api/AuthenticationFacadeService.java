package com.mycompany.advertising.api;

import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.api.enums.Language;

import java.util.Locale;

/**
 * Created by Amir on 6/24/2020.
 */
public interface AuthenticationFacadeService {

    UserDto getCurrentUser();
    boolean hasRole(String role);
    String getDomainName();
    Locale getCurrentLocale();
    Language getCurrentLanguage();
    String getCurrentClientIp();
}
