package com.son.daou.slice.utils;

import com.son.daou.util.HttpUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class HttpUtilsTest {

    @Mock
    HttpServletRequest request;

    @Test
    void success_get_remote_address(){
        // given
        String ip = "127.0.0.1";
        doReturn(ip).when(request).getRemoteAddr();

        // when
        String getClientIp = HttpUtils.getClientIp(request);

        //then
        assertEquals(ip, getClientIp);
    }

    @Test
    void success_get_remote_address_from_forwarded_for(){
        // given
        String ip = "127.0.0.1";
        doReturn(ip).when(request).getHeader("X-Forwarded-For");

        // when
        String getClientIp = HttpUtils.getClientIp(request);

        //then
        assertEquals(ip, getClientIp);
    }

    @Test
    void success_get_remote_address_from_real_ip(){
        // given
        String ip = "127.0.0.1";
        doReturn(null).when(request).getHeader("X-Forwarded-For");
        doReturn(ip).when(request).getHeader("x-real-ip");

        // when
        String getClientIp = HttpUtils.getClientIp(request);

        //then
        assertEquals(ip, getClientIp);
    }

    @Test
    void success_get_remote_address_from_original_forwarded_for(){
        // given
        String ip = "127.0.0.1";
        doReturn(null).when(request).getHeader("X-Forwarded-For");
        doReturn(null).when(request).getHeader("x-real-ip");
        doReturn(ip).when(request).getHeader("x-original-forwarded-for");

        // when
        String getClientIp = HttpUtils.getClientIp(request);

        //then
        assertEquals(ip, getClientIp);
    }

    @Test
    void success_get_remote_address_from_proxy_client_ip(){
        // given
        String ip = "127.0.0.1";
        doReturn(null).when(request).getHeader("X-Forwarded-For");
        doReturn(null).when(request).getHeader("x-real-ip");
        doReturn(null).when(request).getHeader("x-original-forwarded-for");
        doReturn(ip).when(request).getHeader("Proxy-Client-IP");

        // when
        String getClientIp = HttpUtils.getClientIp(request);

        //then
        assertEquals(ip, getClientIp);
    }

    @Test
    void success_get_remote_address_from_wl_proxy_client_ip(){
        // given
        String ip = "127.0.0.1";
        doReturn(null).when(request).getHeader("X-Forwarded-For");
        doReturn(null).when(request).getHeader("x-real-ip");
        doReturn(null).when(request).getHeader("x-original-forwarded-for");
        doReturn(null).when(request).getHeader("Proxy-Client-IP");
        doReturn(ip).when(request).getHeader("WL-Proxy-Client-IP");

        // when
        String getClientIp = HttpUtils.getClientIp(request);

        //then
        assertEquals(ip, getClientIp);
    }

    @Test
    void success_get_remote_address_from_HTTP_X_FORWARDED_FOR(){
        // given
        String ip = "127.0.0.1";
        doReturn(null).when(request).getHeader("X-Forwarded-For");
        doReturn(null).when(request).getHeader("x-real-ip");
        doReturn(null).when(request).getHeader("x-original-forwarded-for");
        doReturn(null).when(request).getHeader("Proxy-Client-IP");
        doReturn(null).when(request).getHeader("WL-Proxy-Client-IP");
        doReturn(ip).when(request).getHeader("HTTP_X_FORWARDED_FOR");

        // when
        String getClientIp = HttpUtils.getClientIp(request);

        //then
        assertEquals(ip, getClientIp);
    }

    @Test
    void success_get_remote_address_from_HTTP_X_FORWARDED(){
        // given
        String ip = "127.0.0.1";
        doReturn(null).when(request).getHeader("X-Forwarded-For");
        doReturn(null).when(request).getHeader("x-real-ip");
        doReturn(null).when(request).getHeader("x-original-forwarded-for");
        doReturn(null).when(request).getHeader("Proxy-Client-IP");
        doReturn(null).when(request).getHeader("WL-Proxy-Client-IP");
        doReturn(null).when(request).getHeader("HTTP_X_FORWARDED_FOR");
        doReturn(ip).when(request).getHeader("HTTP_X_FORWARDED");

        // when
        String getClientIp = HttpUtils.getClientIp(request);

        //then
        assertEquals(ip, getClientIp);
    }

    @Test
    void success_get_remote_address_from_HTTP_X_CLUSTER_CLIENT_IP(){
        // given
        String ip = "127.0.0.1";
        doReturn(null).when(request).getHeader("X-Forwarded-For");
        doReturn(null).when(request).getHeader("x-real-ip");
        doReturn(null).when(request).getHeader("x-original-forwarded-for");
        doReturn(null).when(request).getHeader("Proxy-Client-IP");
        doReturn(null).when(request).getHeader("WL-Proxy-Client-IP");
        doReturn(null).when(request).getHeader("HTTP_X_FORWARDED_FOR");
        doReturn(null).when(request).getHeader("HTTP_X_FORWARDED");
        doReturn(ip).when(request).getHeader("HTTP_X_CLUSTER_CLIENT_IP");

        // when
        String getClientIp = HttpUtils.getClientIp(request);

        //then
        assertEquals(ip, getClientIp);
    }

    @Test
    void success_get_remote_address_from_HTTP_CLIENT_IP(){
        // given
        String ip = "127.0.0.1";
        doReturn(null).when(request).getHeader("X-Forwarded-For");
        doReturn(null).when(request).getHeader("x-real-ip");
        doReturn(null).when(request).getHeader("x-original-forwarded-for");
        doReturn(null).when(request).getHeader("Proxy-Client-IP");
        doReturn(null).when(request).getHeader("WL-Proxy-Client-IP");
        doReturn(null).when(request).getHeader("HTTP_X_FORWARDED_FOR");
        doReturn(null).when(request).getHeader("HTTP_X_FORWARDED");
        doReturn(null).when(request).getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        doReturn(ip).when(request).getHeader("HTTP_CLIENT_IP");

        // when
        String getClientIp = HttpUtils.getClientIp(request);

        //then
        assertEquals(ip, getClientIp);
    }

    @Test
    void success_get_remote_address_from_HTTP_FORWARDED_FOR(){
        // given
        String ip = "127.0.0.1";
        doReturn(null).when(request).getHeader("X-Forwarded-For");
        doReturn(null).when(request).getHeader("x-real-ip");
        doReturn(null).when(request).getHeader("x-original-forwarded-for");
        doReturn(null).when(request).getHeader("Proxy-Client-IP");
        doReturn(null).when(request).getHeader("WL-Proxy-Client-IP");
        doReturn(null).when(request).getHeader("HTTP_X_FORWARDED_FOR");
        doReturn(null).when(request).getHeader("HTTP_X_FORWARDED");
        doReturn(null).when(request).getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        doReturn(null).when(request).getHeader("HTTP_CLIENT_IP");
        doReturn(ip).when(request).getHeader("HTTP_FORWARDED_FOR");

        // when
        String getClientIp = HttpUtils.getClientIp(request);

        //then
        assertEquals(ip, getClientIp);
    }

    @Test
    void success_get_remote_address_from_HTTP_FORWARDED(){
        // given
        String ip = "127.0.0.1";
        doReturn(null).when(request).getHeader("X-Forwarded-For");
        doReturn(null).when(request).getHeader("x-real-ip");
        doReturn(null).when(request).getHeader("x-original-forwarded-for");
        doReturn(null).when(request).getHeader("Proxy-Client-IP");
        doReturn(null).when(request).getHeader("WL-Proxy-Client-IP");
        doReturn(null).when(request).getHeader("HTTP_X_FORWARDED_FOR");
        doReturn(null).when(request).getHeader("HTTP_X_FORWARDED");
        doReturn(null).when(request).getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        doReturn(null).when(request).getHeader("HTTP_CLIENT_IP");
        doReturn(null).when(request).getHeader("HTTP_FORWARDED_FOR");
        doReturn(ip).when(request).getHeader("HTTP_FORWARDED");

        // when
        String getClientIp = HttpUtils.getClientIp(request);

        //then
        assertEquals(ip, getClientIp);
    }

    @Test
    void success_get_remote_address_from_HTTP_VIA(){
        // given
        String ip = "127.0.0.1";
        doReturn(null).when(request).getHeader("X-Forwarded-For");
        doReturn(null).when(request).getHeader("x-real-ip");
        doReturn(null).when(request).getHeader("x-original-forwarded-for");
        doReturn(null).when(request).getHeader("Proxy-Client-IP");
        doReturn(null).when(request).getHeader("WL-Proxy-Client-IP");
        doReturn(null).when(request).getHeader("HTTP_X_FORWARDED_FOR");
        doReturn(null).when(request).getHeader("HTTP_X_FORWARDED");
        doReturn(null).when(request).getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        doReturn(null).when(request).getHeader("HTTP_CLIENT_IP");
        doReturn(null).when(request).getHeader("HTTP_FORWARDED_FOR");
        doReturn(null).when(request).getHeader("HTTP_FORWARDED");
        doReturn(ip).when(request).getHeader("HTTP_VIA");

        // when
        String getClientIp = HttpUtils.getClientIp(request);

        //then
        assertEquals(ip, getClientIp);
    }

    @Test
    void success_get_remote_address_from_REMOTE_ADDR(){
        // given
        String ip = "127.0.0.1";
        doReturn(null).when(request).getHeader("X-Forwarded-For");
        doReturn(null).when(request).getHeader("x-real-ip");
        doReturn(null).when(request).getHeader("x-original-forwarded-for");
        doReturn(null).when(request).getHeader("Proxy-Client-IP");
        doReturn(null).when(request).getHeader("WL-Proxy-Client-IP");
        doReturn(null).when(request).getHeader("HTTP_X_FORWARDED_FOR");
        doReturn(null).when(request).getHeader("HTTP_X_FORWARDED");
        doReturn(null).when(request).getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        doReturn(null).when(request).getHeader("HTTP_CLIENT_IP");
        doReturn(null).when(request).getHeader("HTTP_FORWARDED_FOR");
        doReturn(null).when(request).getHeader("HTTP_FORWARDED");
        doReturn(null).when(request).getHeader("HTTP_VIA");
        doReturn(ip).when(request).getHeader("REMOTE_ADDR");

        // when
        String getClientIp = HttpUtils.getClientIp(request);

        //then
        assertEquals(ip, getClientIp);
    }

}
