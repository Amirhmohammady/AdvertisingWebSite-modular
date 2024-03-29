package com.mycompany.advertising.web.controller;

import com.mycompany.advertising.api.language.LngManager;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Amir on 3/14/2022.
 */
@Controller
public class LanguageController {

    @GetMapping("/lan={language}")
    public String setLanguage(HttpServletRequest request, HttpServletResponse response
            , @PathVariable String language) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        localeResolver.setLocale(request, response, StringUtils.parseLocaleString(LngManager.whatLanguage(language).toString()));
        String referer = request.getHeader("Referer");
        if (referer == null) return "redirect:/index";
        return "redirect:" + referer;
    }
}
