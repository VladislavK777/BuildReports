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

import com.uraltranscom.buildreports.model_ex.ResultClazz;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WriteToFileExcel {
    // Подключаем логгер
    private Logger logger = LoggerFactory.getLogger(WriteToFileExcel.class);

    // Успешная выгрузка
    private SimpleDateFormat dateFormat = new SimpleDateFormat();
    private XSSFWorkbook xssfWorkbook;

    private WriteToFileExcel() {
    }

    public void downloadFileExcel(HttpServletResponse response, Map<Map<String, Boolean>, Map<List<ResultClazz>, Integer>> totalMap) {
        try {
            String fileName = "Report_" + dateFormat.format(new Date()) + ".xlsx";
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
            response.setContentType("application/vnd.ms-excel");

            writeToFileExcel(response, totalMap);

        } catch (Exception e) {
            logger.error("Ошибка записи в файл - {}", e.getMessage());
        }

    }

    public synchronized void writeToFileExcel(HttpServletResponse response, Map<Map<String, Boolean>, Map<List<ResultClazz>, Integer>> totalMap) {
        logger.info("start");
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            File file = new File(classLoader.getResource("plan.xlsx").getFile());

            ServletOutputStream outputStream = response.getOutputStream();

            try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file))) {
                ZipSecureFile.setMinInflateRatio(-1.0d);
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fis);
                XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
                for (int j = 5; j < sheet.getLastRowNum() + 1; j++) {
                    for (Map.Entry<Map<String, Boolean>, Map<List<ResultClazz>, Integer>> map : totalMap.entrySet()) {
                        for (Map.Entry<String, Boolean> mapKey: map.getKey().entrySet()) {
                            if (!mapKey.getValue()) {
                                XSSFRow xssfRow = sheet.getRow(j);
                                String val = xssfRow.getCell(12).getStringCellValue();
                                if (val.equals(mapKey.getKey())) {
                                    // Вставляем запись
                                    int q = j;
                                    for (Map.Entry<List<ResultClazz>, Integer> resultValue : map.getValue().entrySet()) {
                                        if (resultValue.getValue() > 1) {
                                            for (ResultClazz resultClazz : resultValue.getKey()) {
                                                if (!resultClazz.isOk()) {
                                                    int i = sheet.getLastRowNum();
                                                    for (int k = i; k > q; k--) {
                                                        copyRow(xssfWorkbook, sheet, k, k + 1);
                                                    }
                                                    int cellForFormula = q + 2;
                                                    int cellFixForFormula = cellForFormula + resultValue.getValue() - 1;
                                                    XSSFRow rowNew = sheet.getRow(q + 1);
                                                    Cell cell1 = rowNew.createCell(0);
                                                    cell1.setCellValue(resultClazz.getNameRoadOfStationDeparture());
                                                    Cell cell2 = rowNew.createCell(1);
                                                    cell2.setCellValue(resultClazz.getNameOfStationDeparture());
                                                    cell2.setCellStyle(cellStyleAlignmentLeft(sheet));
                                                    Cell cell3 = rowNew.createCell(2);
                                                    if (resultClazz.getCustomer() == null) {
                                                        cell3.setCellValue("НЕТ ЗАЯВКИ");
                                                        cell3.setCellStyle(cellStyleForCustomerEmpty(sheet));
                                                    } else {
                                                        cell3.setCellValue(resultClazz.getCustomer());
                                                        cell3.setCellStyle(cellStyleAlignmentLeft(sheet));
                                                    }
                                                    Cell cell4 = rowNew.createCell(3);
                                                    cell4.setCellValue(resultClazz.getVolume());
                                                    cell4.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell5 = rowNew.createCell(4);
                                                    cell5.setCellValue(resultClazz.getCount());
                                                    cell5.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell6 = rowNew.createCell(5);
                                                    cell6.setCellValue(resultClazz.getCountInDate());
                                                    cell6.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell7 = rowNew.createCell(6);
                                                    cell7.setCellFormula("SUM(F" + cellForFormula + ":F" + cellFixForFormula + ")-SUM(E" + cellForFormula + ":E" + cellFixForFormula + ")/$M$4*$M$3");
                                                    cell7.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell8 = rowNew.createCell(7);
                                                    cell8.setCellFormula("SUM(F" + cellForFormula + ":F" + cellFixForFormula + ")/(SUM(E" + cellForFormula + ":E" + cellFixForFormula + ")/$M$4*$M$3)");
                                                    cell8.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell9 = rowNew.createCell(8);
                                                    cell9.setCellValue(resultClazz.getCountLoading());
                                                    cell9.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell10 = rowNew.createCell(9);
                                                    cell10.setCellValue(resultClazz.getAvarageStopAtStation());
                                                    cell10.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell11 = rowNew.createCell(10);
                                                    cell11.setCellValue(resultClazz.getCountDrive());
                                                    cell11.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell12 = rowNew.createCell(11);
                                                    cell12.setCellValue(resultClazz.getCountInDateSpravo4no());
                                                    cell12.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell13 = rowNew.createCell(12);
                                                    cell13.setCellValue("");
                                                    q++;
                                                    resultClazz.setOk(true);
                                                }
                                            }
                                            sheet.addMergedRegion(new CellRangeAddress(q - resultValue.getValue() + 1, q,1, 1));
                                            sheet.addMergedRegion(new CellRangeAddress(q - resultValue.getValue() + 1, q,3, 3));
                                            sheet.addMergedRegion(new CellRangeAddress(q - resultValue.getValue() + 1, q,6, 6));
                                            sheet.addMergedRegion(new CellRangeAddress(q - resultValue.getValue() + 1, q,7, 7));
                                            sheet.addMergedRegion(new CellRangeAddress(q - resultValue.getValue() + 1, q,8, 8));
                                            sheet.addMergedRegion(new CellRangeAddress(q - resultValue.getValue() + 1, q,9, 9));
                                            sheet.addMergedRegion(new CellRangeAddress(q - resultValue.getValue() + 1, q,10, 10));
                                        } else {
                                            for (ResultClazz resultClazz : resultValue.getKey()) {
                                                if (!resultClazz.isOk()) {
                                                    int i = sheet.getLastRowNum();
                                                    for (int k = i; k > q; k--) {
                                                        copyRow(xssfWorkbook, sheet, k, k + 1);
                                                    }
                                                    int cellForFormula = q + 2;
                                                    XSSFRow rowNew = sheet.getRow(q + 1);
                                                    Cell cell1 = rowNew.createCell(0);
                                                    cell1.setCellValue(resultClazz.getNameRoadOfStationDeparture());
                                                    Cell cell2 = rowNew.createCell(1);
                                                    cell2.setCellValue(resultClazz.getNameOfStationDeparture());
                                                    cell2.setCellStyle(cellStyleAlignmentLeft(sheet));
                                                    Cell cell3 = rowNew.createCell(2);
                                                    if (resultClazz.getCustomer() == null) {
                                                        cell3.setCellValue("НЕТ ЗАЯВКИ");
                                                        cell3.setCellStyle(cellStyleForCustomerEmpty(sheet));
                                                    } else {
                                                        cell3.setCellValue(resultClazz.getCustomer());
                                                        cell3.setCellStyle(cellStyleAlignmentLeft(sheet));
                                                    }
                                                    Cell cell4 = rowNew.createCell(3);
                                                    cell4.setCellValue(resultClazz.getVolume());
                                                    cell4.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell5 = rowNew.createCell(4);
                                                    cell5.setCellValue(resultClazz.getCount());
                                                    cell5.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell6 = rowNew.createCell(5);
                                                    cell6.setCellValue(resultClazz.getCountInDate());
                                                    cell6.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell7 = rowNew.createCell(6);
                                                    cell7.setCellFormula("F" + cellForFormula + "-E" + cellForFormula + "/$M$4*$M$3");
                                                    Cell cell8 = rowNew.createCell(7);
                                                    cell7.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    cell8.setCellFormula("F" + cellForFormula + "/(E" + cellForFormula + "/$M$4*$M$3)");
                                                    cell8.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell9 = rowNew.createCell(8);
                                                    cell9.setCellValue(resultClazz.getCountLoading());
                                                    cell9.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell10 = rowNew.createCell(9);
                                                    cell10.setCellValue(resultClazz.getAvarageStopAtStation());
                                                    cell10.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell11 = rowNew.createCell(10);
                                                    cell11.setCellValue(resultClazz.getCountDrive());
                                                    cell11.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell12 = rowNew.createCell(11);
                                                    cell12.setCellValue(resultClazz.getCountInDateSpravo4no());
                                                    cell12.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell13 = rowNew.createCell(12);
                                                    cell13.setCellValue("");
                                                    q++;
                                                    resultClazz.setOk(true);
                                                }
                                            }
                                        }
                                        mapKey.setValue(true);
                                    }
                                }
                            }
                        }
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

    private static XSSFCellStyle cellStyleAlignmentCenter(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleForCustomerEmpty(XSSFSheet sheet) {
        Font fontTitle = sheet.getWorkbook().createFont();
        fontTitle.setColor(HSSFColor.RED.index);
        fontTitle.setBold(true);
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(fontTitle);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleAlignmentLeft(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    private void copyRow(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {

        XSSFRow newRow = worksheet.createRow(destinationRowNum);
        XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

        for (int i = 0; i < 13; i++) {

            XSSFCell oldCell = sourceRow.getCell(i);
            XSSFCell newCell = newRow.createCell(i);

            if (oldCell == null) {
                newCell = null;
                continue;
            }

            XSSFCellStyle newCellStyle = oldCell.getCellStyle();
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
}
