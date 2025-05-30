package com.example.demo.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResponseHeaderFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        httpResponse.setHeader("Content-Security-Policy",
                "default-src 'none'; " +
                        "script-src 'none'; " +
                        "style-src 'none'; " +
                        "img-src 'none'; " +
                        "connect-src 'self'; " +
                        "object-src 'none' " +
                        "frame-ancestors 'none'");
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
