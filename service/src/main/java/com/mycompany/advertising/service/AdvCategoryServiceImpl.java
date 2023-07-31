package com.mycompany.advertising.service;

import com.mycompany.advertising.api.AdvCategoryService;
import com.mycompany.advertising.api.dto.AdvertiseCategoryDto;
import com.mycompany.advertising.api.enums.Language;
import com.mycompany.advertising.api.utils.CategoryIdPair;
import com.mycompany.advertising.repository.AdvCategoryRepository;
import com.mycompany.advertising.repository.entity.AdvertiseCategoryTo;
import com.mycompany.advertising.service.mapper.AdvCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 6/28/2022.
 */
@Service
public class AdvCategoryServiceImpl implements AdvCategoryService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    AdvCategoryMapper advCategoryMapper;
    @Autowired
    AdvCategoryRepository advCategoryRepository;

    @Override
    @Transactional
    public AdvertiseCategoryDto editCategory(AdvertiseCategoryDto category) {
        logger.info("try to edit category" + category);
        Optional<AdvertiseCategoryTo> advTo = advCategoryRepository.findById(category.getId());
        if (!advTo.isPresent()) return null;
        advTo.get().getCategory().keySet().forEach(e->{if(category.getCategory().get(e)!=null) advTo.get().getCategory().put(e,category.getCategory().get(e));});

        //advTo.get().setCategory(category.getCategory());
        logger.info("try to edit category222222" + advTo);
        return advCategoryMapper.map(advCategoryRepository.save(advTo.get()));
    }

    @Override
    @Transactional
    public AdvertiseCategoryDto addCategory(AdvertiseCategoryDto category) {
//        for (int i = 0; i < category.getCategory().size(); i++)
//            category.getCategory().get(i).setAdvertiseCategory(category);
        return advCategoryMapper.map(advCategoryRepository.save(advCategoryMapper.map(category)));
    }

    @Override
    public Optional<AdvertiseCategoryDto> getCategoryById(Long id) {
        return advCategoryMapper.mapTotoDto(advCategoryRepository.findById(id));
    }

    @Override
    public boolean existsById(Long id) {
        return advCategoryRepository.existsById(id);
    }

    @Override
    @Transactional
    public int deleteByIdCostum(Long id) {
        return advCategoryRepository.deleteByIdCostum(id);
    }

    @Override
    public List<AdvertiseCategoryDto> getRootCtegories() {
        return advCategoryMapper.mapTotoDto(advCategoryRepository.findByParent(null));
    }

    @Override
    public List<CategoryIdPair> getRootCtegories(Language language) {
        List<CategoryIdPair> rslt = advCategoryRepository.getChildsByLanguageByNullId(language);
        logger.info("Root Categories in " + language + " are: " + rslt);
        return rslt;
    }

    @Override
    public List<AdvertiseCategoryDto> getChildCtegories(Long parentId) {
        return advCategoryMapper.mapTotoDto(advCategoryRepository.findByParent_Id(parentId));
    }

    @Override
    public List<CategoryIdPair> getChildsByLanguageAndId(Language language, Long pid) {
        if (pid == 0) return getRootCtegories(language);
        return advCategoryRepository.getChildsByLanguageAndId(language, pid);
    }
}
