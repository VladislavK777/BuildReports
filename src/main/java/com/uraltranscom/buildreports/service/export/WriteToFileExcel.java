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
import org.apache.poi.ss.usermodel.*;
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
import java.util.*;

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
        String [] repairStations = {"Ярославль-Главный", "Юлемисте", "Юдино", "Электросталь", "Шушары", "Шкиротава", "Шепетовка", "Чусовская", "Чукурсай", "Чита I", "Чертаново", "Черняховск", "Чернышевск-Забайкальский", "Черногорские Копи", "Челутай", "Хилок", "Хабаровск I", "Успенская", "Уруша", "Уральск", "Унеча", "Ульяновск-Центральный", "Улан-Удэ", "Узловая I", "Ужур", "Тырган", "Тында", "Туркестан", "Тула I-Курская", "Трубная", "Тосно", "Томусинская", "Томск II", "Тихонова Пустынь", "Тахиаташ", "Тальцы", "Таксимо", "Тайшет", "Тайга", "Суховская-Южная", "Суховская", "Старый Оскол", "Старомарьевская", "Спиченково", "Сосыка-Ейская", "Сосногорск", "Сороковая", "Сольвычегодск", "Смычка", "Смоленск-Сортировочный", "Слюдянка I", "Сковородино", "Серов-Сортировочный", "Сенная", "Северная", "Себряково", "Свислочь", "Сасово", "Сарепта", "Саратов II-Товарный", "Санкт-Петербург-Тов.-Витебский", "Санкт-Петербург-Сорт.-Московский", "Сальск", "Рыбное", "Рузаевка", "Ружино", "Рубцовск", "Ртищево I", "РЗД 470", "Решоты", "Резекне II", "Разъезд 132 км", "Пятихатки", "Пугачевск", "Пруды", "Прохладная", "Проектная", "Присады", "Приисковая", "Предпортовая", "Предкомбинат", "Посьет", "Полевской", "Подсиний", "Поворино", "Плеханово", "Петрозаводск", "Петров Вал", "Пермь-Сортировочная", "Пенза III", "Палагиада", "Павлодар", "Павловск-Воронежский", "Павелец-Тульский", "Оскемен I", "Орша-Центральная", "Орша-Восточная", "Орск", "Орехово-Зуево", "Оренбург", "Омск-Пассажирский", "Оленегорск", "Октябрьск", "Ожерелье", "Обшаровка", "Обозерская", "Ноябрьск I", "Новый Ургал", "Новосокольники", "Новосибирск-Западный", "Новожаново", "Новгород-на-Волхове", "Николаев", "Нижний Новгород-Сортировочный", "Нижнеудинск", "Нижнеднепровск-Узел", "Несветай", "Нерюнгри-Грузовая", "Находка", "Наушки", "Назарово", "Мыски", "Мурманск", "Московка", "Молодечно", "Могоча", "Могилев II", "Михайловский Рудник", "Микунь", "Миасс I", "Междуреченск", "Махачкала", "Макат", "Магдагачи", "Лянгасово", "Льгов-Киевский", "Лужская", "Лоста", "Локоть", "Лодейное Поле", "Лихая", "Лиман", "Ленинск-Кузнецкий II", "Ленинск-Кузнецкий I", "Лена", "Курск", "Курган", "Курбакинская", "Купянск-Сортировочный", "Кузнечное", "Крымская", "Кременчуг", "Красноярск-Восточный", "Красноярск", "Красноуфимск", "Краснодар-Сортировочный", "Краснодар I", "Кошта", "Кочетовка I", "Костомукша-Товарная", "Коршуниха-Ангарская", "Коростень", "Коноша I", "Конотоп", "Комсомольск-Сортировочный", "Комсомольск-на-Амуре", "Ковдор", "Кинель", "Кильчуг", "Кийзак", "Керчь", "Кемь", "Кемерово-Сортировочное", "Качканар", "Карымская", "Карталы I", "Кандыагаш", "Каменск-Уральский", "Каменоломни", "Каменногорск", "Калуга I", "Калинковичи", "Калининград-Сортировочный", "Казачья Лопань", "Казаново", "Казалы", "Кавказская", "Ишаново", "Иркутск-Сортировочный", "Инчхе", "Инская", "Им. Максима Горького", "Иланская", "Иваново-Сортировочное", "Зуевка", "Златоуст", "Зима", "Зилупе", "Запорожье-Левое", "Заосколье", "Заозерная", "Жмеринка", "Жлобин", "Ерунаково", "Елец", "Елгава", "Екатеринбург-Сортировочный", "Ейск", "Егоршино", "Егозово", "Евсино", "Дружинино", "Докшукино", "Джанкой", "Дербент", "Денисовский рзд", "Дема", "Дарница", "Гудермес", "Гродно", "Гребенка", "Готня", "Городище", "Гороблагодатская", "Гончарово", "Гомель", "Гвоздево", "Гатчина-Товарная-Балтийская", "Вязьма-Брянская", "Вязьма", "Выборг", "Входная", "Воркута", "Волховстрой I", "Вологда II", "Вологда I", "Волковыск-Центральный", "Вихоревка", "Витебск", "Верхнезейск", "Верещагино", "Великие Луки", "Ванино", "Вагонозавод", "Бурундай", "Бурея", "Буй", "Бузулук", "Буденновск", "Брест-Восточный", "Борзя", "Бологое-Московское", "Боготол", "Бирюлинская", "Бирюлево-Товарная", "Бигосово-стык", "Беркакит", "Березки", "Бердяуш", "Беломорск", "Белогорск", "Белово", "Белгород", "Белая", "Бекасово-Сортировочное", "Батайск", "Барановичи-Центральные", "Барабинск", "Балаково", "Ачинск I", "АЦОНЕ", "Атырау", "Астрахань II", "Астана I", "Аскиз", "Арыс I", "Арчеда", "Армавир-Ростовский", "Арзамас II", "Апатиты", "Анжерская", "Андреедмитриевка", "Актогай", "Аксарайская II", "Агрыз"};

        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            File file = new File(classLoader.getResource("plan.xlsx").getFile());
            TreeSet<Integer> listRowTotalHeaders = new TreeSet<>();

            ServletOutputStream outputStream = response.getOutputStream();

            try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file))) {
                ZipSecureFile.setMinInflateRatio(-1.0d);
                xssfWorkbook = new XSSFWorkbook(fis);
                XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
                for (int j = 5; j < sheet.getLastRowNum() + 1; j++) {
                    for (Map.Entry<Map<String, Boolean>, Map<List<ResultClazz>, Integer>> map : totalMap.entrySet()) {
                        for (Map.Entry<String, Boolean> mapKey: map.getKey().entrySet()) {
                            if (!mapKey.getValue()) {
                                XSSFRow xssfRow = sheet.getRow(j);
                                String val = xssfRow.getCell(12).getStringCellValue();
                                if (val.equals(mapKey.getKey()) && !map.getValue().isEmpty()) {
                                    // Заполним суммы в заголовках дороги
                                    int cellNumberOne = j + 1;
                                    listRowTotalHeaders.add(cellNumberOne);
                                    int cellNumberStart = j + 2;
                                    int sizeCell = 0;
                                    for (Map.Entry<List<ResultClazz>, Integer> size: map.getValue().entrySet() ) {
                                        sizeCell = sizeCell + size.getKey().size();
                                    }
                                    int cellNumberFinish = cellNumberStart + sizeCell - 1;
                                    XSSFRow rowNewHeader = sheet.getRow(j);
                                    Cell cellHeader4 = rowNewHeader.createCell(4);
                                    cellHeader4.setCellFormula("SUM(E" + cellNumberStart + ":E" + cellNumberFinish + ")");
                                    cellHeader4.setCellStyle(cellStyleAlignmentCenterColorHeader(sheet));
                                    Cell cellHeader5 = rowNewHeader.createCell(5);
                                    cellHeader5.setCellFormula("SUM(F" + cellNumberStart + ":F" + cellNumberFinish + ")");
                                    cellHeader5.setCellStyle(cellStyleAlignmentCenterColorHeader(sheet));
                                    Cell cellHeader6 = rowNewHeader.createCell(6);
                                    cellHeader6.setCellFormula("SUM(G" + cellNumberStart + ":G" + cellNumberFinish + ")");
                                    cellHeader6.setCellStyle(cellStyleAlignmentCenterColorHeaderDoubleFormat(sheet));
                                    Cell cellHeader7 = rowNewHeader.createCell(7);
                                    cellHeader7.setCellFormula("F" + cellNumberOne + "/(E" + cellNumberOne + "/$M$4*$M$3)");
                                    cellHeader7.setCellStyle(cellStyleAlignmentCenterColorHeaderPercentFormat(sheet));
                                    Cell cellHeader8 = rowNewHeader.createCell(8);
                                    cellHeader8.setCellFormula("SUM(I" + cellNumberStart + ":I" + cellNumberFinish + ")");
                                    cellHeader8.setCellStyle(cellStyleAlignmentCenterColorHeader(sheet));
                                    Cell cellHeader10 = rowNewHeader.createCell(10);
                                    cellHeader10.setCellFormula("SUM(K" + cellNumberStart + ":K" + cellNumberFinish + ")");
                                    cellHeader10.setCellStyle(cellStyleAlignmentCenterColorHeader(sheet));
                                    Cell cellHeader11 = rowNewHeader.createCell(11);
                                    cellHeader11.setCellFormula("SUM(L" + cellNumberStart + ":L" + cellNumberFinish + ")");
                                    cellHeader11.setCellStyle(cellStyleAlignmentCenterLastCellHeader(sheet));
                                    // Вставляем запись
                                    int q = j;
                                    for (Map.Entry<List<ResultClazz>, Integer> resultValue : map.getValue().entrySet()) {
                                        if (resultValue.getValue() > 1) {
                                            // Флаги добавления в ячейку данных, если true, то для объединения ячеек значния повторные добавляться не будт, добавляем 0
                                            boolean isFlagCountLoading = false;
                                            boolean isFlagAverageStopAtStation = false;
                                            boolean isFlagCountDrive = false;
                                            boolean isFlagCountFact = false;
                                            boolean isFlagCountFactPercents = false;
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
                                                    cell1.setCellStyle(cellStyleAlignmentLeft(sheet));
                                                    Cell cell2 = rowNew.createCell(1);
                                                    cell2.setCellValue(resultClazz.getNameOfStationDeparture());
                                                    cell2.setCellStyle(cellStyleAlignmentLeft(sheet));
                                                    Cell cell3 = rowNew.createCell(2);
                                                    if (resultClazz.getCustomer() == null) {
                                                        boolean isOk = false;
                                                        for (String s : repairStations) {
                                                            if (s.equals(resultClazz.getNameOfStationDeparture())) {
                                                                cell3.setCellValue("РЕМОНТ");
                                                                cell3.setCellStyle(cellStyleForCustomerEmpty(sheet));
                                                                isOk = true;
                                                            }
                                                        }
                                                        if (!isOk) {
                                                            cell3.setCellValue("НЕТ ЗАЯВКИ");
                                                            cell3.setCellStyle(cellStyleForCustomerEmpty(sheet));
                                                        }
                                                    } else {
                                                        cell3.setCellValue(resultClazz.getCustomer());
                                                        cell3.setCellStyle(cellStyleAlignmentLeft(sheet));
                                                    }
                                                    Cell cell4 = rowNew.createCell(3);
                                                    cell4.setCellValue(resultClazz.getVolume());
                                                    cell4.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell5 = rowNew.createCell(4);
                                                    if (resultClazz.getCount() == 0) {
                                                        cell5.setCellStyle(cellStyleWhiteColor(sheet));
                                                    } else {
                                                        cell5.setCellValue(resultClazz.getCount());
                                                        cell5.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    }
                                                    Cell cell6 = rowNew.createCell(5);
                                                    if (resultClazz.getCountInDate() == 0) {
                                                        cell6.setCellStyle(cellStyleWhiteColor(sheet));
                                                    } else {
                                                        cell6.setCellValue(resultClazz.getCountInDate());
                                                        cell6.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    }
                                                    Cell cell7 = rowNew.createCell(6);
                                                    if (!isFlagCountFact) {
                                                        cell7.setCellFormula("SUM(F" + cellForFormula + ":F" + cellFixForFormula + ")-SUM(E" + cellForFormula + ":E" + cellFixForFormula + ")/$M$4*$M$3");
                                                        cell7.setCellStyle(cellStyleAlignmentCenterColorDoubleFormat(sheet));
                                                        isFlagCountFact = true;
                                                    } else {
                                                        cell7.setCellValue("");
                                                        cell7.setCellStyle(cellStyleAlignmentCenterColorDoubleFormat(sheet));
                                                    }
                                                    Cell cell8 = rowNew.createCell(7);
                                                    if (!isFlagCountFactPercents) {
                                                        cell8.setCellFormula("SUM(F" + cellForFormula + ":F" + cellFixForFormula + ")/(SUM(E" + cellForFormula + ":E" + cellFixForFormula + ")/$M$4*$M$3)");
                                                        cell8.setCellStyle(cellStyleAlignmentCenterColorPercentFormat(sheet));
                                                        isFlagCountFactPercents = true;
                                                    } else {
                                                        cell8.setCellValue("");
                                                        cell8.setCellStyle(cellStyleAlignmentCenterColorPercentFormat(sheet));
                                                    }
                                                    Cell cell9 = rowNew.createCell(8);
                                                    if (resultClazz.getCountLoading() == 0) {
                                                        cell9.setCellStyle(cellStyleWhiteColor(sheet));
                                                    } else {
                                                        if (!isFlagCountLoading) {
                                                            cell9.setCellValue(resultClazz.getCountLoading());
                                                            cell9.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                            isFlagCountLoading = true;
                                                        } else {
                                                            cell9.setCellValue(0);
                                                            cell9.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                        }
                                                    }
                                                    Cell cell10 = rowNew.createCell(9);
                                                    if (resultClazz.getAverageStopAtStation() == 0.00d) {
                                                        cell10.setCellStyle(cellStyleWhiteColor(sheet));
                                                    } else {
                                                        if (!isFlagAverageStopAtStation) {
                                                            cell10.setCellValue(resultClazz.getAverageStopAtStation());
                                                            cell10.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                            isFlagAverageStopAtStation = true;
                                                        } else {
                                                            cell10.setCellValue(0.00d);
                                                            cell10.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                        }
                                                    }
                                                    Cell cell11 = rowNew.createCell(10);
                                                    if (resultClazz.getCountDrive() == 0) {
                                                        cell11.setCellStyle(cellStyleWhiteColor(sheet));
                                                    } else {
                                                        if (!isFlagCountDrive) {
                                                            cell11.setCellValue(resultClazz.getCountDrive());
                                                            cell11.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                            isFlagCountDrive = true;
                                                        } else {
                                                            cell11.setCellValue(0);
                                                            cell11.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                        }
                                                    }
                                                    Cell cell12 = rowNew.createCell(11);
                                                    if (resultClazz.getCountInDateSpravo4no() == 0) {
                                                        cell12.setCellStyle(cellStyleWhiteFontLastCell(sheet));
                                                    } else {
                                                        cell12.setCellValue(resultClazz.getCountInDateSpravo4no());
                                                        cell12.setCellStyle(cellStyleAlignmentCenterLastCell(sheet));
                                                    }
                                                    Cell cell13 = rowNew.createCell(12);
                                                    cell13.setCellValue("");
                                                    q++;
                                                    resultClazz.setOk(true);
                                                }
                                            }
                                            // Объединяем ячейки
                                            sheet.addMergedRegion(new CellRangeAddress(q - resultValue.getValue() + 1, q,1, 1));
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
                                                    cell1.setCellStyle(cellStyleAlignmentLeft(sheet));
                                                    Cell cell2 = rowNew.createCell(1);
                                                    cell2.setCellValue(resultClazz.getNameOfStationDeparture());
                                                    cell2.setCellStyle(cellStyleAlignmentLeft(sheet));
                                                    Cell cell3 = rowNew.createCell(2);
                                                    if (resultClazz.getCustomer() == null) {
                                                        boolean isOk = false;
                                                        for (String s : repairStations) {
                                                            if (s.equals(resultClazz.getNameOfStationDeparture())) {
                                                                cell3.setCellValue("РЕМОНТ");
                                                                cell3.setCellStyle(cellStyleForCustomerEmpty(sheet));
                                                                isOk = true;
                                                            }
                                                        }
                                                        if (!isOk) {
                                                            cell3.setCellValue("НЕТ ЗАЯВКИ");
                                                            cell3.setCellStyle(cellStyleForCustomerEmpty(sheet));
                                                        }
                                                    } else {
                                                        cell3.setCellValue(resultClazz.getCustomer());
                                                        cell3.setCellStyle(cellStyleAlignmentLeft(sheet));
                                                    }
                                                    Cell cell4 = rowNew.createCell(3);
                                                    cell4.setCellValue(resultClazz.getVolume());
                                                    cell4.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    Cell cell5 = rowNew.createCell(4);
                                                    if (resultClazz.getCount() == 0) {
                                                        cell5.setCellStyle(cellStyleWhiteColor(sheet));
                                                    } else {
                                                        cell5.setCellValue(resultClazz.getCount());
                                                        cell5.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    }
                                                    Cell cell6 = rowNew.createCell(5);
                                                    if (resultClazz.getCountInDate() == 0) {
                                                        cell6.setCellStyle(cellStyleWhiteColor(sheet));
                                                    } else {
                                                        cell6.setCellValue(resultClazz.getCountInDate());
                                                        cell6.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    }
                                                    Cell cell7 = rowNew.createCell(6);
                                                    cell7.setCellFormula("F" + cellForFormula + "-E" + cellForFormula + "/$M$4*$M$3");
                                                    cell7.setCellStyle(cellStyleAlignmentCenterColorDoubleFormat(sheet));
                                                    Cell cell8 = rowNew.createCell(7);
                                                    cell8.setCellFormula("F" + cellForFormula + "/(E" + cellForFormula + "/$M$4*$M$3)");
                                                    cell8.setCellStyle(cellStyleAlignmentCenterColorPercentFormat(sheet));
                                                    Cell cell9 = rowNew.createCell(8);
                                                    if (resultClazz.getCountLoading() == 0) {
                                                        cell9.setCellStyle(cellStyleWhiteColor(sheet));
                                                    } else {
                                                        cell9.setCellValue(resultClazz.getCountLoading());
                                                        cell9.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    }
                                                    Cell cell10 = rowNew.createCell(9);
                                                    if (resultClazz.getAverageStopAtStation() == 0.00d) {
                                                        cell10.setCellStyle(cellStyleWhiteColor(sheet));
                                                    } else {
                                                        cell10.setCellValue(resultClazz.getAverageStopAtStation());
                                                        cell10.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    }
                                                    Cell cell11 = rowNew.createCell(10);
                                                    if (resultClazz.getCountDrive() == 0) {
                                                        cell11.setCellStyle(cellStyleWhiteColor(sheet));
                                                    } else {
                                                        cell11.setCellValue(resultClazz.getCountDrive());
                                                        cell11.setCellStyle(cellStyleAlignmentCenter(sheet));
                                                    }
                                                    Cell cell12 = rowNew.createCell(11);
                                                    if (resultClazz.getCountInDateSpravo4no() == 0) {
                                                        cell12.setCellStyle(cellStyleWhiteFontLastCell(sheet));
                                                    } else {
                                                        cell12.setCellValue(resultClazz.getCountInDateSpravo4no());
                                                        cell12.setCellStyle(cellStyleAlignmentCenterLastCell(sheet));
                                                    }
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

                XSSFRow rowNewDate = sheet.getRow(1);
                Cell cellDate = rowNewDate.createCell(11);
                cellDate.setCellValue(getArrayDates().get(0).toString());
                cellDate.setCellStyle(cellStyleBorder(sheet));

                XSSFRow rowNewPrevDay = sheet.getRow(2);
                Cell cellPrevDay = rowNewPrevDay.createCell(12);
                cellPrevDay.setCellValue((Integer) getArrayDates().get(1));

                XSSFRow rowNewLastDay = sheet.getRow(3);
                Cell cellLastDay = rowNewLastDay.createCell(12);
                cellLastDay.setCellValue((Integer) getArrayDates().get(2));

                XSSFRow rowNewHeadSum = sheet.getRow(4);
                Cell cellHeaderSum4 = rowNewHeadSum.createCell(4);
                cellHeaderSum4.setCellFormula(fillFormulaForHeadersTotal("E", listRowTotalHeaders));
                cellHeaderSum4.setCellStyle(cellStyleAlignmentCenterColorHeadTopBoarder(sheet));
                Cell cellHeaderSum5 = rowNewHeadSum.createCell(5);
                cellHeaderSum5.setCellFormula(fillFormulaForHeadersTotal("F", listRowTotalHeaders));
                cellHeaderSum5.setCellStyle(cellStyleAlignmentCenterColorHeadTopBoarder(sheet));
                Cell cellHeaderSum6 = rowNewHeadSum.createCell(6);
                cellHeaderSum6.setCellFormula(fillFormulaForHeadersTotal("G", listRowTotalHeaders));
                cellHeaderSum6.setCellStyle(cellStyleAlignmentCenterColorHeadTopBoarderDoubleFormat(sheet));
                Cell cellHeaderSum7 = rowNewHeadSum.createCell(7);
                cellHeaderSum7.setCellFormula("F5/(E5/$M$4*$M$3)");
                cellHeaderSum7.setCellStyle(cellStyleAlignmentCenterColorHeadTopBoarderTypePercentFormat(sheet));
                Cell cellHeaderSum8 = rowNewHeadSum.createCell(8);
                cellHeaderSum8.setCellFormula(fillFormulaForHeadersTotal("I", listRowTotalHeaders));
                cellHeaderSum8.setCellStyle(cellStyleAlignmentCenterColorHeadTopBoarder(sheet));
                Cell cellHeaderSum10 = rowNewHeadSum.createCell(10);
                cellHeaderSum10.setCellFormula(fillFormulaForHeadersTotal("K", listRowTotalHeaders));
                cellHeaderSum10.setCellStyle(cellStyleAlignmentCenterColorHeadTopBoarder(sheet));
                Cell cellHeaderSum11 = rowNewHeadSum.createCell(11);
                cellHeaderSum11.setCellFormula(fillFormulaForHeadersTotal("L", listRowTotalHeaders));
                cellHeaderSum11.setCellStyle(cellStyleAlignmentCenterColorHeadTopAndRightBoarder(sheet));

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
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.DOTTED);
        cellStyle.setBorderTop(BorderStyle.DOTTED);
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleBorder(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleAlignmentCenterLastCell(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.DOTTED);
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleWhiteFontLastCell(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setColor(HSSFColor.WHITE.index);
        cellStyle.setFont(font);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.DOTTED);
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleWhiteColor(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setColor(HSSFColor.WHITE.index);
        cellStyle.setFont(font);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.DOTTED);
        cellStyle.setBorderTop(BorderStyle.DOTTED);
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleAlignmentCenterColor(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.DOTTED);
        cellStyle.setBorderTop(BorderStyle.DOTTED);
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(216, 228, 188)));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleAlignmentCenterColorDoubleFormat(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.DOTTED);
        cellStyle.setBorderTop(BorderStyle.DOTTED);
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(216, 228, 188)));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFDataFormat dataFormat = sheet.getWorkbook().createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("0.00"));
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleAlignmentCenterColorPercentFormat(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.DOTTED);
        cellStyle.setBorderTop(BorderStyle.DOTTED);
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(216, 228, 188)));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFDataFormat dataFormat = sheet.getWorkbook().createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("0%"));
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleAlignmentCenterColorHeadTopBoarder(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.DOTTED);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(191, 191, 191)));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleAlignmentCenterColorHeadTopBoarderDoubleFormat(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.DOTTED);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(191, 191, 191)));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFDataFormat dataFormat = sheet.getWorkbook().createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("0.00"));
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleAlignmentCenterColorHeadTopBoarderTypePercentFormat(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.DOTTED);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(191, 191, 191)));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFDataFormat dataFormat = sheet.getWorkbook().createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("0%"));
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleAlignmentCenterColorHeadTopAndRightBoarder(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(191, 191, 191)));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleAlignmentCenterColorHeader(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.DOTTED);
        cellStyle.setBorderTop(BorderStyle.DOTTED);
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 0)));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleAlignmentCenterColorHeaderDoubleFormat(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.DOTTED);
        cellStyle.setBorderTop(BorderStyle.DOTTED);
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 0)));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFDataFormat dataFormat = sheet.getWorkbook().createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("0.00"));
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleAlignmentCenterColorHeaderPercentFormat(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.DOTTED);
        cellStyle.setBorderTop(BorderStyle.DOTTED);
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 0)));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFDataFormat dataFormat = sheet.getWorkbook().createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("0%"));
        return cellStyle;
    }

    private static XSSFCellStyle cellStyleAlignmentCenterLastCellHeader(XSSFSheet sheet) {
        XSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.DOTTED);
        cellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 0)));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
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
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.DOTTED);
        cellStyle.setBorderTop(BorderStyle.DOTTED);
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

    private List<Object> getArrayDates() {
        List<Object> listDates = new ArrayList<>();
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        Calendar calendar11 = Calendar.getInstance();

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        calendar11.add(Calendar.DATE, -1);

        Date dayMinOneDay = calendar11.getTime();
        Date lastDayOfMonth = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        listDates.add(simpleDateFormat.format(today));
        listDates.add(Integer.parseInt(sdf.format(dayMinOneDay)));
        listDates.add(Integer.parseInt(sdf.format(lastDayOfMonth)));
        return listDates;
    }

    private String fillFormulaForHeadersTotal(String cellNameLiteral, TreeSet<Integer> listRow) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cellNameLiteral);
        for (Integer numberRow: listRow) {
            stringBuilder.append(numberRow).append("+").append(cellNameLiteral);
        }
        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
        return stringBuilder.toString();
    }
}
