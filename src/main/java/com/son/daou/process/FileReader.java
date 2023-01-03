package com.son.daou.process;

import java.io.File;
import java.util.List;

public interface FileReader {

    List<String[]> read(File file);
    void setSeparator(char separator);
    void setQuoteChar(char quoteChar);

}
