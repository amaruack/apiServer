package com.son.api.process;

import java.io.File;
import java.util.List;

public interface FileReader {

    List<List<String>> read(File file);
    void setSeparator(String separator);
    void setQuoteChar(char quoteChar);
    boolean validate(List<List<String>> readDatas);

}
