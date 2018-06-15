package com.uraltranscom.buildreports.model;

import java.util.Objects;

/**
 *
 * Класс Маршрута
 *
 * @author Vladislav Klochkov
 * @version 1.0
 * @create 13.06.2018
 *
 * 13.06.2018
 *   1. Версия 1.0
 *
 */

public class Route {
    private String nameOfStationDeparture; // Станция отправления
    private String nameRoadOfStationDeparture; // Дорга станции отправления
    private String nameOfStationDestination; // Станция назначения
    private String nameRoadStationDestination; // Дорога станции назначения
    private String customer; // Заказчик

    private int count; // Количество рейсов одинаковых
    private int averageDistance; // Среднее расстояние
    private double averageRate; // Средняя ставка

    public Route(String nameOfStationDeparture, String nameRoadOfStationDeparture, String nameOfStationDestination, String nameRoadStationDestination, String customer) {
        this.nameOfStationDeparture = nameOfStationDeparture;
        this.nameRoadOfStationDeparture = nameRoadOfStationDeparture;
        this.nameOfStationDestination = nameOfStationDestination;
        this.nameRoadStationDestination = nameRoadStationDestination;
        this.customer = customer;
    }

    public Route(String nameOfStationDeparture, String nameRoadOfStationDeparture, String nameOfStationDestination, String nameRoadStationDestination, String customer, int count, int averageDistance, double averageRate) {
        this.nameOfStationDeparture = nameOfStationDeparture;
        this.nameRoadOfStationDeparture = nameRoadOfStationDeparture;
        this.nameOfStationDestination = nameOfStationDestination;
        this.nameRoadStationDestination = nameRoadStationDestination;
        this.customer = customer;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return count == route.count &&
                averageDistance == route.averageDistance &&
                Double.compare(route.averageRate, averageRate) == 0 &&
                Objects.equals(nameOfStationDeparture, route.nameOfStationDeparture) &&
                Objects.equals(nameRoadOfStationDeparture, route.nameRoadOfStationDeparture) &&
                Objects.equals(nameOfStationDestination, route.nameOfStationDestination) &&
                Objects.equals(nameRoadStationDestination, route.nameRoadStationDestination) &&
                Objects.equals(customer, route.customer);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nameOfStationDeparture, nameRoadOfStationDeparture, nameOfStationDestination, nameRoadStationDestination, customer, count, averageDistance, averageRate);
    }

    @Override
    public String toString() {
        return  nameOfStationDeparture +
                ", " + nameRoadOfStationDeparture +
                ", " + nameOfStationDestination +
                ", " + nameRoadStationDestination +
                ", " + customer;
    }
}
