package com.example.pku_chem_backend.aspect;

import com.example.pku_chem_backend.util.JwtUtil;
import com.example.pku_chem_backend.util.LogUtil;
import net.sf.jsqlparser.statement.select.Join;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
public class LoggingAspect {
    private final LogUtil logUtil;

    public LoggingAspect(LogUtil logUtil) {
        this.logUtil = logUtil;
    }

    @Pointcut("within(com.example.pku_chem_backend.controller.*)")
    public void controllerMethods(){}

    @After("controllerMethods()")
    public void logAfterControllerMethods(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = null;
        for(Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                request = (HttpServletRequest) arg;
                break;
            }
        }
        if(request != null) {
            String username = JwtUtil.getUsername(request.getHeader("Authorization"));
            logUtil.writeLog(username, methodName, "null", LocalDateTime.now().toString(), request.getRemoteAddr(), request);
        }
    }

}
