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
    private String nameOfStationDeparture; // Станция отправления
    private String nameRoadOfStationDeparture; // Дорга станции отправления
    private int volume; // Объем вагона
    private Date dateToDeparted; // Дата отправления
    private String condition; // Состояние вагона Под погрузку
    private double stopAtStation; // Простой на станции дислокации
    private String emptyOrFull; // Состояние вагона Порожний/Груженный
    private String customer; // Заказчик

    private String volumeTotal; // Строка объединения объемов вагона
    private int count; // Количество вагонов на станции
    private int countLoading; // Под погрузкой, ед.
    private int countDrive; // Подход (в движении), ед.
    private int countInDate; // Количесвто на дату
    private int countInDateSpravo4no; // Количество на дату для Справочно
    private double avarageStopAtStation; // Средний простой под погр, сут.

    private boolean isOk = false; // Признак добавления

    public Wagon(String nameOfStationDestination, String nameRoadStationDestination, String nameOfStationDeparture, String nameRoadOfStationDeparture, int volume, Date dateToDeparted, String condition, double stopAtStation, String emptyOrFull, String customer) {
        this.nameOfStationDestination = nameOfStationDestination;
        this.nameRoadStationDestination = nameRoadStationDestination;
        this.nameOfStationDeparture = nameOfStationDeparture;
        this.nameRoadOfStationDeparture = nameRoadOfStationDeparture;
        this.volume = volume;
        this.dateToDeparted = dateToDeparted;
        this.condition = condition;
        this.stopAtStation = stopAtStation;
        this.emptyOrFull = emptyOrFull;
        this.customer = customer;
    }

    public Wagon(String nameOfStationDestination, String nameRoadStationDestination, String nameOfStationDeparture, String nameRoadOfStationDeparture, String volumeTotal, int count, int countLoading, int countDrive, int countInDate, int countInDateSpravo4no, double avarageStopAtStation, String customer) {
        this.nameOfStationDestination = nameOfStationDestination;
        this.nameRoadStationDestination = nameRoadStationDestination;
        this.nameOfStationDeparture = nameOfStationDeparture;
        this.nameRoadOfStationDeparture = nameRoadOfStationDeparture;
        this.volumeTotal = volumeTotal;
        this.count = count;
        this.countLoading = countLoading;
        this.countDrive = countDrive;
        this.countInDate = countInDate;
        this.countInDateSpravo4no = countInDateSpravo4no;
        this.avarageStopAtStation = avarageStopAtStation;
        this.customer = customer;
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

    public String getNameOfStationDeparture() {
        return nameOfStationDeparture;
    }

    public void setNameOfStationDeparture(String nameOfStationDeparture) {
        this.nameOfStationDeparture = nameOfStationDeparture;
    }

    public String getNameRoadOfStationDeparture() {
        return nameRoadOfStationDeparture;
    }

    public void setNameRoadOfStationDeparture(String nameRoadOfStationDeparture) {
        this.nameRoadOfStationDeparture = nameRoadOfStationDeparture;
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

    public String getEmptyOrFull() {
        return emptyOrFull;
    }

    public void setEmptyOrFull(String emptyOrFull) {
        this.emptyOrFull = emptyOrFull;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getVolumeTotal() {
        return volumeTotal;
    }

    public void setVolumeTotal(String volumeTotal) {
        this.volumeTotal = volumeTotal;
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

    public int getCountInDateSpravo4no() {
        return countInDateSpravo4no;
    }

    public void setCountInDateSpravo4no(int countInDateSpravo4no) {
        this.countInDateSpravo4no = countInDateSpravo4no;
    }

    public double getAvarageStopAtStation() {
        return avarageStopAtStation;
    }

    public void setAvarageStopAtStation(double avarageStopAtStation) {
        this.avarageStopAtStation = avarageStopAtStation;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
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
                countInDateSpravo4no == wagon.countInDateSpravo4no &&
                Double.compare(wagon.avarageStopAtStation, avarageStopAtStation) == 0 &&
                isOk == wagon.isOk &&
                Objects.equals(nameOfStationDestination, wagon.nameOfStationDestination) &&
                Objects.equals(nameRoadStationDestination, wagon.nameRoadStationDestination) &&
                Objects.equals(nameOfStationDeparture, wagon.nameOfStationDeparture) &&
                Objects.equals(nameRoadOfStationDeparture, wagon.nameRoadOfStationDeparture) &&
                Objects.equals(dateToDeparted, wagon.dateToDeparted) &&
                Objects.equals(condition, wagon.condition) &&
                Objects.equals(emptyOrFull, wagon.emptyOrFull) &&
                Objects.equals(customer, wagon.customer) &&
                Objects.equals(volumeTotal, wagon.volumeTotal);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nameOfStationDestination, nameRoadStationDestination, nameOfStationDeparture, nameRoadOfStationDeparture, volume, dateToDeparted, condition, stopAtStation, emptyOrFull, customer, volumeTotal, count, countLoading, countDrive, countInDate, countInDateSpravo4no, avarageStopAtStation, isOk);
    }

    @Override
    public String toString() {
        return "Wagon{" +
                "nameOfStationDestination='" + nameOfStationDestination + '\'' +
                ", nameRoadStationDestination='" + nameRoadStationDestination + '\'' +
                ", nameOfStationDeparture='" + nameOfStationDeparture + '\'' +
                ", nameRoadOfStationDeparture='" + nameRoadOfStationDeparture + '\'' +
                ", volume=" + volume +
                ", dateToDeparted=" + dateToDeparted +
                ", condition='" + condition + '\'' +
                ", stopAtStation=" + stopAtStation +
                ", emptyOrFull='" + emptyOrFull + '\'' +
                ", customer='" + customer + '\'' +
                ", volumeTotal='" + volumeTotal + '\'' +
                ", count=" + count +
                ", countLoading=" + countLoading +
                ", countDrive=" + countDrive +
                ", countInDate=" + countInDate +
                ", countInDateSpravo4no=" + countInDateSpravo4no +
                ", avarageStopAtStation=" + avarageStopAtStation +
                ", isOk=" + isOk +
                '}';
    }
}
