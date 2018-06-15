package com.uraltranscom.buildreports.service.additional;

/**
 *
 * Класс для конвертации из MultipartFile в File
 *
 * @author Vladislav Klochkov
 * @version 1.0
 * @create 13.06.2018
 *
 * 13.06.2018
 *   1. Версия 1.0
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MultipartFileToFile {
    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(MultipartFileToFile.class);

    public static File convertToFile;

    public static File multipartToFile(MultipartFile multipart) {
        try {
            convertToFile = new File(System.getProperty("java.io.tmpdir"), multipart.getOriginalFilename());
            convertToFile.createNewFile();
            try(FileOutputStream fileOutputStream = new FileOutputStream(convertToFile)) {
                fileOutputStream.write(multipart.getBytes());
            }
        } catch (IOException e) {
            logger.error("Ошибка конвертации файла - {}", e.getMessage());
        }
        return convertToFile;
    }
}
