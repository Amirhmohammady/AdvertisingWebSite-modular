package com.mycompany.advertising.service.locker;

import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.api.locker.annotations.LockApiByIP;
import com.mycompany.advertising.api.locker.annotations.LockApiByUser;
import com.mycompany.advertising.api.locker.annotations.LockApiByVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir on 8/6/2022.
 */
@Service
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class LockerApiStorageServiceImpl implements LockerApiStorageService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private List<LockApiByIPinfo> lockApiByIPinfos = new ArrayList<>();
    private List<LockApiByUserinfo> lockApiByUserinfos = new ArrayList<>();
    private List<LockApiByVariableinfo> lockApiByVariableinfos = new ArrayList<>();

    @Override
    public long getMethodTimeWaitByIP(Method method, String IP, int maxReq, int inSec) {
        LocalDateTime tempTime = LocalDateTime.now().minusSeconds(inSec);
        LocalDateTime lastCall = LocalDateTime.now();
        int methodCalls = 0;
        for (int i = lockApiByIPinfos.size() - 1; i >= 0; i--) {
            if (lockApiByIPinfos.get(i).getTime().isAfter(tempTime)) {
                if (lockApiByIPinfos.get(i).getIP().equals(IP) && lockApiByIPinfos.get(i).getMethod().equals(method)) {
                    methodCalls++;
                    if (lockApiByIPinfos.get(i).getTime().isBefore(lastCall))
                        lastCall = lockApiByIPinfos.get(i).getTime();
                }
            } else break;
        }
        long result;
        if (methodCalls < maxReq) result = 0;
        else result = inSec - Duration.between(lastCall, LocalDateTime.now()).getSeconds();
        logger.info("method " + method + " called " + methodCalls + " times by IP " + IP + " in last " + inSec + " seconds and you need to wait " + result + " seconds");
        return result;
    }

    @Override
    public long getMethodTimeWaitByUser(Method method, UserDto user, int maxReq, int inSec) {
        LocalDateTime tempTime = LocalDateTime.now().minusSeconds(inSec);
        LocalDateTime lastCall = LocalDateTime.now();
        int methodCalls = 0;
        for (int i = lockApiByUserinfos.size() - 1; i >= 0; i--) {
            if (lockApiByUserinfos.get(i).getTime().isAfter(tempTime)) {
                if (lockApiByUserinfos.get(i).getUser() == user && lockApiByUserinfos.get(i).getMethod().equals(method)) {
                    methodCalls++;
                    if (lockApiByUserinfos.get(i).getTime().isBefore(lastCall))
                        lastCall = lockApiByUserinfos.get(i).getTime();
                }
            } else break;
        }
        long result;
        if (methodCalls < maxReq) result = 0;
        else result = inSec - Duration.between(lastCall, LocalDateTime.now()).getSeconds();
        logger.info("method " + method + " called " + methodCalls + " times by User " + user + " in last " + inSec + " seconds and you need to wait " + result + " seconds");
        return result;
    }

    @Override
    public long getMethodTimeWaitByVariable(Method method, String var, int maxReq, int inSec) {
        LocalDateTime tempTime = LocalDateTime.now().minusSeconds(inSec);
        LocalDateTime lastCall = LocalDateTime.now();
        int methodCalls = 0;
        for (int i = lockApiByVariableinfos.size() - 1; i >= 0; i--) {
            if (lockApiByVariableinfos.get(i).getTime().isAfter(tempTime)) {
                if (lockApiByVariableinfos.get(i).getValue().equals(var) && lockApiByVariableinfos.get(i).getMethod().equals(method)) {
                    methodCalls++;
                    if (lockApiByVariableinfos.get(i).getTime().isBefore(lastCall))
                        lastCall = lockApiByVariableinfos.get(i).getTime();
                }
            } else break;
        }
        long result;
        if (methodCalls < maxReq) result = 0;
        else result = inSec - Duration.between(lastCall, LocalDateTime.now()).getSeconds();
        logger.info("method " + method + " called " + methodCalls + " times by value " + var + " in last " + inSec + " seconds and you need to wait " + result + " seconds");
        return result;
    }

    @Override
    public void saveApiCalledWithIPLimit(Method method, String IP) {
        LockApiByIPinfo lockByIP = new LockApiByIPinfo();
        lockByIP.setMethod(method);
        lockByIP.setTime(LocalDateTime.now());
        lockByIP.setIP(IP);
        lockApiByIPinfos.add(lockByIP);
    }

    @Override
    public void saveApiCalledWithUserLimit(Method method, UserDto user) {
        LockApiByUserinfo lockByUser = new LockApiByUserinfo();
        lockByUser.setMethod(method);
        lockByUser.setTime(LocalDateTime.now());
        lockByUser.setUser(user);
        lockApiByUserinfos.add(lockByUser);
    }

    @Override
    public void saveApiCalledWithVariableLimit(Method method, String var) {
        LockApiByVariableinfo lockByVar = new LockApiByVariableinfo();
        lockByVar.setMethod(method);
        lockByVar.setTime(LocalDateTime.now());
        lockByVar.setValue(var);
        lockApiByVariableinfos.add(lockByVar);
    }

    @Override
    public void deleteAllExpiredLocker() {
        int i;
        for (i = 0; i < lockApiByIPinfos.size(); i++) {
            LockApiByIP annotation = lockApiByIPinfos.get(i).getMethod().getDeclaredAnnotation(LockApiByIP.class);
            if (Duration.between(lockApiByIPinfos.get(i).getTime(), LocalDateTime.now()).getSeconds() > annotation.timeLimiter().inSeconds()) {
                lockApiByIPinfos.remove(i);
                i--;
            }
        }
        logger.debug(String.valueOf(i) + " saved methods locked by IP deleted from memory");
        for (i = 0; i < lockApiByUserinfos.size(); i++) {
            LockApiByUser annotation = lockApiByUserinfos.get(i).getMethod().getDeclaredAnnotation(LockApiByUser.class);
            if (Duration.between(lockApiByUserinfos.get(i).getTime(), LocalDateTime.now()).getSeconds() > annotation.timeLimiter().inSeconds()) {
                lockApiByUserinfos.remove(i);
                i--;
            }
        }
        logger.debug(String.valueOf(i) + " saved methods locked by user deleted from memory");
        for (i = 0; i < lockApiByVariableinfos.size(); i++) {
            LockApiByVariable annotation = lockApiByVariableinfos.get(i).getMethod().getDeclaredAnnotation(LockApiByVariable.class);
            if (Duration.between(lockApiByVariableinfos.get(i).getTime(), LocalDateTime.now()).getSeconds() > annotation.timeLimiter().inSeconds()) {
                lockApiByVariableinfos.remove(i);
                i--;
            }
        }
        logger.debug(String.valueOf(i) + " saved methods locked by variable deleted from memory");
    }

    @Override
    public void removeLastLockByVariable(Method method, String var) {
        int i;
        for (i = lockApiByVariableinfos.size() - 1; i >= 0; i--)
            if (lockApiByVariableinfos.get(i).getValue().equals(var) && lockApiByVariableinfos.get(i).getMethod().equals(method)) {
                lockApiByVariableinfos.remove(i);
                break;
            }
        if (i >= 0) logger.info("last call method " + method + " by variable " + var + " is removed");
        else logger.info("can not remove last call method " + method + " by variable " + var);
    }
}