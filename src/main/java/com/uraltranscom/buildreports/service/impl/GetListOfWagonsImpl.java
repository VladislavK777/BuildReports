package com.uraltranscom.buildreports.service.impl;

import com.uraltranscom.buildreports.model.ReplaceNameStationClazz;
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
import java.util.*;

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
    private List<Wagon> listOfWagonsFull = new ArrayList<>();
    private List<Wagon> listOfWagonsEmpty = new ArrayList<>();

    // Переменные для работы с файлами
    private File file ;
    private FileInputStream fileInputStream;
    private List<Date> dates;
    private List<Date> datesSpravo4no;

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
        listOfWagonsFull.clear();
        listOfWagonsEmpty.clear();
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
                String customer = null;

                for (int c = 0; c < row.getLastCellNum(); c++) {
                    if (row.getCell(c).getStringCellValue().trim().equals("Станция назначения")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        nameOfStationDestination = ReplaceNameStationClazz.getNameRootStation(xssfRow.getCell(c).getStringCellValue());
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Дорога назначения")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        nameRoadStationDestination = xssfRow.getCell(c).getStringCellValue();
                        if (nameRoadStationDestination.equals("УЗБ")) {
                            nameRoadStationDestination = "УТИ";
                        }
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Станция отправления")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        nameOfStationDeparture = ReplaceNameStationClazz.getNameRootStation(xssfRow.getCell(c).getStringCellValue());
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Дорога отправления, наименование")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        roadOfStationDeparture = xssfRow.getCell(c).getStringCellValue();
                        if (roadOfStationDeparture.equals("УЗБ")) {
                            roadOfStationDeparture = "УТИ";
                        }
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Дата отправления")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        dateToDeparted = xssfRow.getCell(c).getDateCellValue();
                        dateToDeparted.setHours(0);
                        dateToDeparted.setMinutes(0);
                        dateToDeparted.setSeconds(0);
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
                    if (row.getCell(c).getStringCellValue().trim().equals("Заказчик")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        customer = xssfRow.getCell(c).getStringCellValue();
                    }
                }
                if (!nameOfStationDestination.equals("00000")) {
                    if (volume == 122 || volume == 114) volume = 120;
                    if (volume == 140) volume = 138;
                    if (volume == 158) volume = 150;
                    if (emptyOrFull.equals("ПОР")) {
                        listOfWagonsEmpty.add(new Wagon(nameOfStationDestination, nameRoadStationDestination, nameOfStationDeparture, roadOfStationDeparture, volume, dateToDeparted, condition, stopAtStation, emptyOrFull, null));
                    } else {
                        listOfWagonsFull.add(new Wagon(nameOfStationDestination, nameRoadStationDestination, nameOfStationDeparture, roadOfStationDeparture, volume, dateToDeparted, condition, stopAtStation, emptyOrFull, customer));
                    }
                }
            }
            logger.debug("Body wagon: {}, {}", listOfWagonsFull, listOfWagonsEmpty);
        } catch (IOException e) {
            logger.error("Ошибка загруки файла - {}", e.getMessage());
        } catch (OLE2NotOfficeXmlFileException e1) {
            logger.error("Некорректный формат файла дислокации, необходим формат xlsx");
        }

    }

    public List<Wagon> getListCountWagon(List<Wagon> listOfWagonsEmpty, List<Wagon> listOfWagonsFull) {
        List<Wagon> listCountWagon = new ArrayList<>();
        if (listOfWagonsFull.isEmpty() || listOfWagonsEmpty.isEmpty()) {
            logger.error("Списоки вагонов пусты");
        } else {
            for (Wagon wagon : listOfWagonsEmpty) {
                int count = 0;
                int countLoading = 0;
                int countDrive = 0;
                double stopAtStation = 0.00d;
                StringBuilder stringVolume = new StringBuilder();
                TreeSet<Integer> listVolume = new TreeSet<>();
                for (Wagon wagon1 : listOfWagonsEmpty) {
                    if (wagon.getNameOfStationDestination().equals(wagon1.getNameOfStationDestination())) {
                        count++;
                        listVolume.add(wagon1.getVolume());
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
                            case "В отстой(е)":
                                countDrive++;
                                break;
                        }
                    }
                }
                Iterator iterator = listVolume.iterator();
                while (iterator.hasNext()) {
                    Object volume = iterator.next();
                    if (volume != listVolume.last()) {
                        stringVolume.append(String.valueOf(volume)).append("/");
                    } else {
                        stringVolume.append(String.valueOf(volume));
                    }
                }
                if (!listCountWagon.contains(new Wagon(wagon.getNameOfStationDestination(), wagon.getNameRoadStationDestination(), null, null,
                        stringVolume.toString(), count, countLoading, countDrive, 0, 0, Math.round((stopAtStation / countLoading) * 100.0) / 100.0d, null))) {
                    listCountWagon.add(new Wagon(wagon.getNameOfStationDestination(), wagon.getNameRoadStationDestination(), null, null,
                            stringVolume.toString(), count, countLoading, countDrive, 0, 0, Math.round((stopAtStation / countLoading) * 100.0) / 100.0d, null));
                }
            }
            for (Wagon wagon : listOfWagonsFull) {
                int countInDate = 0;
                int countInDateSpravo4no = 0;
                StringBuilder stringVolume = new StringBuilder();
                TreeSet<Integer> listVolume = new TreeSet<>();
                for (Wagon wagon1 : listOfWagonsFull) {
                    if (wagon.getNameOfStationDeparture().equals(wagon1.getNameOfStationDeparture()) &&
                            wagon.getCustomer().equals(wagon1.getCustomer()) &&
                            dates.get(0).getTime() <= wagon1.getDateToDeparted().getTime() &&
                            wagon1.getDateToDeparted().getTime() <= dates.get(1).getTime()) {
                        listVolume.add(wagon1.getVolume());
                        countInDate++;
                    }
                    if (wagon.getNameOfStationDeparture().equals(wagon1.getNameOfStationDeparture()) &&
                            wagon.getCustomer().equals(wagon1.getCustomer()) &&
                            datesSpravo4no.get(0).getTime() <= wagon1.getDateToDeparted().getTime() &&
                            wagon1.getDateToDeparted().getTime() <= datesSpravo4no.get(1).getTime()) {
                        listVolume.add(wagon1.getVolume());
                        countInDateSpravo4no++;
                    }
                }
                Iterator iterator = listVolume.iterator();
                while (iterator.hasNext()) {
                    Object volume = iterator.next();
                    if (volume != listVolume.last()) {
                        stringVolume.append(String.valueOf(volume)).append("/");
                    } else {
                        stringVolume.append(String.valueOf(volume));
                    }
                }
                if (countInDate > 0) {
                    if (!listCountWagon.contains(new Wagon(null, null, wagon.getNameOfStationDeparture(), wagon.getNameRoadOfStationDeparture(),
                            stringVolume.toString(), 0, 0, 0, countInDate, countInDateSpravo4no,0.00d, wagon.getCustomer()))) {
                        listCountWagon.add(new Wagon(null, null, wagon.getNameOfStationDeparture(), wagon.getNameRoadOfStationDeparture(),
                                stringVolume.toString(), 0, 0, 0, countInDate, countInDateSpravo4no, 0.00d, wagon.getCustomer()));
                    }
                }
            }
        }
        logger.debug("listCountWagon: {}", listCountWagon);
        return listCountWagon;
    }

    public List<Wagon> getListOfWagonsFull() {
        return listOfWagonsFull;
    }

    public void setListOfWagonsFull(List<Wagon> listOfWagonsFull) {
        this.listOfWagonsFull = listOfWagonsFull;
    }

    public List<Wagon> getListOfWagonsEmpty() {
        return listOfWagonsEmpty;
    }

    public void setListOfWagonsEmpty(List<Wagon> listOfWagonsEmpty) {
        this.listOfWagonsEmpty = listOfWagonsEmpty;
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

    public List<Date> getDatesSpravo4no() {
        return datesSpravo4no;
    }

    public void setDatesSpravo4no(List<Date> datesSpravo4no) {
        this.datesSpravo4no = datesSpravo4no;
    }
}
