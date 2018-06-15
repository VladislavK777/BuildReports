package com.uraltranscom.buildreports.model;

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
    private String nameOfStationDeparture; // Станция отправления
    private String nameRoadOfStationDeparture; // Дорга станции отправления
    private String nameOfStationDestination; // Станция назначения
    private String nameRoadStationDestination; // Дорога станции назначения
    private String customer; // Заказчик
    private String cargo; // Груз
    private String distance; // Дистанция
    private double rate; // Ставка

    private int count; // Количество рейсов одинаковых
    private int averageDistance; // Среднее расстояние
    private double averageRate; // Средняя ставка

    private boolean isOk = false;

    public Wagon(String nameOfStationDeparture, String nameRoadOfStationDeparture, String nameOfStationDestination, String nameRoadStationDestination, String customer, String cargo, String distance, double rate) {
        this.nameOfStationDeparture = nameOfStationDeparture;
        this.nameRoadOfStationDeparture = nameRoadOfStationDeparture;
        this.nameOfStationDestination = nameOfStationDestination;
        this.nameRoadStationDestination = nameRoadStationDestination;
        this.customer = customer;
        this.cargo = cargo;
        this.distance = distance;
        this.rate = rate;
    }

    public Wagon(String nameOfStationDeparture, String nameRoadOfStationDeparture, String nameOfStationDestination, String nameRoadStationDestination, String customer, String cargo, int count, int averageDistance, double averageRate) {
        this.nameOfStationDeparture = nameOfStationDeparture;
        this.nameRoadOfStationDeparture = nameRoadOfStationDeparture;
        this.nameOfStationDestination = nameOfStationDestination;
        this.nameRoadStationDestination = nameRoadStationDestination;
        this.customer = customer;
        this.cargo = cargo;
        this.count = count;
        this.averageDistance = averageDistance;
        this.averageRate = averageRate;
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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAverageDistance() {
        return averageDistance;
    }

    public void setAverageDistance(int averageDistance) {
        this.averageDistance = averageDistance;
    }

    public double getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(double averageRate) {
        this.averageRate = averageRate;
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
        return Double.compare(wagon.rate, rate) == 0 &&
                count == wagon.count &&
                averageDistance == wagon.averageDistance &&
                Double.compare(wagon.averageRate, averageRate) == 0 &&
                Objects.equals(nameOfStationDeparture, wagon.nameOfStationDeparture) &&
                Objects.equals(nameRoadOfStationDeparture, wagon.nameRoadOfStationDeparture) &&
                Objects.equals(nameOfStationDestination, wagon.nameOfStationDestination) &&
                Objects.equals(nameRoadStationDestination, wagon.nameRoadStationDestination) &&
                Objects.equals(customer, wagon.customer) &&
                Objects.equals(cargo, wagon.cargo) &&
                Objects.equals(distance, wagon.distance);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nameOfStationDeparture, nameRoadOfStationDeparture, nameOfStationDestination, nameRoadStationDestination, customer, cargo, distance, rate, count, averageDistance, averageRate);
    }

    @Override
    public String toString() {
        return "Wagon{" +
                "nameOfStationDeparture='" + nameOfStationDeparture + '\'' +
                ", nameRoadOfStationDeparture='" + nameRoadOfStationDeparture + '\'' +
                ", nameOfStationDestination='" + nameOfStationDestination + '\'' +
                ", nameRoadStationDestination='" + nameRoadStationDestination + '\'' +
                ", customer='" + customer + '\'' +
                ", cargo='" + cargo + '\'' +
                ", distance='" + distance + '\'' +
                ", rate=" + rate +
                ", count=" + count +
                ", averageDistance=" + averageDistance +
                ", averageRate=" + averageRate +
                ", isOk=" + isOk +
                '}';
    }
}
