package com.uraltranscom.buildreports.model;

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
    private String customer; // Заказчик
    private int volumeFrom; // Объем от
    private int volumeTo; // Объем до
    private int countOrder; // Количество ПС
    private boolean isOk = false; // Признак добавления

    public Route(String nameOfStationDeparture, String nameRoadOfStationDeparture, String customer, int volumeFrom, int volumeTo, int countOrder) {
        this.nameOfStationDeparture = nameOfStationDeparture;
        this.nameRoadOfStationDeparture = nameRoadOfStationDeparture;
        this.customer = customer;
        this.volumeFrom = volumeFrom;
        this.volumeTo = volumeTo;
        this.countOrder = countOrder;
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

    public int getVolumeFrom() {
        return volumeFrom;
    }

    public void setVolumeFrom(int volumeFrom) {
        this.volumeFrom = volumeFrom;
    }

    public int getVolumeTo() {
        return volumeTo;
    }

    public void setVolumeTo(int volumeTo) {
        this.volumeTo = volumeTo;
    }

    public int getCountOrder() {
        return countOrder;
    }

    public void setCountOrder(int countOrder) {
        this.countOrder = countOrder;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    @Override
    public String toString() {
        return  nameOfStationDeparture +
                ", " + nameRoadOfStationDeparture +
                ", " + customer +
                ", " + volumeFrom +
                ", " + countOrder +
                ", " + isOk;
    }
}
