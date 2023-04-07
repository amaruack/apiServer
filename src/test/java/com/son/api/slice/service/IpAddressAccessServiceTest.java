package com.son.api.slice.service;

import com.son.api.properties.DaouConfigProperties;
import com.son.api.service.address.IpAddressAccessService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@Import({IpAddressAccessService.class})
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IpAddressAccessServiceTest {

    DaouConfigProperties daouConfigProperties;

    @InjectMocks
    IpAddressAccessService ipAddressAccessService;

    @BeforeAll
    public void beforeAll() {
        daouConfigProperties = mock(DaouConfigProperties.class);
        doReturn(List.of("192.168.51.0/24", "127.0.0.1")).when(daouConfigProperties).getAccessIpAddress();
    }

    @Test
    void success_assess_ip() {
        // given
        String ipAddress = "192.168.51.11";
        // when
        boolean isAccessible = ipAddressAccessService.isAccessible(ipAddress);
        // then
        assertTrue(isAccessible);
    }

    @Test
    void success_assess_deny_ip() {
        // given
        String ipAddress = "192.168.52.11";
        // when
        boolean isAccessible = ipAddressAccessService.isAccessible(ipAddress);
        // then
        assertFalse(isAccessible);
    }

}
