package com.son.daou.config.interceptor;

import com.son.daou.properties.DaouConfigProperties;
import com.son.daou.util.HttpUtils;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Autowired
    DaouConfigProperties daouConfigProperties;

    public Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, this::newBucket);
    }

    private Bucket newBucket(String ip) {
        DaouConfigProperties.RateLimit rateLimit =  daouConfigProperties.getRateLimit();
        return Bucket.builder()
                .addLimit(Bandwidth.classic(rateLimit.getCapacity(), Refill.intervally(rateLimit.getCapacity(), getDuration(rateLimit.getTime(), rateLimit.getUnit()))))
                .build();
    }

    private Duration getDuration(int time, String unit){
        return Duration.of(time, ChronoUnit.valueOf(unit.toUpperCase()));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = HttpUtils.getClientIp(request);
        Bucket tokenBucket = this.resolveBucket(clientIp);
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "");
            return false;
        }
    }
}