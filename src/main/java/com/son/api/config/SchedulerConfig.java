package com.son.api.config;


import com.son.api.service.file.FileProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    FileProcessService fileProcessService;

    @Scheduled(cron = "0 0 0 * * *")
    public void fileReadScheduled() {

        fileProcessService.fileProcess();

    }

}
