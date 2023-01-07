package com.sewerynkamil.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

public class SaleTransaction {
    private String uuid;
    private LocalDateTime timestamp;
    private int[] type;
    private int[] size;
    private double price;
    private String offer;
    private String discount;
    private Country country;
    private String city;
    private long userId;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int[] getType() {
        return type;
    }

    public void setType(int[] type) {
        this.type = type;
    }

    public int[] getSize() {
        return size;
    }

    public void setSize(int[] size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public SaleTransaction withUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public SaleTransaction withTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public SaleTransaction withType(int[] type) {
        this.type = type;
        return this;
    }

    public SaleTransaction withSize(int[] size) {
        this.size = size;
        return this;
    }

    public SaleTransaction withPrice(double price) {
        this.price = price;
        return this;
    }

    public SaleTransaction withOffer(String offer) {
        this.offer = offer;
        return this;
    }

    public SaleTransaction withDiscount(String discount) {
        this.discount = discount;
        return this;
    }

    public SaleTransaction withCity(String city) {
        this.city = city;
        return this;
    }

    public SaleTransaction withCountry(Country country) {
        this.country = country;
        return this;
    }

    public SaleTransaction withUserId(long userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleTransaction that = (SaleTransaction) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return "SaleTransaction{" +
                "uuid='" + uuid + '\'' +
                ", timestamp=" + timestamp +
                ", type=" + Arrays.toString(type) +
                ", size=" + Arrays.toString(size) +
                ", price=" + price +
                ", offer='" + offer + '\'' +
                ", discount='" + discount + '\'' +
                ", country=" + country +
                ", city='" + city + '\'' +
                ", userId=" + userId +
                '}';
    }

    public enum Country {
        UK, JAPAN, ITALY, CANADA
    }
}
