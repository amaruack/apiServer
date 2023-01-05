package com.son.daou.config;


import com.son.daou.dto.shop.ShopHistoryCreateRequest;
import com.son.daou.process.FileExtension;
import com.son.daou.process.FileReader;
import com.son.daou.process.FileReaderFactory;
import com.son.daou.properties.DaouConfigProperties;
import com.son.daou.service.shop.ShopHistoryService;
import com.son.daou.util.DateTimeUtils;
import com.son.daou.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    DaouConfigProperties daouConfigProperties;

    @Autowired
    ShopHistoryService shopHistoryService;

    private final int INDEX_DATE_TIME = 0;
    private final int INDEX_REGISTER_COUNT = 1;
    private final int INDEX_DELETE_COUNT = 2;
    private final int INDEX_PAYMENT = 3;
    private final int INDEX_USED = 4;
    private final int INDEX_SALES = 5;

    private final NumberFormat numberFormat = NumberFormat.getInstance();

//    @Scheduled(cron = "0/5 * * * * *")
    @Scheduled(cron = "0 0 * * * *")
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
            List<List<String>> readDatas = fileReader.read(file);

            if (fileReader.validate(readDatas)){
                List<ShopHistoryCreateRequest> createRequests = readDatas.stream().map(readData -> {
                    try {
                        return ShopHistoryCreateRequest.builder()
                                .dateTime(LocalDateTime.parse(readData.get(INDEX_DATE_TIME), DateTimeUtils.READ_FILE_DATE_TIME_FORMATTER))
                                .registerCount(numberFormat.parse(readData.get(INDEX_REGISTER_COUNT)).intValue())
                                .deleteCount(numberFormat.parse(readData.get(INDEX_DELETE_COUNT)).intValue())
                                .payment(numberFormat.parse(readData.get(INDEX_PAYMENT)).longValue())
                                .used(numberFormat.parse(readData.get(INDEX_USED)).longValue())
                                .sales(numberFormat.parse(readData.get(INDEX_SALES)).longValue())
                                .build();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
                shopHistoryService.createAll(createRequests);
            }
            // TODO 읽은 파일 이동 // 성공 실패에 따라 이동
        }



    }

}
