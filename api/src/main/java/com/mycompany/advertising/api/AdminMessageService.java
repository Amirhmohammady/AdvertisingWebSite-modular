package com.mycompany.advertising.api;

import com.mycompany.advertising.api.dto.AdminMessageDto;
import com.mycompany.advertising.api.dto.UserCommentDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Amir on 1/27/2022.
 */
public interface AdminMessageService {
    AdminMessageDto addAdminMessage(AdminMessageDto adminMessage);

    AdminMessageDto getLastMessage();

    Page<AdminMessageDto> getPageAdminMessage(int page);

    List<AdminMessageDto> getAllAdminMessage();

    AdminMessageDto getAdminMessageById(Long id);

    void addUserComment(UserCommentDto userCommentTo, Long adminMessageId);

    void addUserComment(UserCommentDto userCommentTo);

    int deleteUserCommentById(long id);

    int deleteAdminMessageById(long id);
}
