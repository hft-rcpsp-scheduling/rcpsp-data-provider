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
        LOGGER.info("Running explicit Garbage Collector after " + req.getMethod() + " : " + req.getRequestURI());
        System.gc();
    }
}
