package com.son.daou.process;

import java.util.List;

public abstract class AbstractFileReader implements FileReader{

    protected char separator = ',';
    protected char quoteChar = '"';

    public void setSeparator(char separator) {
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
    public boolean validate(List<String[]> readDatas) {

        if (readDatas.size() != 24) {
            return false;
        }

        for (String[] strings : readDatas ) {
            if (strings.length != 6) {
                return false;
            }
        }

        return true;
    }
}
