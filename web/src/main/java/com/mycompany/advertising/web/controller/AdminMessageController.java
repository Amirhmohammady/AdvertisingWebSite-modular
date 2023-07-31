package com.mycompany.advertising.web.controller;

import com.mycompany.advertising.api.AdminMessageService;
import com.mycompany.advertising.api.dto.AdminMessageDto;
import com.mycompany.advertising.api.dto.UserCommentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;
import java.time.LocalDateTime;

/**
 * Created by Amir on 2/11/2022.
 */
@Controller
public class AdminMessageController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    AdminMessageService adminMessageService;
    @Value("${amir.error.folder}")
    String errorfolder;

    @GetMapping("/adminMessages/{msgId}")
    public String adminMessagesGet(Model model, @PathVariable long msgId) {
        AdminMessageDto adminMessage = adminMessageService.getAdminMessageById(msgId);
        /*if (!adminMessageTo.isPresent()) {
            return errorfolder + "error-404";
        }*/
        model.addAttribute("adminMessage", adminMessage);
        return "adminMessage";
    }

    @GetMapping("/adminMessgesList/{page}")
    public String adminMessgesList(Model model, @PathVariable int page) {
        model.addAttribute("adminMessages", adminMessageService.getPageAdminMessage(page));
        return "allAdminMessages";
    }

    @PostMapping("/adminMessages/{msgId}")
    public String adminMessagesPost(Model model, @PathVariable long msgId,
                                    @RequestParam(required = false, name = "name") String name,
                                    @RequestParam(required = false, name = "message") String message) {
        AdminMessageDto adminMessage = adminMessageService.getAdminMessageById(msgId);
        /*if (!adminMessage.isPresent()) {
            return errorfolder + "error-404";
        }*/
        try {
            message = URLDecoder.decode(message, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logger.info("user sent comment \"" + name + " " + message + "\"");
        model.addAttribute("adminMessage", adminMessage);
        if (adminMessage.getMessageCnt() >= 20) return "adminMessage";
        UserCommentDto userComment = new UserCommentDto();
        userComment.setDate(LocalDateTime.now());
        userComment.setMessage(message);
        userComment.setName(name);
        userComment.setMsgowner(adminMessage);
        adminMessageService.addUserComment(userComment);
        return "adminMessage";
    }
}
