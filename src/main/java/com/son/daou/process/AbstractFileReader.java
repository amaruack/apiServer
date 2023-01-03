package com.son.daou.process;

public abstract class AbstractFileReader implements FileReader{

    protected char separator = ',';
    protected char quoteChar = '"';

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    public void setQuoteChar(char quoteChar) {
        this.quoteChar = quoteChar;
    }
}
