package com.example.lock.aspect;

import java.lang.reflect.Method;

import com.example.lock.annotation.ChangeSetAnnotation;
import com.example.lock.exception.AlreadyLockedException;
import com.example.lock.service.ChangeLogService;
import com.example.lock.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ChangeSetAspect {

  private static final String CHANGEDATASET_LOCK = "CHANGEDATASET_LOCK";

  private static final int LOCK_EXPIRATION = 120;

  @Autowired
  MongoTemplate mongoTemplate;

  @Autowired
  LockService lockService;

  @Autowired
  ChangeLogService changeLogService;

  @Pointcut("within(com.example.lock..*) && @annotation(com.example.lock.annotation.ChangeSetAnnotation)")
  public void hasChangeLogAnnotation(){
  }

  @Around("hasChangeLogAnnotation()")
  public Object changeSet(ProceedingJoinPoint joinPoint) throws Throwable {
    ChangeSetAnnotation changeSetAnnotation = this.getChangeSetAnnotation((MethodSignature) joinPoint.getSignature());
    String token = lockService.acquire(CHANGEDATASET_LOCK, LOCK_EXPIRATION);
    if (token == null) {
      return null;
    }

    String author = changeSetAnnotation.author();
    String changeId = changeSetAnnotation.changeId();
    String changeLogResult = changeLogService.block(author, changeId);
    if (changeLogResult == null) {
      log.error("There was an error setting up the changelog data");
      //TODO: Throw exception if ChangeLogService don't do it
      return null;
    }
    log.debug("Changelog result: {}", changeLogResult);

    //Execute the annotation
    Object result = joinPoint.proceed();

    changeLogService.release(changeLogResult);
    lockService.release(CHANGEDATASET_LOCK, token);
    return result;
  }

  ChangeSetAnnotation getChangeSetAnnotation(MethodSignature signature) {
    Method method = signature.getMethod();
    return method.getAnnotation(ChangeSetAnnotation.class);
  }
}
