package com.uraltranscom.buildreports.service.impl;

import com.uraltranscom.buildreports.model.Wagon;
import com.uraltranscom.buildreports.service.export.WriteToFileExcel;
import com.uraltranscom.buildreports.service.GetList;
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

                String nameOfStationDeparture = null;
                String nameRoadOfStationDeparture = null;
                String nameOfStationDestination = null;
                String nameRoadStationDestination = null;
                String customer = null;
                String cargo = null;
                String distance = null;
                double rate = 0.00d;

                for (int c = 0; c < row.getLastCellNum(); c++) {
                    if (row.getCell(c).getStringCellValue().trim().equals("Станция отправления")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        nameOfStationDeparture = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Дор. отпр.")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        nameRoadOfStationDeparture = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Станция назначения")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        nameOfStationDestination = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Дор. назн.")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        nameRoadStationDestination = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Заказчик")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        customer = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Груз (ЕТСНГ)")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        cargo = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Расстояние всего")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        String val = Double.toString(xssfRow.getCell(c).getNumericCellValue());
                        double valueDouble = xssfRow.getCell(c).getNumericCellValue();
                        if ((valueDouble - (int) valueDouble) * 1000 == 0) {
                            val = (int) valueDouble + "";
                        }
                        distance = val;
                    }
                    if (row.getCell(c).getStringCellValue().trim().equals("Ставка с Заказчиком, р/ваг (без НДС)")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        rate = xssfRow.getCell(c).getNumericCellValue();
                    }
                }
                listOfWagons.add(new Wagon(nameOfStationDeparture, nameRoadOfStationDeparture, nameOfStationDestination, nameRoadStationDestination, customer, cargo, distance, rate));
            }
            logger.debug("Body wagon: {}", listOfWagons);
            writeToFileExcel.setTempListWagon(getListCountWagon(listOfWagons));
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
                int distance = 0;
                double rate = 0.00d;
                for (Wagon wagon1 : listOfWagons) {
                    if (wagon.getNameOfStationDeparture().equals(wagon1.getNameOfStationDeparture()) &&
                            wagon.getNameOfStationDestination().equals(wagon1.getNameOfStationDestination()) &&
                            wagon.getCustomer().equals(wagon1.getCustomer())) {
                        count++;
                        distance = distance + Integer.valueOf(wagon1.getDistance());
                        rate = rate + wagon1.getRate();
                    }
                }
                if (!listCountWagon.contains(new Wagon(wagon.getNameOfStationDeparture(), wagon.getNameRoadOfStationDeparture(), wagon.getNameOfStationDestination(), wagon.getNameRoadStationDestination(),
                        wagon.getCustomer(), wagon.getCargo(), count, distance / count, Math.round((rate / count) * 100.0) / 100.0d))) {
                    listCountWagon.add(new Wagon(wagon.getNameOfStationDeparture(), wagon.getNameRoadOfStationDeparture(), wagon.getNameOfStationDestination(), wagon.getNameRoadStationDestination(),
                            wagon.getCustomer(), wagon.getCargo(), count, distance / count, Math.round((rate / count) * 100.0) / 100.0d));
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
}
