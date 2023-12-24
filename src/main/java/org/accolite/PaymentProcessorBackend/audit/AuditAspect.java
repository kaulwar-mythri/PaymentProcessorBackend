package org.accolite.PaymentProcessorBackend.audit;

import jakarta.persistence.Column;
import org.accolite.PaymentProcessorBackend.entity.Audit;
import org.accolite.PaymentProcessorBackend.repository.AuditRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Aspect
//@Component
//public class AuditAspect {
//    @Autowired
//    private AuditRepository auditRepository;
//
//    @Pointcut("execution(* org.accolite.PaymentProcessorBackend.serviceImpl.UserServiceImpl*(..)) || " +
//            "execution(* org.accolite.PaymentProcessorBackend.serviceImpl.VendorServiceImpl.*(..)) || " +
//            "execution(* org.accolite.PaymentProcessorBackend.serviceImpl.AdminServiceImpl.*(..)) || ")
//    public void serviceMethods() {
//
//    }
//    @AfterReturning(pointcut = "auditPointcut()", returning = "result")
//    public void afterReturning(JoinPoint joinPoint, Object result) {
//        saveAuditEntry(joinPoint, result);
//    }
//
//    @AfterThrowing(pointcut = "auditPointcut()", throwing = "exception")
//    public void afterThrowing(JoinPoint joinPoint, Throwable exception) {
//        saveAuditEntry(joinPoint, exception);
//    }
//
//    @Async
//    private void saveAuditEntry(JoinPoint joinPoint, Object result) {
//        try {
//            String serviceName = joinPoint.getTarget().getClass().getSimpleName();
//            String methodName = joinPoint.getSignature().getName();
//            LocalDateTime timestamp = LocalDateTime.now();
//
//            Audit audit = new Audit();
//            audit.setServiceName(serviceName);
//            audit.setMethodName(methodName);
//            audit.setTimestamp(timestamp);
//
//            if(result != null) {
//                audit.setResult(result.toString());
//            }
//            auditRepository.save(audit);
//        } catch (DataAccessException e) {
//            e.printStackTrace();
//        }
//    }
//}
