package com.son.daou.process;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TxtFileReader extends AbstractFileReader {

    private final String DEFAULT_SEPARATOR = "|";
    private final char DEFAULT_QUOTE = '"';

    public TxtFileReader() {
        super.separator = DEFAULT_SEPARATOR;
        super.quoteChar = DEFAULT_QUOTE;
    }

    @Override
    public List<List<String>> read(File file) {
        List<List<String>> result = new ArrayList<>();
        try( Reader reader = new InputStreamReader(new BOMInputStream( new FileInputStream(file)), "UTF-8")) {
            CSVFormat csvFormat = CSVFormat.Builder.create(CSVFormat.EXCEL).setDelimiter(DEFAULT_SEPARATOR).build();
            Iterable<CSVRecord> records = csvFormat.parse(reader);
            for (CSVRecord record : records) {
                if (record.toList().equals(List.of(""))){
                    log.warn("NULL LINE SKIP");
                } else {
                    result.add(record.toList());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
