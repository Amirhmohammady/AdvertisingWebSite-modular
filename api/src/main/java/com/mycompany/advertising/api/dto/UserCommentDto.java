package com.mycompany.advertising.api.dto;

import java.time.LocalDateTime;

/**
 * Created by Amir on 2/1/2022.
 */
public class UserCommentDto {
    private Long id;
    private AdminMessageDto msgowner;
    private String message;
    private String name;
    private LocalDateTime date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdminMessageDto getMsgowner() {
        return msgowner;
    }

    public void setMsgowner(AdminMessageDto msgowner) {
        this.msgowner = msgowner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
