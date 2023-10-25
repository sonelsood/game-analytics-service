
package com.fsb.game.analytics.service.server.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fsb.game.analytics.service.core.model.InvocationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * Interceptor which takes header values, if present, and puts it on the thread local, else throws {@link Exception}.
 */
@Slf4j
public class HeadersInterceptor implements HandlerInterceptor {

    private static final String X_USER = "x-user";

    private void extractUser(HttpServletRequest request) throws HttpMediaTypeNotSupportedException {
        InvocationContext.setUserId(request.getHeader(X_USER));
    }

    /** {@inheritDoc} */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug("Pre Interceptor invokes for request {}", request.getServletPath());
        // Cleanup data from the previous thread execution.
        InvocationContext.clear();
        InvocationContext.setHttpMethodType(request.getMethod());
        InvocationContext.setPathInfo(request.getPathInfo());
        extractUser(request);

        return true;
    }

    /** {@inheritDoc} */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // No operation

    }

    /** {@inheritDoc} */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // No operation

    }
}