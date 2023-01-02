package com.userservice.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiter rateLimiter;

    public RateLimitInterceptor(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (!isSupported(handlerMethod)) {
            return true;
        }

        return rateLimiter.canAcquire();
    }

    public boolean isSupported(HandlerMethod handlerMethod) {
        return handlerMethod.hasMethodAnnotation(RateLimit.class);
    }

}
