package com.son.daou.config;


import com.son.daou.dto.shop.ShopHistoryResponse;
import com.son.daou.service.file.FileProcessService;
import com.son.daou.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    FileProcessService fileProcessService;

    @Scheduled(cron = "0 0 0 * * *")
    public void fileReadScheduled() {

        List<ShopHistoryResponse> response = fileProcessService.fileProcess();
        if (response.size() > 0) {
            log.info("INSERT COMPLETED, ids=[{}]", response.stream()
                    .map(res -> res.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))
                    .collect(Collectors.joining(",")));
        } else {
            log.error("INSERT FAIL ");
        }

    }

}
