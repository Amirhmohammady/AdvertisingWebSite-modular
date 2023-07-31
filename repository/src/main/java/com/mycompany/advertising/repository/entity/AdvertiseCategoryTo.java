package com.mycompany.advertising.repository.entity;

import com.mycompany.advertising.api.enums.Language;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

/**
 * Created by Amir on 6/23/2022.
 */
@Entity
public class AdvertiseCategoryTo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = true)
    @JoinColumn(nullable = true)
    private AdvertiseCategoryTo parent;
    @ElementCollection
    @JoinColumn()//reqired for OnDelete
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Map<Language, String> category;
    //@ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "categories", fetch = FetchType.LAZY)
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<AdvertiseTo> advertiseTos;

    @Override
    public String toString() {
        return "AdvertiseCategoryTo{" +
                "id=" + id +
                ", category=" + category +
                ", parent=" + (parent != null ? parent.getId() : null) +
                '}';
    }

    public Set<AdvertiseTo> getAdvertiseTos() {
        return advertiseTos;
    }

    public void setAdvertiseTos(Set<AdvertiseTo> advertiseTos) {
        this.advertiseTos = advertiseTos;
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

    public AdvertiseCategoryTo getParent() {
        return parent;
    }

    public void setParent(AdvertiseCategoryTo parent) {
        this.parent = parent;
    }

}
