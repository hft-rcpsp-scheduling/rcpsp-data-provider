package com.hft.provider.config;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

@Component
@Order(2)
public class ResponseGCFilter implements Filter {
    private final Logger LOGGER = Logger.getLogger(ResponseGCFilter.class.getName());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        filterChain.doFilter(servletRequest, servletResponse);
        LOGGER.fine("Running explicit Garbage Collector after " + req.getMethod() + " : " + req.getRequestURI());
        System.gc(); // TODO potential for further improvements (Load Testing + Memory Investigation in the Debugger and Profiler)
    }
}
