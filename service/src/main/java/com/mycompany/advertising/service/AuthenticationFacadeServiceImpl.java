package com.mycompany.advertising.service;

import com.mycompany.advertising.api.AuthenticationFacadeService;
import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.api.enums.Language;
import com.mycompany.advertising.api.language.LngManager;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Locale;

/**
 * Created by Amir on 6/24/2020.
 */
@Component
public class AuthenticationFacadeServiceImpl implements AuthenticationFacadeService {
    @Override
    public UserDto getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDto) {
            return (UserDto) principal;
        } else {
            return null;
        }
    }

    @Override
    public boolean hasRole(String role) {
        System.out.println("checking for Authentication----------------------");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        for (GrantedAuthority authoritie : authorities) {
            System.out.println(authoritie.toString());
            if (role.equals(authoritie.getAuthority())) {
                System.out.println("returning true in hasRole");
                return true;
            }
        }
        System.out.println("returning false in hasRole");
        return false;
    }

    @Override
    public String getDomainName() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    @Override
    public Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }

    @Override
    public Language getCurrentLanguage() {
        return LngManager.whatLanguage(LocaleContextHolder.getLocale().toString());
    }

    @Override
    public String getCurrentClientIp() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();
    }
    /*@Override
    public UserTo getUserToDetails2() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        GrantedAuthority grantedAuthority = authorities.iterator().next();
        if (grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS")) return null;
        else {
            try {
                String username;
                Object principal = auth.getPrincipal();
                if (principal instanceof UserDetails) {
                    username = ((UserDetails) principal).getUsername();
                } else {
                    username = principal.toString();
                }
                return userToDetailsService.loadUserToByUsername(username);
            } catch (UsernameNotFoundException e) {
                return null;
            }
        }
    }*/
}
