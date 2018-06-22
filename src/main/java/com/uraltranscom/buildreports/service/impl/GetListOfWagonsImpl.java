package com.uraltranscom.buildreports.service.impl;

import com.uraltranscom.buildreports.model.Wagon;
import com.uraltranscom.buildreports.service.GetList;
import com.uraltranscom.buildreports.service.export.WriteToFileExcel;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Класс получения списка вагонов
 *
 * @author Vladislav Klochkov
 * @version 1.0
 * @create 13.06.2018
 *
 * 13.06.2018
 *   1. Версия 1.0
 *
 */

@Service
public class GetListOfWagonsImpl implements GetList {
    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(GetListOfWagonsImpl.class);

    // Основаная мапа, куда записываем все вагоны
    private List<Wagon> listOfWagons = new ArrayList<>();

    // Переменные для работы с файлами
    private File file ;
    private FileInputStream fileInputStream;
    private List<Date> dates;

    // Переменные для работы с Excel файлом(формат XLSX)
    private XSSFWorkbook xssfWorkbook;
    private XSSFSheet sheet;

    @Autowired
    private WriteToFileExcel writeToFileExcel;

    public GetListOfWagonsImpl() {
    }

    // Заполняем Map вагонами
    @Override
    public void fillMap() {
        System.out.println(file.toString());
        listOfWagons.clear();
        // Получаем файл формата xls
        try {
            fileInputStream = new FileInputStream(this.file);
            xssfWorkbook = new XSSFWorkbook(fileInputStream);

            // Заполняем мапу данными
            sheet = xssfWorkbook.getSheetAt(0);
            for (int j = 1; j < sheet.getLastRowNum() + 1; j++) {
                XSSFRow row = sheet.getRow(0);

                String nameOfStationDestination = null;
                String nameRoadStationDestination = null;
                String nameOfStationDeparture = null;
                String roadOfStationDeparture = null;
                int volume = 0;
                Date dateToDeparted = null;
                String condition = null;
                double stopAtStation = 0.00d;
                String emptyOrFull = null;

                for (int c = 0; c < row.getLastCellNum(); c++) {
                    if (row.getCell(c).getStringCellValue().trim().equals("Станция назначения")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        nameOfStationDestination = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Дорога назначения")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        nameRoadStationDestination = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Станция отправления")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        nameOfStationDeparture = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Дорога отправления, наименование")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        roadOfStationDeparture = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Дата отправления")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        dateToDeparted = xssfRow.getCell(c).getDateCellValue();
                        if (dateToDeparted == null) dateToDeparted = new Date();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Состояние вагона")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        condition = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Обьем вагона")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        volume = (int) xssfRow.getCell(c).getNumericCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Простой на станции дислокации")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        stopAtStation = xssfRow.getCell(c).getNumericCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Груж\\Порож")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        emptyOrFull = xssfRow.getCell(c).getStringCellValue();
                    }
                }
                if (!nameOfStationDestination.equals("00000")) {
                    if (volume == 122) volume = 120;
                    if (volume == 158) volume = 150;
                    listOfWagons.add(new Wagon(nameOfStationDestination, nameRoadStationDestination, nameOfStationDeparture, roadOfStationDeparture, volume, dateToDeparted, condition, stopAtStation, emptyOrFull));
                }
            }
            logger.debug("Body wagon: {}", listOfWagons);
        } catch (IOException e) {
            logger.error("Ошибка загруки файла - {}", e.getMessage());
        } catch (OLE2NotOfficeXmlFileException e1) {
            logger.error("Некорректный формат файла дислокации, необходим формат xlsx");
        }

    }

    public List<Wagon> getListCountWagon(List<Wagon> listOfWagons) {
        List<Wagon> listCountWagon = new ArrayList<>();
        if (listOfWagons.isEmpty()) {
            logger.error("Список вагонов пустой");
        } else {
            for (Wagon wagon : listOfWagons) {
                int count = 0;
                int countLoading = 0;
                int countDrive = 0;
                int countInDate = 0;
                double stopAtStation = 0.00d;
                for (Wagon wagon1 : listOfWagons) {
                    if (wagon.getNameOfStationDestination().equals(wagon1.getNameOfStationDestination()) &&
                            wagon.getVolume() == wagon1.getVolume() &&
                            wagon.getEmptyOrFull().equals("ПОР") && wagon1.getEmptyOrFull().equals("ПОР")) {
                        count++;
                        switch (wagon1.getCondition()) {
                            case "Под погрузкой":
                                countLoading++;
                                stopAtStation = stopAtStation + wagon1.getStopAtStation();
                                break;
                            case "Порожний ход":
                                countDrive++;
                                break;
                            case "Перестановка":
                                countDrive++;
                                break;
                            case "На промывке":
                                countDrive++;
                                break;
                        }
                    }
                }
                /*if (wagon1.getEmptyOrFull().equals("ГРУЖ")) {
                    if (dates.get(0).getTime() <= wagon1.getDateToDeparted().getTime() && wagon1.getDateToDeparted().getTime() <= dates.get(1).getTime()) {
                        countInDate++;
                    }
                }*/
                if (countInDate > 0) {
                    if (!listCountWagon.contains(new Wagon(wagon.getNameOfStationDeparture(), wagon.getNameRoadOfStationDeparture(),
                            wagon.getVolume(), 0, 0, 0, countInDate, 0.00d))) {
                        listCountWagon.add(new Wagon(wagon.getNameOfStationDeparture(), wagon.getNameRoadOfStationDeparture(),
                                wagon.getVolume(), 0, 0, 0, countInDate, 0.00d));
                    }
                } else {
                    if (!listCountWagon.contains(new Wagon(wagon.getNameOfStationDestination(), wagon.getNameRoadStationDestination(),
                            wagon.getVolume(), count, countLoading, countDrive, countInDate, Math.round((stopAtStation / countLoading) * 100.0) / 100.0d))) {
                        listCountWagon.add(new Wagon(wagon.getNameOfStationDestination(), wagon.getNameRoadStationDestination(),
                                wagon.getVolume(), count, countLoading, countDrive, countInDate, Math.round((stopAtStation / countLoading) * 100.0) / 100.0d));
                    }
                }
            }
        }
        logger.debug("listCountWagon: {}", listCountWagon);
        return listCountWagon;
    }

    public List<Wagon> getListOfWagons() {
        return listOfWagons;
    }

    public void setListOfWagons(List<Wagon> listOfWagons) {
        this.listOfWagons = listOfWagons;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        fillMap();
    }

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(ArrayList<Date> dates) {
        this.dates = dates;
    }
}
