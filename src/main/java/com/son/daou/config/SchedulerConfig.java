package com.son.daou.config;


import com.son.daou.process.FileExtension;
import com.son.daou.process.FileReader;
import com.son.daou.process.FileReaderFactory;
import com.son.daou.properties.DaouConfigProperties;
import com.son.daou.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.List;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

    @Autowired
    DaouConfigProperties daouConfigProperties;

    @Scheduled(cron = "0/5 * * * * *")
    public void cronJobSch() {

        File path = new File(daouConfigProperties.getRootPath());
        File[] files = path.listFiles((file) -> {
            if (file.isFile()) {
                if (file.getName().lastIndexOf('.') > 0) {
                    String extension = FileUtils.getFileExtension(file);
                    if (FileExtension.contains(extension)){
                        return true;
                    }
                }
            }
            return false;
        });

        for (File file : files) {
            String extension = FileUtils.getFileExtension(file);
            FileReader fileReader = FileReaderFactory.getFileReader(extension);
            List<String[]> readDatas = fileReader.read(file);

//            if (fileReader.validate(readDatas)){
//                readDatas.stream().map(strings -> {
//
//                });
//            }
            // TODO 읽은 파일 이동 // 성공 실패에 따라 이동
        }



    }

}
