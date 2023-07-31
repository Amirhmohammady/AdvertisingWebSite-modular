package com.mycompany.advertising.service;

import com.mycompany.advertising.api.AdminMessageService;
import com.mycompany.advertising.api.dto.AdminMessageDto;
import com.mycompany.advertising.api.dto.UserCommentDto;
import com.mycompany.advertising.api.exceptions.MyNotFoundException;
import com.mycompany.advertising.repository.AdminMessageRepository;
import com.mycompany.advertising.repository.UserCommentRepository;
import com.mycompany.advertising.repository.entity.AdminMessageTo;
import com.mycompany.advertising.repository.entity.UserCommentTo;
import com.mycompany.advertising.service.mapper.AdminMessageMapper;
import com.mycompany.advertising.service.mapper.UserCommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Amir on 1/28/2022.
 */
@Service
@Transactional
public class AdminMessageServiceImpl implements AdminMessageService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private UserCommentRepository userCommentRepository;
    @Autowired
    private AdminMessageRepository adminMessageRepository;
    @Autowired
    UserCommentMapper userCommentMapper;
    @Autowired
    private AdminMessageMapper adminMessageMapper;

    @Override
    public AdminMessageDto addAdminMessage(AdminMessageDto adminMessage) {
        AdminMessageTo rslt = adminMessageRepository.save(adminMessageMapper.map(adminMessage));
        logger.info("admin message saved: " + rslt);
        return adminMessageMapper.map(rslt);
    }

    @Override
    public AdminMessageDto getLastMessage() {
        AdminMessageTo rslt = adminMessageRepository.findFirstByOrderByIdDesc();
        logger.info("get last admin message: " + rslt);
        return adminMessageMapper.map(rslt);
    }

    @Override
    public Page<AdminMessageDto> getPageAdminMessage(int page) {
        logger.info("request fo admin message pag " + page);
        Pageable pageable = PageRequest.of((page - 1) * 30, page * 30);//, Sort.by("text")
        return adminMessageMapper.map(adminMessageRepository.findAllByOrderByIdDesc(pageable));
    }

    @Override
    public List<AdminMessageDto> getAllAdminMessage() {
        return adminMessageMapper.map(adminMessageRepository.findAll());
    }

    @Override
    public AdminMessageDto getAdminMessageById(Long id) {
        return adminMessageMapper.map(adminMessageRepository.findById(id).orElseThrow(new MyNotFoundException("admin message with id " + id + " not found")));
    }

    @Override
    public void addUserComment(UserCommentDto userCommentDto, Long adminMessageId) {
        AdminMessageTo adminMessage = adminMessageRepository.findById(adminMessageId).orElseThrow(new MyNotFoundException("admin message b id " + adminMessageId + " not exist"));
        UserCommentTo userCommentTo= userCommentMapper.map(userCommentDto);
        userCommentTo.setDate(LocalDateTime.now());
        try {
            userCommentTo.setMessage(URLDecoder.decode(userCommentTo.getMessage(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        userCommentTo.setMsgowner(adminMessage);
        userCommentRepository.save(userCommentTo);
    }

    @Override
    public void addUserComment(UserCommentDto userCommentDto) {
        userCommentRepository.save(userCommentMapper.map(userCommentDto));
    }

    @Override
    public int deleteUserCommentById(long id) {
        return userCommentRepository.deleteByIdCont(id);
    }

    @Override
    public int deleteAdminMessageById(long id) {
        return adminMessageRepository.deleteByIdCount(id);
    }
}
