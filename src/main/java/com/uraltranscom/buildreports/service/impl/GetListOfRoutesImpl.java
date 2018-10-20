package com.uraltranscom.buildreports.service.impl;

import com.uraltranscom.buildreports.model.ReplaceNameStationClazz;
import com.uraltranscom.buildreports.model.Route;
import com.uraltranscom.buildreports.service.GetList;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Класс получения списка заявок
 *
 * @author Vladislav Klochkov
 * @version 1.0
 * @create 13.06.2018
 *
 * 13.06.2018
 *   1. Версия 1.0
 *
 */

public class GetListOfRoutesImpl implements GetList {
    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(GetListOfRoutesImpl.class);

    // Основаная мапа, куда записываем все маршруты
    private Map<Integer, Route> mapOfRoutes = new ConcurrentHashMap<>();

    // Переменные для работы с файлами
    private File file;
    private FileInputStream fileInputStream;

    // Переменные для работы с Excel файлом(формат XLSX)
    private XSSFWorkbook xssfWorkbook;
    private XSSFSheet sheet;

    private GetListOfRoutesImpl() {
    }

    // Заполняем Map вагонами
    // TODO Переписать метод, избавиться от формата жесткого, необходимо и XLSX и XLS
    @Override
    public void fillMap() {
        mapOfRoutes.clear();
        // Получаем файл формата xls
        try {
            fileInputStream = new FileInputStream(this.file);
            xssfWorkbook = new XSSFWorkbook(fileInputStream);

            // Заполняем Map данными
            sheet = xssfWorkbook.getSheetAt(0);
            int i = 0;
            for (int j = 2; j < sheet.getLastRowNum() + 1; j++) {
                XSSFRow row = sheet.getRow(1);
                String nameOfStationDeparture = null;
                String roadOfStationDeparture = null;
                String customer = null;
                int volumeFrom = 0;
                int countOrder = 0;

                for (int c = 1; c < row.getLastCellNum(); c++) {
                    if (row.getCell(c).getStringCellValue().trim().equals("Ст. отправления")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        nameOfStationDeparture = ReplaceNameStationClazz.getNameRootStation(xssfRow.getCell(c).getStringCellValue());
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Дорога отправления")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        roadOfStationDeparture = xssfRow.getCell(c).getStringCellValue();
                        if (roadOfStationDeparture.equals("УЗБ")) {
                            roadOfStationDeparture = "УТИ";
                        }
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Контрагент")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        customer = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Объем от")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        volumeFrom = (int) xssfRow.getCell(c).getNumericCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Кол-во ПС")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        countOrder = (int) xssfRow.getCell(c).getNumericCellValue();
                    }
                }
                if (volumeFrom == 122 || volumeFrom == 114) volumeFrom = 120;
                if (volumeFrom == 140) volumeFrom = 138;
                if (volumeFrom == 158) volumeFrom = 150;
                if (mapOfRoutes.isEmpty()) {
                    mapOfRoutes.put(i, new Route(nameOfStationDeparture, roadOfStationDeparture, customer,volumeFrom, countOrder));
                    i++;
                } else {
                    boolean isOk = false;
                    Iterator<Map.Entry<Integer, Route>> iterator = mapOfRoutes.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Integer, Route> map = iterator.next();
                        if (map.getValue().getNameRoadOfStationDeparture().equals(roadOfStationDeparture) &&
                                map.getValue().getNameOfStationDeparture().equals(nameOfStationDeparture) &&
                                map.getValue().getCustomer().equals(customer)) {
                            map.getValue().setCountOrder(map.getValue().getCountOrder() + countOrder);
                            map.getValue().getListVolume().add(volumeFrom);
                            isOk = true;
                        }
                    }
                    if (!isOk) {
                        mapOfRoutes.put(i, new Route(nameOfStationDeparture, roadOfStationDeparture, customer, volumeFrom, countOrder));
                        i++;
                    }
                }
            }
            for (Map.Entry<Integer, Route> map: mapOfRoutes.entrySet()) {
                map.getValue().setStringBuilderVolume();
            }
            logger.debug("Body route: {}", mapOfRoutes);
        } catch (IOException e) {
            logger.error("Ошибка загруки файла - {}", e.getMessage());
        } catch (OLE2NotOfficeXmlFileException e1) {
            logger.error("Некорректный формат файла заявок, необходим формат xlsx");
        }
    }

    public Map<Integer, Route> getMapOfRoutes() {
        return mapOfRoutes;
    }

    public void setMapOfRoutes(Map<Integer, Route> mapOfRoutes) {
        this.mapOfRoutes = mapOfRoutes;
    }

    public void setFile(File file) {
        this.file = file;
        fillMap();
    }
}
