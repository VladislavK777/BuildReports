package com.uraltranscom.buildreports.service.export;

/**
 *
 * Класс записи в файл Excel
 *
 * @author Vladislav Klochkov
 * @version 1.0
 * @create 13.06.2018
 *
 * 13.06.2018
 *   1. Версия 1.0
 *
 */

import com.uraltranscom.buildreports.model.Wagon;
import com.uraltranscom.buildreports.service.impl.GetListOfWagonsImpl;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class WriteToFileExcel {
    // Подключаем логгер
    private Logger logger = LoggerFactory.getLogger(WriteToFileExcel.class);

    // Успешная выгрузка
    private SimpleDateFormat dateFormat = new SimpleDateFormat();
    private XSSFWorkbook xssfWorkbook;
    private List<Wagon> tempListWagon;

    private File file;

    @Autowired
    private GetListOfWagonsImpl getListOfWagons;

    private WriteToFileExcel() {
        file = null;
    }

    public void downloadFileExcel(HttpServletResponse response) {
        try {
            String fileName = "Report_" + dateFormat.format(new Date()) + ".xlsx";
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
            response.setContentType("application/vnd.ms-excel");

            writeToFileExcel(response);

        } catch (Exception e) {
            logger.error("Ошибка записи в файл - {}", e.getMessage());
        }

    }

    public synchronized void writeToFileExcel(HttpServletResponse response) {
        logger.info("start");
        try {
            ServletOutputStream outputStream = response.getOutputStream();

            try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file))) {
                ZipSecureFile.setMinInflateRatio(-1.0d);
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fis);
                XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
                for (int j = 4; j < sheet.getLastRowNum() + 1; j++) {
                    XSSFRow row = sheet.getRow(2);
                    for (Wagon wagon : tempListWagon) {
                        for (int c = 0; c < row.getLastCellNum(); c++) {
                            if (row.getCell(c).getStringCellValue().equals(" Заказчик ")) {
                                XSSFRow xssfRow = sheet.getRow(j);
                                String val = xssfRow.getCell(c).getStringCellValue();
                                if (val.equals(wagon.getCustomer())) {
                                    for (int q = 0; q < row.getLastCellNum(); q++) {
                                        if (row.getCell(q).getStringCellValue().equals(" Ст. отправления ")) {
                                            XSSFRow xssfRow1 = sheet.getRow(j);
                                            String val1 = xssfRow1.getCell(q).getStringCellValue();
                                            if (val1.equals(wagon.getNameOfStationDeparture())) {
                                                for (int w = 0; w < row.getLastCellNum(); w++) {
                                                    if (row.getCell(w).getStringCellValue().equals(" Ст. назначения ")) {
                                                        XSSFRow xssfRow2 = sheet.getRow(j);
                                                        String val2 = xssfRow2.getCell(w).getStringCellValue();
                                                        if (val2.equals(wagon.getNameOfStationDestination())) {
                                                            for (int e = 0; e < row.getLastCellNum(); e++) {
                                                                if (row.getCell(e).getStringCellValue().equals("Расст., км.")) {
                                                                    Cell cell = xssfRow2.createCell(e);
                                                                    cell.setCellValue(wagon.getAverageDistance());
                                                                    cell.setCellStyle(cellStyle(sheet));
                                                                }
                                                                if (row.getCell(e).getStringCellValue().equals("Ставка, р/ваг (без НДС)")) {
                                                                    Cell cell = xssfRow2.createCell(e);
                                                                    cell.setCellValue(wagon.getAverageRate());
                                                                    cell.setCellStyle(cellStyle(sheet));
                                                                }
                                                                Cell cell = xssfRow2.createCell(15);
                                                                cell.setCellValue(wagon.getCount());
                                                                cell.setCellStyle(cellStyle(sheet));

                                                                wagon.setOk(true);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                for (int j = 4; j < sheet.getLastRowNum() + 1; j++) {
                    XSSFRow row = sheet.getRow(2);
                    for (Wagon wagon : tempListWagon) {
                        if (!wagon.isOk()) {
                            for (int c = 0; c < row.getLastCellNum(); c++) {
                                if (row.getCell(c).getStringCellValue().equals(" Дор. отпр. ")) {
                                    XSSFRow xssfRow = sheet.getRow(j);
                                    String val = xssfRow.getCell(c).getStringCellValue();
                                    if (val.equals(wagon.getNameRoadOfStationDeparture())) {
                                        int i = sheet.getLastRowNum();
                                        for (int k = i; k > j - 1; k--) {
                                            copyRow(xssfWorkbook, sheet, k, k + 1);
                                        }

                                        // Вставляем запись
                                        XSSFRow rowNew = sheet.createRow(j);
                                        Cell cell1 = rowNew.createCell(2);
                                        cell1.setCellValue(wagon.getCustomer());
                                        cell1.setCellStyle(cellStyle(sheet));
                                        Cell cell2 = rowNew.createCell(3);
                                        cell2.setCellValue(wagon.getNameRoadOfStationDeparture());
                                        cell2.setCellStyle(cellStyle(sheet));
                                        Cell cell3 = rowNew.createCell(4);
                                        cell3.setCellValue(wagon.getNameOfStationDeparture());
                                        cell3.setCellStyle(cellStyle(sheet));
                                        Cell cell4 = rowNew.createCell(5);
                                        cell4.setCellValue(wagon.getNameRoadStationDestination());
                                        cell4.setCellStyle(cellStyle(sheet));
                                        Cell cell5 = rowNew.createCell(6);
                                        cell5.setCellValue(wagon.getNameOfStationDestination());
                                        cell5.setCellStyle(cellStyle(sheet));
                                        Cell cell6 = rowNew.createCell(7);
                                        cell6.setCellValue(wagon.getCargo());
                                        cell6.setCellStyle(cellStyle(sheet));
                                        Cell cell7 = rowNew.createCell(8);
                                        cell7.setCellValue(wagon.getAverageDistance());
                                        cell7.setCellStyle(cellStyle(sheet));
                                        Cell cell8 = rowNew.createCell(11);
                                        cell8.setCellValue(wagon.getAverageRate());
                                        cell8.setCellStyle(cellStyle(sheet));
                                        Cell cell9 = rowNew.createCell(15);
                                        cell9.setCellValue(wagon.getCount());
                                        cell9.setCellStyle(cellStyle(sheet));
                                        wagon.setOk(true);
                                    }
                                }
                            }
                        }
                    }
                }
                for (Wagon wagon : tempListWagon) {
                    if (!wagon.isOk()) {
                        int i = sheet.getLastRowNum();
                        copyRow(xssfWorkbook, sheet, i, i + 1);

                        // Вставляем запись
                        XSSFRow rowNew = sheet.createRow(i);
                        Cell cell1 = rowNew.createCell(2);
                        cell1.setCellValue(wagon.getCustomer());
                        cell1.setCellStyle(cellStyle(sheet));
                        Cell cell2 = rowNew.createCell(3);
                        cell2.setCellValue(wagon.getNameRoadOfStationDeparture());
                        cell2.setCellStyle(cellStyle(sheet));
                        Cell cell3 = rowNew.createCell(4);
                        cell3.setCellValue(wagon.getNameOfStationDeparture());
                        cell3.setCellStyle(cellStyle(sheet));
                        Cell cell4 = rowNew.createCell(5);
                        cell4.setCellValue(wagon.getNameRoadStationDestination());
                        cell4.setCellStyle(cellStyle(sheet));
                        Cell cell5 = rowNew.createCell(6);
                        cell5.setCellValue(wagon.getNameOfStationDestination());
                        cell5.setCellStyle(cellStyle(sheet));
                        Cell cell6 = rowNew.createCell(7);
                        cell6.setCellValue(wagon.getCargo());
                        cell6.setCellStyle(cellStyle(sheet));
                        Cell cell7 = rowNew.createCell(8);
                        cell7.setCellValue(wagon.getAverageDistance());
                        cell7.setCellStyle(cellStyle(sheet));
                        Cell cell8 = rowNew.createCell(11);
                        cell8.setCellValue(wagon.getAverageRate());
                        cell8.setCellStyle(cellStyle(sheet));
                        Cell cell9 = rowNew.createCell(15);
                        cell9.setCellValue(wagon.getCount());
                        cell9.setCellStyle(cellStyle(sheet));
                        wagon.setOk(true);
                    }
                }

                xssfWorkbook.write(outputStream);
                outputStream.flush();
                outputStream.close();
                logger.info("end");
            }
        } catch (IOException e) {
            logger.error("Ошибка записи в файл - {}", e.getMessage());
        }
    }

    private void copyRow(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {

        XSSFRow newRow = worksheet.createRow(destinationRowNum);
        XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {

            XSSFCell oldCell = sourceRow.getCell(i);
            XSSFCell newCell = newRow.createCell(i);

            if (oldCell == null) {
                newCell = null;
                continue;
            }

            XSSFCellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            newCell.setCellStyle(newCellStyle);

            // Устанавливаем тип
            switch (oldCell.getCellTypeEnum()) {
                case BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case BOOLEAN:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
                case _NONE:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
            }
        }

        // Объединенные ячейки
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                        (newRow.getRowNum() +
                                (cellRangeAddress.getFirstRow() -
                                        cellRangeAddress.getLastRow())),
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }
    }


    private static XSSFCellStyle cellStyle(XSSFSheet sheet) {
        Font fontTitle = sheet.getWorkbook().createFont();
        fontTitle.setColor(HSSFColor.BLACK.index);
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(fontTitle);
        cellStyle.setFillForegroundColor(new XSSFColor(new Color(204, 255, 204)));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyle;
    }

    public List<Wagon> getTempListWagon() {
        return tempListWagon;
    }

    public void setTempListWagon(List<Wagon> tempListWagon) {
        this.tempListWagon = tempListWagon;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public GetListOfWagonsImpl getGetListOfWagons() {
        return getListOfWagons;
    }

    public void setGetListOfWagons(GetListOfWagonsImpl getListOfWagons) {
        this.getListOfWagons = getListOfWagons;
    }
}
