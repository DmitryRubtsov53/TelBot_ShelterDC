package com.devsteam.getname.telbot_shelterdc.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ShelterServiceTest {
    @Value("${name.of.test.data.file}")
    private String testFileName;
    @Autowired
    ShelterService service;


    @Test
    void getCorrectDataFile() {
        File actualFile = service.getDataFile(testFileName);
        File expectedFile = new File("ShelterTMP.json");
        Assertions.assertEquals(expectedFile, actualFile);
    }

    @Test
    void getInvalidDataFile() {

        assertThrows(RuntimeException.class, ()->service.getDataFile("test.json"));
    }

}