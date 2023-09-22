package com.ffreaky.apigw.authentication;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
public class HideCredentialsInLogsAspect {

    private static final Logger logger = LoggerFactory.getLogger(HideCredentialsInLogsAspect.class);

    @Before(value = "@annotation(com.ffreaky.apigw.authentication.HideCredentials)")
    public void hideCredentialsInLogs(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                recursivelyHideCredentials(arg);
            }
        }
        logger.info("Method {} called with arguments: {}", joinPoint.getSignature().getName(), args);
    }

    private void recursivelyHideCredentials(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            boolean originalAccessible = field.canAccess(obj);
            field.setAccessible(true);
            try {
                if (field.isAnnotationPresent(HideCredentials.class) && field.getType() == String.class) {
                    field.set(obj, "*****");
                } else if (field.getType().getPackage().getName().startsWith("com.ffreaky")) {
                    recursivelyHideCredentials(field.get(obj));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                field.setAccessible(originalAccessible);
            }
        }
    }
}
