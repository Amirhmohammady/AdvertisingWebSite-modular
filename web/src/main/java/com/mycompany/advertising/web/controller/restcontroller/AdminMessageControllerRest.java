package com.mycompany.advertising.web.controller.restcontroller;

import com.mycompany.advertising.api.AdminMessageService;
import com.mycompany.advertising.api.dto.AdminMessageDto;
import com.mycompany.advertising.api.dto.UserCommentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;

/**
 * Created by Amir on 2/11/2022.
 */
@RestController
@RequestMapping("/api/v1/adminMessages")
public class AdminMessageControllerRest {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    AdminMessageService adminMessageService;
    @Value("${amir.error.folder}")
    String errorfolder;

    @GetMapping("/{msgId}")
    public ResponseEntity<AdminMessageDto> adminMessagesGet(Model model, @PathVariable long msgId) {
        AdminMessageDto adminMessage = adminMessageService.getAdminMessageById(msgId);
        return new ResponseEntity<>(adminMessage, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getList/{page}")
    public ResponseEntity<Page<AdminMessageDto>> adminMessgesList(Model model, @PathVariable int page) {
        return new ResponseEntity<>(adminMessageService.getPageAdminMessage(page), HttpStatus.OK);
    }

    @PostMapping("/{msgId}")
    public ResponseEntity<String> adminMessagesPost(@RequestBody UserCommentDto userComment) {
        logger.info("user sent comment " + userComment);
        adminMessageService.addUserComment(userComment);
        return new ResponseEntity<>("comment added successfully", HttpStatus.OK);
    }
}
