package com.son.api.service.file;

import com.son.api.dto.shop.ShopHistoryCreateRequest;
import com.son.api.dto.shop.ShopHistoryResponse;
import com.son.api.process.FileExtension;
import com.son.api.process.FileReader;
import com.son.api.process.FileReaderFactory;
import com.son.api.properties.DaouConfigProperties;
import com.son.api.service.shop.ShopHistoryService;
import com.son.api.util.DateTimeUtils;
import com.son.api.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileProcessService {

    @Autowired
    DaouConfigProperties daouConfigProperties;

    @Autowired
    ShopHistoryService shopHistoryService;

    private final NumberFormat numberFormat = NumberFormat.getInstance();

    public void fileProcess() {

        File[] files = getFiles(daouConfigProperties.getRootPath());
        if (files == null || files.length == 0) {
            log.warn("NOT EXIST FILE IN ROOT PATH [{}]", daouConfigProperties.getRootPath());
        }
        for (File file : files) {
            String extension = FileUtils.getFileExtension(file);
            FileReader fileReader = FileReaderFactory.getFileReader(extension);
            List<List<String>> readDatas = fileReader.read(file);

            if (fileReader.validate(readDatas)){
                List<ShopHistoryCreateRequest> createRequests = readDatas.stream().map(readData -> {
                    try {
                        return ShopHistoryCreateRequest.builder()
                                .dateTime(LocalDateTime.parse(readData.get(FileUtils.INDEX_DATE_TIME), DateTimeUtils.READ_FILE_DATE_TIME_FORMATTER).format(DateTimeUtils.DATE_TIME_ID_FORMATTER))
                                .registerCount(numberFormat.parse(readData.get(FileUtils.INDEX_REGISTER_COUNT)).intValue())
                                .deleteCount(numberFormat.parse(readData.get(FileUtils.INDEX_DELETE_COUNT)).intValue())
                                .payment(numberFormat.parse(readData.get(FileUtils.INDEX_PAYMENT)).longValue())
                                .used(numberFormat.parse(readData.get(FileUtils.INDEX_USED)).longValue())
                                .sales(numberFormat.parse(readData.get(FileUtils.INDEX_SALES)).longValue())
                                .build();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
                List<ShopHistoryResponse> responses = shopHistoryService.createAll(createRequests);
                log.info("INSERT COMPLETED, ids=[{}]", responses.stream()
                        .map(res -> res.getDateTime())
                        .collect(Collectors.joining(",")));
            } else {
                log.error("FILE DATA FORMAT ERROR [{}]", file.getName());
            }
        }
    }

    /**
     * root path 안에 파일 리스트 불러오기
     * FileExtension enum 에 설정된 파일 리스트 조회
     *
     * @param rootPath
     * @return
     */
    public File[] getFiles(String rootPath) {
        File path = new File(rootPath);
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
        return files;
    }

}
