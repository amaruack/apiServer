package com.son.api.slice.process;

import com.son.api.process.CsvFileReader;
import com.son.api.process.FileReader;
import com.son.api.process.TxtFileReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TxtFileReaderTest {

    @Test
    void success_text_file_read() throws FileNotFoundException {
        //given
        File file = ResourceUtils.getFile("classpath:sample/txtFile.txt");

        //when
        FileReader fileReader = new TxtFileReader();
        List<List<String>> readDatas = fileReader.read(file);

        //then
        assertEquals(24, readDatas.size());
        assertEquals(6, readDatas.get(2).size());
        assertEquals(readDatas.get(2), List.of( new String[]{"2022-07-22 02","24","4","45,100","27,300","95,000"}));

    }

    @Test
    void fail_text_file_read_with_different_separator() throws FileNotFoundException {
        //given
        File file = ResourceUtils.getFile("classpath:sample/txtFile_dif_separator.txt");

        //when
        FileReader fileReader = new TxtFileReader();
        List<List<String>> readDatas = fileReader.read(file);

        //then
        assertEquals(24, readDatas.size());
        assertNotEquals(6, readDatas.get(2).size());

    }

    @Test
    void fail_text_file_read_validation_check_row_length() throws FileNotFoundException {
        //given
        File file = ResourceUtils.getFile("classpath:sample/txtFile_validation_01.txt");
        FileReader fileReader = new CsvFileReader();
        List<List<String>> readDatas = fileReader.read(file);

        //when
        boolean result = fileReader.validate(readDatas);

        //then
        assertFalse(result);
    }

    @Test
    void fail_text_file_read_validation_check_column_length() throws FileNotFoundException {
        //given
        File file = ResourceUtils.getFile("classpath:sample/txtFile_validation_02.txt");
        FileReader fileReader = new CsvFileReader();
        List<List<String>> readDatas = fileReader.read(file);

        //when
        boolean result = fileReader.validate(readDatas);

        //then
        assertFalse(result);
    }

    @Test
    void fail_text_file_read_validation_check_duplicate_id() throws FileNotFoundException {
        //given
        File file = ResourceUtils.getFile("classpath:sample/txtFile_validation_03.txt");
        FileReader fileReader = new CsvFileReader();
        List<List<String>> readDatas = fileReader.read(file);

        //when
        boolean result = fileReader.validate(readDatas);

        //then
        assertFalse(result);
    }

    @Test
    void fail_text_file_read_validation_check_single_day() throws FileNotFoundException {
        //given
        File file = ResourceUtils.getFile("classpath:sample/txtFile_validation_04.txt");
        FileReader fileReader = new CsvFileReader();
        List<List<String>> readDatas = fileReader.read(file);

        //when
        boolean result = fileReader.validate(readDatas);

        //then
        assertFalse(result);
    }
    
}
