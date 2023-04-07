package com.son.api.config.filter;

import com.son.api.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class RequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        long end = System.currentTimeMillis();

        log.info( "{} - {} | START={} | END={} | INTERVAL={}" ,
                ((HttpServletRequest) request).getMethod(),
                ((HttpServletRequest) request).getRequestURI(),
                LocalDateTime.ofEpochSecond(start / 1000L, (int)(start % 1000L), DateTimeUtils.ZONE_OFFSET),
                LocalDateTime.ofEpochSecond(end / 1000L, (int)(end % 1000L), DateTimeUtils.ZONE_OFFSET),
                (end - start));
    }

}