package com.mycompany.advertising.web.controller;

import com.mycompany.advertising.api.AdvCategoryService;
import com.mycompany.advertising.api.dto.AdvertiseCategoryDto;
import com.mycompany.advertising.api.language.LngManager;
import com.mycompany.advertising.api.utils.CategoryIdPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by Amir on 6/27/2022.
 */
@RestController
@RequestMapping("/api")
public class AdvCategoryRestController {
    @Autowired
    AdvCategoryService advCategoryService;

    @PostMapping("/advCategory")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<AdvertiseCategoryDto> addCategory(@RequestBody AdvertiseCategoryDto category) {
        AdvertiseCategoryDto rslt = advCategoryService.addCategory(category);
        if (rslt != null) return new ResponseEntity<>(rslt, HttpStatus.OK);
        else return new ResponseEntity<>((AdvertiseCategoryDto) null, HttpStatus.NOT_ACCEPTABLE);
    }

    @PatchMapping("/advCategory")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<AdvertiseCategoryDto> updateCategory(@RequestBody AdvertiseCategoryDto category) {
        AdvertiseCategoryDto advCategory = advCategoryService.editCategory(category);
        if (advCategory != null)
            return new ResponseEntity<>(advCategory, HttpStatus.OK);
        return new ResponseEntity<>((AdvertiseCategoryDto) null, HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/advCategory")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> deleteCategory(@RequestParam(required = true) Long id) {
        int rows = advCategoryService.deleteByIdCostum(id);
        if (rows > 0) return new ResponseEntity<>("Advertise category deleted successfully", HttpStatus.OK);
        return new ResponseEntity<>("failed to delete Advertise category", HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/advCategories")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<List<CategoryIdPair>> getCategoriesByParentId(@RequestParam(required = true) Long parentId,
                                                                        @RequestParam(required = true) String lan) {
        return new ResponseEntity<>(advCategoryService.getChildsByLanguageAndId(LngManager.whatLanguage(lan), parentId), HttpStatus.OK);
    }

    @GetMapping("/advCategory")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<AdvertiseCategoryDto> getCategoryById(@RequestParam(required = true) Long id) {
        Optional<AdvertiseCategoryDto> category = advCategoryService.getCategoryById(id);
        if (category.isPresent())
            return new ResponseEntity<>(category.get(), HttpStatus.OK);
        return new ResponseEntity<>((AdvertiseCategoryDto) null, HttpStatus.NOT_ACCEPTABLE);
    }
}
