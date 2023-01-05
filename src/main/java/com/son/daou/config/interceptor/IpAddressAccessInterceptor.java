package com.son.daou.config.interceptor;

import com.son.daou.service.address.IpAddressAccessService;
import com.son.daou.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class IpAddressAccessInterceptor implements HandlerInterceptor {

    private final IpAddressAccessService ipAddressAccessService;

    public IpAddressAccessInterceptor(IpAddressAccessService ipAddressAccessService){
        this.ipAddressAccessService = ipAddressAccessService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String clientIpAddress = HttpUtils.getClientIp(request);

        if (!ipAddressAccessService.isAccessible(clientIpAddress)) {
            String requestURI = request.getRequestURI();
            log.warn("Forbidden access. request uri={}, client ip={}", requestURI, clientIpAddress);

            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}