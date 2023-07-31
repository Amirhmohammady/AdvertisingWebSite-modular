package com.mycompany.advertising.service;

import com.mycompany.advertising.api.AdvertiseService;
import com.mycompany.advertising.api.StorageService;
import com.mycompany.advertising.api.dto.AdvertiseDto;
import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.api.enums.AdvertiseStatus;
import com.mycompany.advertising.api.enums.Role;
import com.mycompany.advertising.repository.AdvertiseRepository;
import com.mycompany.advertising.repository.entity.AdvertiseTo;
import com.mycompany.advertising.service.mapper.AdvertiseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 10/28/2019.
 */
@Service
public class AdvertiseServiceImpl implements AdvertiseService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    AdvertiseMapper advertiseMapper;
    @Autowired
    StorageService storageService;
    /*@Autowired
    AuthenticationFacade authenticationFacade;*/
    @Autowired
    AdvertiseRepository advertiseRepository;
    @Value("${advertises.per.index.page}")
    private int advPerPage;
    private int unAcceptedAdvPerPagee = 20;

    @Override
    public Page<AdvertiseDto> getPageAcceptedAdvertises(int page) {
        Pageable pageable = PageRequest.of(page - 1, advPerPage);//, Sort.by("text")
        Page<AdvertiseTo> result = advertiseRepository.findAllByStatusOrderByStartdateDesc(AdvertiseStatus.Accepted, pageable);//.getContent();
        logger.info("get advertises at page " + page + " reult: " + result.getTotalElements());
        return advertiseMapper.mapTotoDto(result);
    }

    @Override
    public Page<AdvertiseDto> getPageAcceptedAdvertises(int page, String search) {
        Pageable pageable = PageRequest.of(page - 1, advPerPage);//, Sort.by("text")
        //messageRepository.findAllByTextOrTelegramlink(search, search, pageable).getTotalPages();
        return advertiseMapper.mapTotoDto(advertiseRepository.findAllByStatusAndTextContainingOrTitleContainingOrderByStartdateDesc(AdvertiseStatus.Accepted, search, search, pageable));//.getContent();
    }

    @Override
    public AdvertiseDto saveAdvertise(AdvertiseDto advertise) {
        return advertiseMapper.map(advertiseRepository.save(advertiseMapper.map(advertise)));
        //return messageTo.getId();
    }

    @Override
    @Transactional
    public AdvertiseDto publishAdvertiseByUser(AdvertiseDto advertise) {
        advertise.setStatus(AdvertiseStatus.Not_Accepted);
        advertise.setStartdate(LocalDateTime.now());
        System.out.println(advertise);
        AdvertiseTo rslt = advertiseRepository.save(advertiseMapper.map(advertise));
        logger.info("user " + advertise.getUserDto().getUsername() + " successfully published advertise " + advertise.getId());
        return advertiseMapper.map(rslt);
    }

    @Override
    public void uploadImage(AdvertiseDto advertise, MultipartFile pic1) {
        if (pic1 != null && !pic1.isEmpty()) {
            try {
                List<URL> files;
                files = storageService.storeImage(pic1);
                advertise.setImageUrl1(files.get(0));
                advertise.setSmallImageUrl1(files.get(1));
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("couldn't upload image " + pic1.getOriginalFilename() + " because of " + e.getMessage());
            }
        }
    }

    @Override
    public List<AdvertiseDto> getAllAdvertises() {
        return advertiseMapper.mapTotoDto(advertiseRepository.findAll());
    }

    @Override
    public Optional<AdvertiseDto> getAdvertiseById(Long id) {
        return advertiseMapper.mapTotoDto(advertiseRepository.findById(id));
    }

    @Override
    public Optional<AdvertiseDto> acceptAdvertiseById(Long id, UserDto admin) {
        logger.info("Admin id:" + admin.getId() + " try to accept advertise id:" + id);
        AdvertiseTo advertise = advertiseRepository.getOne(id);
        if (advertise == null || advertise.getStatus() != AdvertiseStatus.Not_Accepted) {
            logger.warn("can not accept advertise by id:" + id);
            return Optional.empty();
        }
        advertise.setStatus(AdvertiseStatus.Accepted);
        advertise.setStartdate(LocalDateTime.now());
        advertise.setExpiredate(LocalDateTime.now().plusDays(14));
        logger.info("advertise by id:" + id + " accepted successfully");
        return Optional.of(advertiseMapper.map(advertiseRepository.save(advertise)));
    }

    @Override
    public Optional<AdvertiseDto> rejectAdvertiseById(Long id, UserDto admin) {
        logger.info("Admin id:" + admin.getId() + " try to reject advertise id:" + id);
        AdvertiseTo advertise = advertiseRepository.getOne(id);
        if (advertise == null || advertise.getStatus() != AdvertiseStatus.Not_Accepted) {
            logger.warn("can not reject advertise by id:" + id);
            return Optional.empty();
        }
        advertise.setStatus(AdvertiseStatus.Rejected);
        advertise.setStartdate(LocalDateTime.now());
        logger.info("advertise by id:" + id + " rejected successfully");
        return Optional.of(advertiseMapper.map(advertiseRepository.save(advertise)));
    }

    @Override
    public Page<AdvertiseDto> getPageNotAcceptedAdvertises(int page) {
        Pageable pageable = PageRequest.of(page - 1, unAcceptedAdvPerPagee);//, Sort.by("text")
        Page<AdvertiseTo> result = advertiseRepository.findAllByStatusOrderByStartdateAsc(AdvertiseStatus.Not_Accepted, pageable);//.getContent();
        logger.info("get unaccepted advertises at page " + page + " reult: " + result.getTotalElements());
        return advertiseMapper.mapTotoDto(result);
    }
    /*public List<MessageTo> getMessagesById(Long id) {
        return messageRepository.findAllById(id);
    }*/

    @Override
    @Transactional
    public int deleteAdvertiseById(Long id, UserDto user) {
        Optional<AdvertiseTo> advertise = advertiseRepository.findById(id);
        if (advertise.isPresent()) {
            if (user.hasRole(Role.ROLE_ADMIN)) {
                advertise.get().setStatus(AdvertiseStatus.Deleted_By_Admin);
                return 1;
            } else if (user.hasRole(Role.ROLE_USER)) {
                advertise.get().setStatus(AdvertiseStatus.Deleted_By_User);
                return 1;
            }
        }
        return 0;
        //return advertiseRepository.deleteByIdCount(id);
    }

    @Override
    public Optional<AdvertiseDto> findByImage(String imageUrl) {
        return advertiseMapper.mapTotoDto(advertiseRepository.findByImageUrl1OrSmallImageUrl1(imageUrl, imageUrl));
    }

    @Override
    public List<AdvertiseDto> findAllByDomainNames(List<String> domainNames) {
        return advertiseMapper.mapTotoDto(advertiseRepository.findByImageUrl1Containing_Costum(domainNames));
    }
}
