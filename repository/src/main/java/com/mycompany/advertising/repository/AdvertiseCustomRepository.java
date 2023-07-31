package com.mycompany.advertising.repository;



import com.mycompany.advertising.repository.entity.AdvertiseTo;

import java.util.List;

/**
 * Created by Amir on 10/7/2022.
 */
public interface AdvertiseCustomRepository {
    List<AdvertiseTo> findByImageUrl1Containing_Costum(List<String> domainNames);
}
