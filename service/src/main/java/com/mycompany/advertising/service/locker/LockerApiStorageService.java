package com.mycompany.advertising.service.locker;

import com.mycompany.advertising.api.dto.UserDto;

import java.lang.reflect.Method;

/**
 * Created by Amir on 8/5/2022.
 */
public interface LockerApiStorageService {
    long getMethodTimeWaitByIP(Method method, String IP, int maxReq, int inSec);
    long getMethodTimeWaitByUser(Method method, UserDto user, int maxReq, int inSec);
    long getMethodTimeWaitByVariable(Method method, String var, int maxReq, int inSec);
    void saveApiCalledWithIPLimit(Method method, String IP);
    void saveApiCalledWithUserLimit(Method method, UserDto user);
    void saveApiCalledWithVariableLimit(Method method, String var);
    void deleteAllExpiredLocker();
    void removeLastLockByVariable(Method method, String var);
}
