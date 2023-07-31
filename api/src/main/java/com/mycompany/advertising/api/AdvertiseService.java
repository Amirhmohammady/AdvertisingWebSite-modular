package com.mycompany.advertising.api;

import com.mycompany.advertising.api.dto.AdvertiseDto;
import com.mycompany.advertising.api.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 11/10/2019.
 */
public interface AdvertiseService {

    AdvertiseDto saveAdvertise(AdvertiseDto advertise);

    AdvertiseDto publishAdvertiseByUser(AdvertiseDto advertise);

    void uploadImage(AdvertiseDto advertise, MultipartFile pic1);

    List<AdvertiseDto> getAllAdvertises();

    Optional<AdvertiseDto> getAdvertiseById(Long id);

    Optional<AdvertiseDto> acceptAdvertiseById(Long id, UserDto admin);

    Optional<AdvertiseDto> rejectAdvertiseById(Long id, UserDto admin);

    Page<AdvertiseDto> getPageNotAcceptedAdvertises(int page);

    Page<AdvertiseDto> getPageAcceptedAdvertises(int page);

    Page<AdvertiseDto> getPageAcceptedAdvertises(int page, String search);

    int deleteAdvertiseById(Long id, UserDto user);

    Optional<AdvertiseDto> findByImage(String imageUrl);

    List<AdvertiseDto> findAllByDomainNames(List<String> domainNames);

    //Optional<AdvertiseTo> editAdvertises(AdvertiseTo advertise);
}
