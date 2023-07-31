package com.mycompany.advertising.api;

import com.mycompany.advertising.api.dto.AdvertiseCategoryDto;
import com.mycompany.advertising.api.utils.CategoryIdPair;
import com.mycompany.advertising.api.enums.Language;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 6/27/2022.
 */
public interface AdvCategoryService {
    AdvertiseCategoryDto editCategory(AdvertiseCategoryDto category);

    AdvertiseCategoryDto addCategory(AdvertiseCategoryDto category);

    Optional<AdvertiseCategoryDto> getCategoryById(Long id);

    boolean existsById(Long id);

    int deleteByIdCostum(Long id);

    List<AdvertiseCategoryDto> getRootCtegories();

    List<CategoryIdPair> getRootCtegories(Language language);

    List<AdvertiseCategoryDto> getChildCtegories(Long parentId);

    List<CategoryIdPair> getChildsByLanguageAndId(Language language, Long id);
}
