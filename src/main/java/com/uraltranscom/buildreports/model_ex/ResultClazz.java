package com.uraltranscom.buildreports.model_ex;

import java.util.Objects;

/**
 *
 * Класс результат
 *
 * @author Vladislav Klochkov
 * @version 1.0
 * @create 13.06.2018
 *
 * 13.06.2018
 *   1. Версия 1.0
 *
 */

public class ResultClazz {
    private String nameOfStationDeparture; // Станция отправления
    private String nameRoadOfStationDeparture; // Дорга станции отправления
    private String customer; // Заказчик
    private int volume; // Объем
    private int count; // Количество заявок
    private int countLoading; // Под погрузкой, ед.
    private int countDrive; // Подход (в движении), ед.
    private int countInDate; // Количесвто на дату
    private double avarageStopAtStation; // Средний простой под погр, сут.
    private boolean isOk = false; // Был ли добавлен в файл

    public ResultClazz(String nameOfStationDeparture, String nameRoadOfStationDeparture, String customer, int volume, int count, int countLoading, int countDrive, int countInDate, double avarageStopAtStation) {
        this.nameOfStationDeparture = nameOfStationDeparture;
        this.nameRoadOfStationDeparture = nameRoadOfStationDeparture;
        this.customer = customer;
        this.volume = volume;
        this.count = count;
        this.countLoading = countLoading;
        this.countDrive = countDrive;
        this.countInDate = countInDate;
        this.avarageStopAtStation = avarageStopAtStation;
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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
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
        ResultClazz that = (ResultClazz) o;
        return volume == that.volume &&
                count == that.count &&
                countLoading == that.countLoading &&
                countDrive == that.countDrive &&
                countInDate == that.countInDate &&
                Double.compare(that.avarageStopAtStation, avarageStopAtStation) == 0 &&
                isOk == that.isOk &&
                Objects.equals(nameOfStationDeparture, that.nameOfStationDeparture) &&
                Objects.equals(nameRoadOfStationDeparture, that.nameRoadOfStationDeparture) &&
                Objects.equals(customer, that.customer);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nameOfStationDeparture, nameRoadOfStationDeparture, customer, volume, count, countLoading, countDrive, countInDate, avarageStopAtStation, isOk);
    }

    @Override
    public String toString() {
        return "ResultClazz{" +
                "nameOfStationDeparture='" + nameOfStationDeparture + '\'' +
                ", nameRoadOfStationDeparture='" + nameRoadOfStationDeparture + '\'' +
                ", customer='" + customer + '\'' +
                ", volume=" + volume +
                ", count=" + count +
                ", countLoading=" + countLoading +
                ", countDrive=" + countDrive +
                ", countInDate=" + countInDate +
                ", avarageStopAtStation=" + avarageStopAtStation +
                ", isOk=" + isOk +
                '}';
    }
}
