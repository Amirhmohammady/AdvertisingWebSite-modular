package com.mycompany.advertising.api.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.advertising.api.enums.Language;

import java.util.Map;
import java.util.Set;

/**
 * Created by Amir on 6/23/2022.
 */
public class AdvertiseCategoryDto {
    private Long id;
    private AdvertiseCategoryDto parent;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Map<Language, String> category;
    @JsonIgnore
    private Set<AdvertiseDto> advertiseDtos;

    @Override
    public String toString() {
        return "AdvertiseCategoryTo{" +
                "id=" + id +
                ", category=" + category +
                ", parent=" + (parent != null ? parent.getId() : null) +
                '}';
    }

    public Set<AdvertiseDto> getAdvertiseDtos() {
        return advertiseDtos;
    }

    //com.mycompany.advertising.model.entity
    public void setAdvertiseDtos(Set<AdvertiseDto> advertiseDtos) {
        this.advertiseDtos = advertiseDtos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Language, String> getCategory() {
        return category;
    }

    public void setCategory(Map<Language, String> category) {
        this.category = category;
    }

    public AdvertiseCategoryDto getParent() {
        return parent;
    }

    public void setParent(AdvertiseCategoryDto parent) {
        this.parent = parent;
    }

}
