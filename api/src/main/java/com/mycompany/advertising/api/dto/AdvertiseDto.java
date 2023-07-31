package com.mycompany.advertising.api.dto;


import com.mycompany.advertising.api.enums.AdvertiseStatus;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Amir on 10/28/2019.
 */
//@Proxy(lazy=false)
public class AdvertiseDto {
    private Long id;
    private UserDto userDto;
    @Size(max = 64, message = "title excited than 64 characters")
    @Size(min = 5, message = "title lenght should be at least 5 chars")
    private String title;
    @Size(max = 512, message = "text excited than 512 characters")
    private String text;
    private URL webSiteLink;
    private LocalDateTime startdate;
    private LocalDateTime expiredate;
    @NotNull(message = "you should put a valid URL")
    private URL imageUrl1;
    private URL smallImageUrl1;
    private AdvertiseStatus status;
    @Size(min = 0, max = 10)
    private List<AdvertiseCategoryDto> categories;

    public AdvertiseDto() {
    }

    public List<AdvertiseCategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<AdvertiseCategoryDto> categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AdvertiseStatus getStatus() {
        return status;
    }

    public void setStatus(AdvertiseStatus status) {
        this.status = status;
    }

    public URL getSmallImageUrl1() {
        return smallImageUrl1;
    }

    public void setSmallImageUrl1(URL smallImageUrl1) {
        this.smallImageUrl1 = smallImageUrl1;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public LocalDateTime getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(LocalDateTime expiredate) {
        this.expiredate = expiredate;
    }

    public URL getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(URL imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public LocalDateTime getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDateTime startdate) {
        this.startdate = startdate;
    }

    public URL getWebSiteLink() {
        return webSiteLink;
    }

    public void setWebSiteLink(URL webSiteLink) {
        this.webSiteLink = webSiteLink;
    }

    @Override
    public String toString() {
        return "AdvertiseTo{" +
                "id=" + id +
                ", userDto=" + (userDto != null ? userDto.getUsername() : "null") +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", webSiteLink=" + webSiteLink +
                ", startdate=" + startdate +
                ", expiredate=" + expiredate +
                ", imageUrl1=" + imageUrl1 +
                ", smallImageUrl1=" + smallImageUrl1 +
                ", status=" + status +
                ", categories=" + categories +
                '}';
    }
}
