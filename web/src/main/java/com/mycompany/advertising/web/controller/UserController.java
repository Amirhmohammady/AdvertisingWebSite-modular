package com.mycompany.advertising.web.controller;

import com.mycompany.advertising.api.*;
import com.mycompany.advertising.api.dto.AdminMessageDto;
import com.mycompany.advertising.api.dto.AdvertiseDto;
import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.web.controller.utils.PageCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

/**
 * Created by Amir on 8/21/2020.
 */
@Controller
@RequestMapping("User")
public class UserController {
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
    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/profile")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String profile() {
        return "profile";
    }

    @GetMapping("/adminMessage")
    @Secured("ROLE_ADMIN")
    public String adminMessageGet(Model model) {
        model.addAttribute("pfragment01", "adminMessage");
        return "profile2/Dashboard";
    }

    @PostMapping("/adminMessage")
    @Secured("ROLE_ADMIN")
    public String adminMessagePost(Model model, @RequestParam String title, @RequestParam String message) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDto) {
            AdminMessageDto adminMessage = new AdminMessageDto();
            adminMessage.setDate(LocalDateTime.now());
            adminMessage.setMessage(message);
            adminMessage.setTitle(title);
            adminMessage.setOwner((UserDto) principal);
            adminMessageService.addAdminMessage(adminMessage);
            logger.info("message added: " + message);
        } else logger.warn("message NOT added: " + message);
        model.addAttribute("pfragment01", "adminMessage");
        return "profile2/Dashboard";
    }

    @GetMapping("/userlist")
    @Secured({"ROLE_ADMIN"})
    public String userList() {
        return "user_list";
    }

    @GetMapping("/baneduserlist")
    @Secured({"ROLE_ADMIN"})
    public String banedUserList() {
        return "user_list";
    }

    @GetMapping("/unAcceptedAdvs/{pageNumber}")
    @Secured({"ROLE_ADMIN"})
    public String unAcceptedAdvsGet(Model model, @PathVariable Integer pageNumber) {
        if (pageNumber == null || pageNumber < 1) pageNumber = 1;
        Page<AdvertiseDto> advertiseToPage = advertiseService.getPageNotAcceptedAdvertises(pageNumber);
        model.addAttribute("currentPage", pageNumber);
        if (advertiseToPage.getTotalPages() == 0)
            model.addAttribute("pfragment01", "unAcceptedAdvs_notFound");
        else {
            if (pageNumber > advertiseToPage.getTotalPages()) pageNumber = advertiseToPage.getTotalPages();
            model.addAttribute("advertises", advertiseToPage);
            model.addAttribute("pfragment01", "unAcceptedAdvs");
            model.addAttribute("pages", PageCalculator.getMyPage(advertiseToPage.getTotalPages(), pageNumber, 7));
        }
        return "profile2/Dashboard";
    }

    @GetMapping("/Dashboard")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String dashboard(Model model) {//}, HttpServletRequest request) {
        UserDto userTo = authenticationFacade.getCurrentUser();
        model.addAttribute("userTo", userTo);
        model.addAttribute("pfragment01", "profile");
        //if (request.isUserInRole("ROLE_ADMIN")) return "profile2/DashboardAdmin";
        //if (request.isUserInRole("ROLE_USER")) return "profile2/DashboardUser";
        return "profile2/Dashboard";
    }

    @GetMapping("/manageTags")
    @Secured({"ROLE_ADMIN"})
    public String manageTags(Model model) {
        model.addAttribute("pfragment01", "manageTags");
        //if (request.isUserInRole("ROLE_ADMIN")) return "profile2/DashboardAdmin";
        //if (request.isUserInRole("ROLE_USER")) return "profile2/DashboardUser";
        return "profile2/Dashboard";
    }
}
