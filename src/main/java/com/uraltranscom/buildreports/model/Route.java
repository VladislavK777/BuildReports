package com.uraltranscom.buildreports.model;

import java.util.*;

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
    private List<Integer> listVolume = new ArrayList<>(); // Массив объемов;
    private String volumeTotal; // Строка объединения объемов
    private int countOrder; // Количество ПС
    private boolean isOk = false; // Признак добавления

    public Route(String nameOfStationDeparture, String nameRoadOfStationDeparture, String customer, Integer volume, int countOrder) {
        this.nameOfStationDeparture = nameOfStationDeparture;
        this.nameRoadOfStationDeparture = nameRoadOfStationDeparture;
        this.customer = customer;
        this.listVolume.add(volume);
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

    public String getVolumeTotal() {
        return volumeTotal;
    }

    public List<Integer> getListVolume() {
        return listVolume;
    }

    public void setListVolume(List<Integer> listVolume) {
        this.listVolume = listVolume;
    }

    public void setVolumeTotal(String volumeTotal) {
        this.volumeTotal = volumeTotal;
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

    public void setStringBuilderVolume() {
        StringBuilder stringVolume = new StringBuilder();
        TreeSet<Integer> listVolume = new TreeSet<>(this.listVolume);
        Iterator iterator = listVolume.iterator();
        while (iterator.hasNext()) {
            Object volume = iterator.next();
            if (volume != listVolume.last()) {
                stringVolume.append(String.valueOf(volume)).append("/");
            } else {
                stringVolume.append(String.valueOf(volume));
            }
        }
        this.volumeTotal = stringVolume.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return countOrder == route.countOrder &&
                isOk == route.isOk &&
                Objects.equals(nameOfStationDeparture, route.nameOfStationDeparture) &&
                Objects.equals(nameRoadOfStationDeparture, route.nameRoadOfStationDeparture) &&
                Objects.equals(customer, route.customer) &&
                Objects.equals(listVolume, route.listVolume) &&
                Objects.equals(volumeTotal, route.volumeTotal);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nameOfStationDeparture, nameRoadOfStationDeparture, customer, listVolume, volumeTotal, countOrder, isOk);
    }

    @Override
    public String toString() {
        return "Route{" +
                "nameOfStationDeparture='" + nameOfStationDeparture + '\'' +
                ", nameRoadOfStationDeparture='" + nameRoadOfStationDeparture + '\'' +
                ", customer='" + customer + '\'' +
                ", listVolume=" + listVolume +
                ", volumeTotal='" + volumeTotal + '\'' +
                ", countOrder=" + countOrder +
                ", isOk=" + isOk +
                '}';
    }
}
