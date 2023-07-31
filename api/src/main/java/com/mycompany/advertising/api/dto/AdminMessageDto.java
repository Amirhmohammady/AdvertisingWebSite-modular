package com.mycompany.advertising.api.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Amir on 7/5/2023.
 */
public class AdminMessageDto {
    private Long id;
    private UserDto owner;
    private List<UserCommentDto> comments;
    private String message;
    private String title;
    private LocalDateTime date;
    private int messageCnt = 0;

    public AdminMessageDto() {
    }

    public AdminMessageDto(UserDto owner, String message, LocalDateTime date) {
        this();
        this.owner = owner;
        this.message = message;
        this.date = date;
    }

    public int getMessageCnt() {
        return messageCnt;
    }

    public void setMessageCnt(int messageCnt) {
        this.messageCnt = messageCnt;
    }

    public List<UserCommentDto> getComments() {
        return comments;
    }

    public void setComments(List<UserCommentDto> comments) {
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AdminMessageTo{" +
                "id=" + id +
                ", owner=" + owner.getUsername() +
                ", message='" + message + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                '}';
    }
}
