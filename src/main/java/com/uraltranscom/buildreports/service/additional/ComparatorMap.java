package com.uraltranscom.buildreports.service.additional;

import com.uraltranscom.buildreports.model_ex.ResultClazz;

import java.util.Comparator;
import java.util.List;

/**
 *
 * Класс компаратора для сортировки по станции отправления
 *
 * @author Vladislav Klochkov
 * @version 1.0
 * @create 28.06.2018
 *
 * 28.06.2018
 *   1. Версия 1.0
 *
 */

public class ComparatorMap implements Comparator<List<ResultClazz>> {

    @Override
    public int compare(List<ResultClazz> o1, List<ResultClazz> o2) {
        for (ResultClazz resultClazz: o1) {
            for (ResultClazz _resultClazz: o2) {
                if (resultClazz.getNameOfStationDeparture().compareTo(_resultClazz.getNameOfStationDeparture()) >= 0) {
                    return 1;
                }
            }
        }
        return -1;
    }
}

