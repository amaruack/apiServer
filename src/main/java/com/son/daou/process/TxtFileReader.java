package com.son.daou.process;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TxtFileReader extends AbstractFileReader {

    private final char DEFAULT_SEPARATOR = '|';
    private final char DEFAULT_QUOTE = '"';

    public TxtFileReader() {
        super.separator = DEFAULT_SEPARATOR;
        super.quoteChar = DEFAULT_QUOTE;
    }

    @Override
    public List<String[]> read(File file) {
        List<String[]> result;
        CSVParser parser = new CSVParserBuilder().withSeparator(separator).withQuoteChar(quoteChar).build();
        try ( CSVReader reader = new CSVReaderBuilder(new java.io.FileReader(file)).withCSVParser(parser).build() ) {
            result = reader.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
