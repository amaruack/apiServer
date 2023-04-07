package com.son.api.service.address;

import com.son.api.properties.DaouConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IpAddressAccessService {

    private final List<IpAddressMatcher> ipAddressMatchers;
    private final DaouConfigProperties daouConfigProperties;

    @Autowired
    public IpAddressAccessService(DaouConfigProperties daouConfigProperties) {
        this.daouConfigProperties = daouConfigProperties;
        this.ipAddressMatchers = daouConfigProperties.getAccessIpAddress().stream()
                .map(address -> new IpAddressMatcher(address))
                .collect(Collectors.toList());
    }

    public boolean isAccessible(String ipAddress) {
        return ipAddressMatchers.stream().anyMatch(matcher -> matcher.matches(ipAddress));
    }
}