package com.uraltranscom.buildreports.service.impl;

import com.uraltranscom.buildreports.model.Route;
import com.uraltranscom.buildreports.model.Wagon;
import com.uraltranscom.buildreports.model_ex.ResultClazz;
import com.uraltranscom.buildreports.service.additional.ComparatorMap;
import com.uraltranscom.buildreports.service.export.WriteToFileExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Класс корень
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
public class RootClazz {
    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(RootClazz.class);

    @Autowired
    GetListOfWagonsImpl getListOfWagons;
    @Autowired
    GetListOfRoutesImpl getListOfRoutes;
    @Autowired
    WriteToFileExcel writeToFileExcel;

    private RootClazz() {
    }

    public void startProcess(ArrayList<Date> dates, ArrayList<Date> datesSpravo4no, HttpServletResponse response) {
        Map<Map<String, Boolean>, Map<List<ResultClazz>, Integer>> totalMap = new HashMap<>();
        Map<Integer, ResultClazz> mapResult = new ConcurrentHashMap<>();
        getListOfWagons.setDates(dates);
        getGetListOfWagons().setDatesSpravo4no(datesSpravo4no);
        List<Wagon> wagonList = getListOfWagons.getListCountWagon(getListOfWagons.getListOfWagonsEmpty(), getListOfWagons.getListOfWagonsFull());
        Map<Integer, Route> routeMap = getListOfRoutes.getMapOfRoutes();
        String [] roads = {"АРМ", "ГРЗ", "МНГ", "МЛД", "КРГ", "ТРК", "ТДЖ", "УТИ", "КЗХ", "ЮЗП", "ЮЖН", "ПДН", "ОДС", "ЛЬВ", "АЗР", "ЭСТ", "ЛИТ", "ЛАТ", "БЕЛ", "ЖДЯ", "САХ", "Крым", "ДВС", "ЗАБ", "ВСБ", "КРС", "ЗСБ", "ЮУР", "СВР", "КБШ", "ПРВ", "ЮВС", "СКВ", "СЕВ", "ГОР", "МСК", "ОКТ", "КЛГ", "ДОН"};

        int i = 0;
        for (Map.Entry<Integer, Route> route : routeMap.entrySet()) {
            for (Wagon wagon : wagonList) {
                if (route.getValue().getNameOfStationDeparture().equals(wagon.getNameOfStationDestination())) {
                    mapResult.put(i, new ResultClazz(
                            route.getValue().getNameOfStationDeparture(),
                            route.getValue().getNameRoadOfStationDeparture(),
                            route.getValue().getCustomer(),
                            route.getValue().getVolumeTotal(),
                            route.getValue().getCountOrder(),
                            wagon.getCountLoading(),
                            wagon.getCountDrive(),
                            wagon.getCountInDate(),
                            wagon.getCountInDateSpravo4no(),
                            wagon.getAvarageStopAtStation()));
                    wagon.setOk(true);
                    route.getValue().setOk(true);
                    i++;
                    for (Wagon wagon1: wagonList) {
                        if (route.getValue().getNameOfStationDeparture().equals(wagon1.getNameOfStationDeparture()) &&
                                route.getValue().getCustomer().equals(wagon1.getCustomer())) {
                            Iterator<Map.Entry<Integer, ResultClazz>> iterator = mapResult.entrySet().iterator();
                            while (iterator.hasNext()) {
                                Map.Entry<Integer, ResultClazz> map = iterator.next();
                                if (route.getValue().getNameOfStationDeparture().
                                        equals(map.getValue().getNameOfStationDeparture()) &&
                                        map.getValue().getCustomer().equals(route.getValue().getCustomer())) {
                                    map.getValue().setCountInDate(wagon1.getCountInDate());
                                    map.getValue().setCountInDateSpravo4no(wagon1.getCountInDateSpravo4no());
                                    map.getValue().setVolume(mergeVolume(wagon1.getVolumeTotal(), map.getValue().getVolume()));
                                    wagon1.setOk(true);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (route.getValue().getNameOfStationDeparture().
                        equals(wagon.getNameOfStationDeparture()) &&
                        route.getValue().getCustomer().equals(wagon.getCustomer()) &&
                        !wagon.isOk()) {
                    mapResult.put(i, new ResultClazz(
                            route.getValue().getNameOfStationDeparture(),
                            route.getValue().getNameRoadOfStationDeparture(),
                            route.getValue().getCustomer(),
                            mergeVolume(wagon.getVolumeTotal(), route.getValue().getVolumeTotal()),
                            route.getValue().getCountOrder(),
                            wagon.getCountLoading(),
                            wagon.getCountDrive(),
                            wagon.getCountInDate(),
                            wagon.getCountInDateSpravo4no(),
                            wagon.getAvarageStopAtStation()));
                    wagon.setOk(true);
                    route.getValue().setOk(true);
                    i++;
                    break;
                }

            }
        }
        for (Map.Entry<Integer, Route> route : routeMap.entrySet()) {
            if (!route.getValue().isOk()){
                mapResult.put(i, new ResultClazz(
                        route.getValue().getNameOfStationDeparture(),
                        route.getValue().getNameRoadOfStationDeparture(),
                        route.getValue().getCustomer(),
                        route.getValue().getVolumeTotal(),
                        route.getValue().getCountOrder(),
                        0,
                        0,
                        0,
                        0,
                        0));
                route.getValue().setOk(true);
                i++;
            }
        }
        for (Wagon wagon : wagonList) {
            if (!wagon.isOk() && wagon.getCountInDate() == 0) {
                mapResult.put(i, new ResultClazz(
                        wagon.getNameOfStationDestination(),
                        wagon.getNameRoadStationDestination(),
                        null,
                        wagon.getVolumeTotal(),
                        0,
                        wagon.getCountLoading(),
                        wagon.getCountDrive(),
                        wagon.getCountInDate(),
                        wagon.getCountInDateSpravo4no(),
                        wagon.getAvarageStopAtStation()));
                wagon.setOk(true);
                i++;
                for (Wagon wagon1: wagonList) {
                    if (wagon.getNameOfStationDestination().equals(wagon1.getNameOfStationDeparture()) && wagon1.getCountInDate() > 0) {
                        Iterator<Map.Entry<Integer, ResultClazz>> iterator = mapResult.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<Integer, ResultClazz> map = iterator.next();
                            if (wagon1.getNameOfStationDeparture().equals(map.getValue().getNameOfStationDeparture())) {
                                map.getValue().setCountInDate(map.getValue().getCountInDate() + wagon1.getCountInDate());
                                map.getValue().setCountInDateSpravo4no(map.getValue().getCountInDateSpravo4no() + wagon1.getCountInDateSpravo4no());
                                map.getValue().setVolume(mergeVolume(wagon1.getVolumeTotal(), map.getValue().getVolume()));
                                wagon1.setOk(true);
                                break;
                            }
                        }
                    }
                }
            } else if (!wagon.isOk() && wagon.getCountInDate() > 0) {
                mapResult.put(i, new ResultClazz(
                        wagon.getNameOfStationDeparture(),
                        wagon.getNameRoadOfStationDeparture(),
                        wagon.getCustomer(),
                        wagon.getVolumeTotal(),
                        0,
                        wagon.getCountLoading(),
                        wagon.getCountDrive(),
                        wagon.getCountInDate(),
                        wagon.getCountInDateSpravo4no(),
                        wagon.getAvarageStopAtStation()));
                wagon.setOk(true);
                i++;
            }
        }

        Map<Map<String, Boolean>, List<ResultClazz>> result = new HashMap<>();
        for (String road: roads) {
            List<ResultClazz> resultClazzes = new ArrayList<>();
            Map<String, Boolean> mapKey = new HashMap<>();
            for (Map.Entry<Integer, ResultClazz> map: mapResult.entrySet()) {
                if (map.getValue().getNameRoadOfStationDeparture().equals(road)) {
                    resultClazzes.add(map.getValue());
                }
            }
            mapKey.put(road, false);
            result.put(mapKey, resultClazzes);
        }

        for (Map.Entry<Map<String, Boolean>, List<ResultClazz>> _result : result.entrySet()) {
            Map<List<ResultClazz>, Integer> newResultMap = new TreeMap<>(new ComparatorMap());
            for (ResultClazz resultClazz : _result.getValue()) {
                int count = 0;
                List<ResultClazz> newResultList = new ArrayList<>();
                for (ResultClazz _resultClazz : _result.getValue()) {
                    if (resultClazz.getNameOfStationDeparture().equals(_resultClazz.getNameOfStationDeparture())) {
                        count++;
                        newResultList.add(_resultClazz);
                    }
                }
                newResultMap.put(newResultList, count);
            }

            // Добавляем нашу мапу сортированную в Linked для удаление дублей
            Map<List<ResultClazz>, Integer> newResultMapLinked = new LinkedHashMap<>(newResultMap);
            totalMap.put(_result.getKey(), newResultMapLinked);
        }
        logger.debug("map {}", totalMap);
        writeToFileExcel.downloadFileExcel(response, totalMap);
    }

    private String mergeVolume(String volume1, String volume2) {
        StringBuilder stringVolume = new StringBuilder();
        TreeSet<Integer> listVolume = new TreeSet<>();
        String [] stringsVolume1 = volume1.split("/");
        String [] stringsVolume2 = volume2.split("/");
        for (String sv1: stringsVolume1) {
            listVolume.add(Integer.parseInt(sv1));
        }
        for (String sv2: stringsVolume2) {
            listVolume.add(Integer.parseInt(sv2));
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
        return stringVolume.toString();
    }

    public GetListOfWagonsImpl getGetListOfWagons() {
        return getListOfWagons;
    }

    public void setGetListOfWagons(GetListOfWagonsImpl getListOfWagons) {
        this.getListOfWagons = getListOfWagons;
    }

    public GetListOfRoutesImpl getGetListOfRoutes() {
        return getListOfRoutes;
    }

    public void setGetListOfRoutes(GetListOfRoutesImpl getListOfRoutes) {
        this.getListOfRoutes = getListOfRoutes;
    }

    public WriteToFileExcel getWriteToFileExcel() {
        return writeToFileExcel;
    }

    public void setWriteToFileExcel(WriteToFileExcel writeToFileExcel) {
        this.writeToFileExcel = writeToFileExcel;
    }
}
