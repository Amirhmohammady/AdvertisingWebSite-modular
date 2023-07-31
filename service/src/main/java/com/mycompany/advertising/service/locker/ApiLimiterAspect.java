package com.mycompany.advertising.service.locker;

import com.mycompany.advertising.api.AuthenticationFacadeService;
import com.mycompany.advertising.api.dto.UserDto;
import com.mycompany.advertising.api.locker.annotations.LockApiByIP;
import com.mycompany.advertising.api.locker.annotations.LockApiByUser;
import com.mycompany.advertising.api.locker.annotations.LockApiByVariable;
import com.mycompany.advertising.api.locker.annotations.TimeLimiter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amir on 8/3/2022.
 */
@Aspect
@Component
public class ApiLimiterAspect {
    @Autowired
    AuthenticationFacadeService authenticationFacade;
    @Autowired
    LockerApiStorageService lockerApiService;
    private Map<Method, Integer> lockApiByVariableIndexes = new HashMap<>();

    @Before("@annotation(com.mycompany.advertising.api.locker.annotations.LockApiByUser)")
    public void lockApiByUser(JoinPoint joinPoint) throws Throwable {//ProceedingJoinPoint
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LockApiByUser lock = method.getAnnotation(LockApiByUser.class);
        TimeLimiter timeLimiter = lock.timeLimiter();
        UserDto user = authenticationFacade.getCurrentUser();
        long watingTime = lockerApiService.getMethodTimeWaitByUser(method, user, timeLimiter.maxRequest(), timeLimiter.inSeconds());
        if (watingTime > 0) {
            throw lock.excptionType().getDeclaredConstructor(String.class).newInstance(lock.exceptionMsg());
            /*if (lock.waitOrErr() == LockerWaitType.WAIT) Thread.sleep(watingTime * 1000);
            else {
                if (lock.returnType() == ReturnType.JSON)
                    throw new CallRestApiLimitException("time limit exceeded to call " + method.getName());
                else throw new CallWebApiLimitException("time limit exceeded to call " + method.getName());
            }*/
        }
        lockerApiService.saveApiCalledWithUserLimit(method, user);
    }

    @Before("@annotation(com.mycompany.advertising.api.locker.annotations.LockApiByIP)")
    public void lockApiByVariable(JoinPoint joinPoint) throws Throwable {//ProceedingJoinPoint
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LockApiByVariable lock = method.getAnnotation(LockApiByVariable.class);
        TimeLimiter timeLimiter = lock.timeLimiter();
        if (lockApiByVariableIndexes.get(method) == null) {
            LockApiByVariable annotation = method.getAnnotation(LockApiByVariable.class);
            String parameterName = annotation.variableName();
            int idx = Arrays.asList(signature.getParameterNames()).indexOf(parameterName);
            //Arrays.asList(signature.getParameterNames()).forEach(System.out::println);
            lockApiByVariableIndexes.put(method, idx);
        }
        String value = joinPoint.getArgs()[lockApiByVariableIndexes.get(method)].toString();
        long watingTime = lockerApiService.getMethodTimeWaitByVariable(method, value, timeLimiter.maxRequest(), timeLimiter.inSeconds());
        if (watingTime > 0) {
            throw lock.excptionType().getDeclaredConstructor(String.class).newInstance(lock.exceptionMsg());
            /*if (lock.waitOrErr() == LockerWaitType.WAIT) Thread.sleep(watingTime * 1000);
            else {
                if (lock.returnType() == ReturnType.JSON)
                    throw new CallRestApiLimitException("time limit exceeded to call " + method.getName());
                else throw new CallWebApiLimitException("time limit exceeded to call " + method.getName());
            }*/
        }
        lockerApiService.saveApiCalledWithVariableLimit(method, value);
    }

    @Before("@annotation(com.mycompany.advertising.api.locker.annotations.LockApiByIP)")
    public void lockApiByIP(JoinPoint joinPoint) throws Throwable {//ProceedingJoinPoint
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LockApiByIP lock = method.getAnnotation(LockApiByIP.class);
        TimeLimiter timeLimiter = lock.timeLimiter();
        String IP = authenticationFacade.getCurrentClientIp();
        long watingTime = lockerApiService.getMethodTimeWaitByIP(method, IP, timeLimiter.maxRequest(), timeLimiter.inSeconds());
        if (watingTime > 0) {
            throw lock.excptionType().getDeclaredConstructor(String.class).newInstance(lock.exceptionMsg());
            /*if (lock.waitOrErr() == LockerWaitType.WAIT) Thread.sleep(watingTime * 1000);
            else {
                if (lock.returnType() == ReturnType.JSON)
                    throw new CallRestApiLimitException("time limit exceeded to call " + method.getName());
                else throw new CallWebApiLimitException("time limit exceeded to call " + method.getName());
            }*/
        }
        lockerApiService.saveApiCalledWithIPLimit(method, IP);
    }
}
