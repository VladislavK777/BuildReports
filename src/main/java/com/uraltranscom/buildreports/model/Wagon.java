package com.uraltranscom.buildreports.model;

import java.util.Date;
import java.util.Objects;

/**
 *
 * Класс Вагон
 *
 * @author Vladislav Klochkov
 * @version 1.0
 * @create 13.06.2018
 *
 * 13.06.2018
 *   1. Версия 1.0
 *
 */

public class Wagon {
    private String nameOfStationDestination; // Станция назначения
    private String nameRoadStationDestination; // Дорога станции назначения
    private int volume; // Объем вагона
    private Date dateToDeparted; // Дата отправления
    private String condition; // Состояние вагона Под погрузку
    private double stopAtStation; // Простой на станции дислокации
    private String emptyOrFull; // Состояние вагона Порожний/Груженный

    private int count; // Количество вагонов на станции
    private int countLoading; // Под погрузкой, ед.
    private int countDrive; // Подход (в движении), ед.
    private int countInDate; // Количесвто на дату
    private double avarageStopAtStation; // Средний простой под погр, сут.

    public Wagon(String nameOfStationDestination, String nameRoadStationDestination, int volume, Date dateToDeparted, String condition, double stopAtStation, String emptyOrFull) {
        this.nameOfStationDestination = nameOfStationDestination;
        this.nameRoadStationDestination = nameRoadStationDestination;
        this.volume = volume;
        this.dateToDeparted = dateToDeparted;
        this.condition = condition;
        this.stopAtStation = stopAtStation;
        this.emptyOrFull = emptyOrFull;
    }

    public Wagon(String nameOfStationDestination, String nameRoadStationDestination, int volume, int count, int countLoading, int countDrive, int countInDate, double avarageStopAtStation) {
        this.nameOfStationDestination = nameOfStationDestination;
        this.nameRoadStationDestination = nameRoadStationDestination;
        this.volume = volume;
        this.count = count;
        this.countLoading = countLoading;
        this.countDrive = countDrive;
        this.countInDate = countInDate;
        this.avarageStopAtStation = avarageStopAtStation;
    }

    public String getNameOfStationDestination() {
        return nameOfStationDestination;
    }

    public void setNameOfStationDestination(String nameOfStationDestination) {
        this.nameOfStationDestination = nameOfStationDestination;
    }

    public String getNameRoadStationDestination() {
        return nameRoadStationDestination;
    }

    public void setNameRoadStationDestination(String nameRoadStationDestination) {
        this.nameRoadStationDestination = nameRoadStationDestination;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Date getDateToDeparted() {
        return dateToDeparted;
    }

    public void setDateToDeparted(Date dateToDeparted) {
        this.dateToDeparted = dateToDeparted;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getStopAtStation() {
        return stopAtStation;
    }

    public void setStopAtStation(double stopAtStation) {
        this.stopAtStation = stopAtStation;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCountLoading() {
        return countLoading;
    }

    public void setCountLoading(int countLoading) {
        this.countLoading = countLoading;
    }

    public int getCountDrive() {
        return countDrive;
    }

    public void setCountDrive(int countDrive) {
        this.countDrive = countDrive;
    }

    public int getCountInDate() {
        return countInDate;
    }

    public void setCountInDate(int countInDate) {
        this.countInDate = countInDate;
    }

    public double getAvarageStopAtStation() {
        return avarageStopAtStation;
    }

    public void setAvarageStopAtStation(double avarageStopAtStation) {
        this.avarageStopAtStation = avarageStopAtStation;
    }

    public String getEmptyOrFull() {
        return emptyOrFull;
    }

    public void setEmptyOrFull(String emptyOrFull) {
        this.emptyOrFull = emptyOrFull;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wagon wagon = (Wagon) o;
        return volume == wagon.volume &&
                Double.compare(wagon.stopAtStation, stopAtStation) == 0 &&
                count == wagon.count &&
                countLoading == wagon.countLoading &&
                countDrive == wagon.countDrive &&
                countInDate == wagon.countInDate &&
                Double.compare(wagon.avarageStopAtStation, avarageStopAtStation) == 0 &&
                Objects.equals(nameOfStationDestination, wagon.nameOfStationDestination) &&
                Objects.equals(nameRoadStationDestination, wagon.nameRoadStationDestination) &&
                Objects.equals(dateToDeparted, wagon.dateToDeparted) &&
                Objects.equals(condition, wagon.condition) &&
                Objects.equals(emptyOrFull, wagon.emptyOrFull);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nameOfStationDestination, nameRoadStationDestination, volume, dateToDeparted, condition, stopAtStation, emptyOrFull, count, countLoading, countDrive, countInDate, avarageStopAtStation);
    }

    @Override
    public String toString() {
        return "Wagon{" +
                "nameOfStationDestination='" + nameOfStationDestination + '\'' +
                ", nameRoadStationDestination='" + nameRoadStationDestination + '\'' +
                ", volume=" + volume +
                ", dateToDeparted=" + dateToDeparted +
                ", condition='" + condition + '\'' +
                ", stopAtStation=" + stopAtStation +
                ", emptyOrFull='" + emptyOrFull + '\'' +
                ", count=" + count +
                ", countLoading=" + countLoading +
                ", countDrive=" + countDrive +
                ", countInDate=" + countInDate +
                ", avarageStopAtStation=" + avarageStopAtStation +
                '}';
    }
}
