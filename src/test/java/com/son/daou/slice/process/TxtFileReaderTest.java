package com.son.daou.slice.process;

import com.son.daou.process.FileReader;
import com.son.daou.process.TxtFileReader;
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
        List<String[]> readDatas = fileReader.read(file);

        //then
        assertEquals(24, readDatas.size());
        assertEquals(6, readDatas.get(2).length);
        assertArrayEquals(readDatas.get(2), new String[]{"2022-07-22 02","24","4","45,100","27,300","95,000"});

    }

    @Test
    void fail_text_file_read_with_different_separator() throws FileNotFoundException {
        //given
        File file = ResourceUtils.getFile("classpath:sample/txtFile_dif_separator.txt");

        //when
        FileReader fileReader = new TxtFileReader();
        List<String[]> readDatas = fileReader.read(file);

        //then
        assertEquals(24, readDatas.size());
        assertNotEquals(6, readDatas.get(2).length);

    }

}
