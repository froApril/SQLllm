package usyd.elec5620.sqlllm.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import usyd.elec5620.sqlllm.annotation.DS;
import usyd.elec5620.sqlllm.context.DynamicDataSourceContextHolder;

import java.util.Objects;

@Aspect
@Component
public class DynamicDataSourceAspect {

    @Pointcut("@annotation(usyd.elec5620.sqlllm.annotation.DS)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String dsKey = getDSAnnotation(joinPoint).value();
        DynamicDataSourceContextHolder.setContextKey(dsKey);
        try {
            return joinPoint.proceed();
        } finally {
            DynamicDataSourceContextHolder.removeContextKey();
        }
    }

    private DS getDSAnnotation(ProceedingJoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        DS dsAnnotation = targetClass.getAnnotation(DS.class);
        // 先判断类的注解，再判断方法注解
        if(Objects.nonNull(dsAnnotation)){
            return dsAnnotation;
        }else{
            MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
            return methodSignature.getMethod().getAnnotation(DS.class);
        }
    }
}
