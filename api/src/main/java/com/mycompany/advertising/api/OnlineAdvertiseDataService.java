package com.mycompany.advertising.api;

import com.mycompany.advertising.api.utils.OnlineAdvertiseData;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Amir on 8/7/2022.
 */
public interface OnlineAdvertiseDataService {
    OnlineAdvertiseData getData(URL url) throws IOException;
}
