package com.son.daou.process;

public class FileReaderFactory {

    /**
     * File Reader 생성 factory 메소드
     * @param fileExtension - file 확장자 string type
     * @return
     */
    public static FileReader getFileReader(String fileExtension){
        return getFileReader(FileExtension.getFileExtension(fileExtension).orElseThrow(() -> {
            String message = "NOT ACCEPTED FILE EXTENSION";
            throw new RuntimeException(message);
        }));
    }

    /**
     * File Reader 생성 factory 메소드
     * @param fileExtension - file 확장자 enum type
     * @return
     */
    public static FileReader getFileReader(FileExtension fileExtension){
        FileReader fileReader;
        switch (fileExtension) {
            case CSV:
                fileReader = new CsvFileReader();
                break;
            case TXT:
                fileReader = new TxtFileReader();
                break;
            default:
                String message = "NOT ACCEPTED FILE EXTENSION";
                throw new RuntimeException(message);
        }
        return fileReader;
    }

}
