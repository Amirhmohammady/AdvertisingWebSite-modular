package com.mycompany.advertising.service.mapper;

import com.mycompany.advertising.api.dto.AdvertiseDto;
import com.mycompany.advertising.repository.entity.AdvertiseTo;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 7/7/2023.
 */
@Mapper
public interface AdvertiseMapper {
    AdvertiseTo map(AdvertiseDto inp);
    AdvertiseDto map(AdvertiseTo inp);
    List<AdvertiseTo> mapDtotoTo(List<AdvertiseDto> inp);
    List<AdvertiseDto> mapTotoDto(List<AdvertiseTo> inp);
    default Optional<AdvertiseTo> mapDtotoTo(Optional<AdvertiseDto> inp){
        return inp.map(this::map);
    }
    default Optional<AdvertiseDto> mapTotoDto(Optional<AdvertiseTo> inp){
        return inp.map(this::map);
    }
    default Page<AdvertiseDto> mapTotoDto(Page<AdvertiseTo> inp) {
        List<AdvertiseDto> userDtos = mapTotoDto(inp.getContent());
        return new PageImpl<>(userDtos, inp.getPageable(), inp.getTotalElements());
    }
}
