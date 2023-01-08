package com.son.daou.process;

import com.son.daou.util.DateTimeUtils;
import com.son.daou.util.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractFileReader implements FileReader{

    protected String separator = ",";
    protected char quoteChar = '"';

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void setQuoteChar(char quoteChar) {
        this.quoteChar = quoteChar;
    }

    /**
     * 파일을 읽어서 DB에 저장할 때는 파일 전체를 모두 입력해야 한다. 파일 중간에 오류가 있을 경우 미입력 처리
     *  오류가 너무 모호함
     *
     * @return
     */
    public boolean validate(List<List<String>> readDatas) {

        if (readDatas.size() != 24) {
            log.error("data row size is not match [{}/24]");
            return false;
        }

        for (int i = 0; i < readDatas.size() ; i++) {
            List<String> strings = readDatas.get(i);
            if (strings.size() != 6) {
                log.error("data column size is not match [{}/6] in row {}", strings.size(), i+1);
                return false;
            }
        }

        // 같은 시간 데이터 가 있다면 false
        List<String> ids = readDatas.stream()
                .map( data -> data.get(FileUtils.INDEX_DATE_TIME)).collect(Collectors.toList());
        List<String> dupleDateTimes = ids.stream()
                .filter(data -> ids.indexOf(data) != ids.lastIndexOf(data))
                .distinct()
                .collect(Collectors.toList());
        if ( dupleDateTimes.size() > 0 ) {
            log.error("data datetime duplicate [{}]", String.join(",", dupleDateTimes));
            return false;
        }

        // 같은 날이 아니면 false
        List<String> dates = ids.stream()
                .map(id -> LocalDateTime.parse(id, DateTimeUtils.READ_FILE_DATE_TIME_FORMATTER).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .distinct().collect(Collectors.toList());
        if (dates.size() > 1) {
            log.error("data datetime is not on single day [{}]", String.join(",", dates));
            return false;
        }

        return true;
    }
}
