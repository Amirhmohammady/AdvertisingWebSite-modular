package com.mycompany.advertising.service.mapper;

import com.mycompany.advertising.api.dto.AdvertiseCategoryDto;
import com.mycompany.advertising.repository.entity.AdvertiseCategoryTo;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 7/7/2023.
 */
@Mapper
public interface AdvCategoryMapper {
    AdvertiseCategoryTo map(AdvertiseCategoryDto inp);
    AdvertiseCategoryDto map(AdvertiseCategoryTo inp);
    List<AdvertiseCategoryTo> mapDtotoTo(List<AdvertiseCategoryDto> inp);
    List<AdvertiseCategoryDto> mapTotoDto(List<AdvertiseCategoryTo> inp);
    default Optional<AdvertiseCategoryTo> mapDtotoTo(Optional<AdvertiseCategoryDto> inp){
        return inp.map(this::map);
    }
    default Optional<AdvertiseCategoryDto> mapTotoDto(Optional<AdvertiseCategoryTo> inp){
        return inp.map(this::map);
    }
}
