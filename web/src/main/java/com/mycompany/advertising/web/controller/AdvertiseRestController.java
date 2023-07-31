package com.mycompany.advertising.web.controller;

import com.mycompany.advertising.api.AdvertiseService;
import com.mycompany.advertising.api.AuthenticationFacadeService;
import com.mycompany.advertising.api.OnlineAdvertiseDataService;
import com.mycompany.advertising.api.dto.AdvertiseDto;
import com.mycompany.advertising.api.utils.OnlineAdvertiseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 6/5/2022.
 */
@RestController
@RequestMapping("/api")
public class AdvertiseRestController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    OnlineAdvertiseDataService onlineService;
    @Autowired
    AuthenticationFacadeService authenticationFacade;
    @Autowired
    AdvertiseService advertiseService;

    @Secured({"ROLE_ADMIN"})
    @PatchMapping(value = "/advertises/acceptbyid={id}")
    public ResponseEntity<String> acceptAdvertise(@PathVariable long id) {
        Optional<AdvertiseDto> adv = advertiseService.acceptAdvertiseById(id, authenticationFacade.getCurrentUser());
        if (adv.isPresent()) return new ResponseEntity<>("Advertise accepted successfully", HttpStatus.OK);
        else return new ResponseEntity<>("Can not accept advertise", HttpStatus.NOT_ACCEPTABLE);
    }

    @Secured({"ROLE_ADMIN"})
    @PatchMapping(value = "/advertises/rejectbyid={id}")
    public ResponseEntity<String> rejectAdvertise(@PathVariable long id) {
        Optional<AdvertiseDto> adv = advertiseService.rejectAdvertiseById(id, authenticationFacade.getCurrentUser());
        if (adv.isPresent()) return new ResponseEntity<>("Advertise rejected successfully", HttpStatus.OK);
        else return new ResponseEntity<>("Can not reject advertise", HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/advertises")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<String>> addAdvertise(@RequestParam(required = false) MultipartFile pic1,
                                                     @Valid AdvertiseDto advertise) {
        List<String> succsessMessage = new ArrayList<>();
        advertiseService.uploadImage(advertise, pic1);
        if (advertise.getImageUrl1() != null) succsessMessage.add("Image successfully uploaded");
        else succsessMessage.add("Image didn't upload");
        advertise.setUserDto(authenticationFacade.getCurrentUser());
        AdvertiseDto rslt = advertiseService.publishAdvertiseByUser(advertise);
        succsessMessage.add("advertise successfully sent and waiting for accepting by admin");
        logger.info("user " + advertise.getUserDto().getUsername() + " successfully published advertise " + rslt.getId());
        return new ResponseEntity<>(succsessMessage, HttpStatus.OK);
    }

    @GetMapping("/advertises/onlineData")
    ResponseEntity<OnlineAdvertiseData> getOnlineData(@RequestParam URL url) {
        try {
            return new ResponseEntity<>(onlineService.getData(url), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>((OnlineAdvertiseData) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/advertises")
    ResponseEntity<Page<AdvertiseDto>> getAdvertiseByPage(@RequestParam int page) {
        return new ResponseEntity<>(advertiseService.getPageAcceptedAdvertises(page), HttpStatus.OK);
    }
}
