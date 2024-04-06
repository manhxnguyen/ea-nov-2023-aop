package miu.ea.aop.component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import miu.ea.aop.model.ActivityLog;
import miu.ea.aop.repository.ActivityLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@AllArgsConstructor
public class ExecutionTimeAspect {
    private final ActivityLogRepository activityLogRepository;

    private final HttpServletRequest request;

    @Around("@annotation(miu.ea.aop.component.ExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        ActivityLog log = new ActivityLog();
        log.setDate(LocalDateTime.now());
        log.setOperation(joinPoint.getSignature().getName());
        log.setDuration(executionTime);
        activityLogRepository.save(log);

        return proceed;
    }

    @Before("execution(* miu.ea.aop.service.*.*(..))")
    public void checkAopIsAwesomeHeader(JoinPoint joinPoint) throws AopIsAwesomeHeaderException {
        if (request.getMethod() == "POST") {
            if (!"AOP-IS-AWESOME".equals(request.getHeader("AOP-IS-AWESOME"))) {
                throw new AopIsAwesomeHeaderException("AOP-IS-AWESOME header is missing");
            }
        }
    }
}
