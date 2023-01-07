package com.sewerynkamil.helper;

import com.sewerynkamil.model.Currency;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class FXHelper {
    private final Currency baseCurrency;

    Map<LocalDate, Map<Currency, Double>> rates = new HashMap<>();

    public FXHelper(YearMonth ym, Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
        try (FileInputStream fis = new FileInputStream("data/rates-" + ym + ".ser")) {
            ObjectInputStream oos = new ObjectInputStream(fis);
            rates = (Map<LocalDate, Map<Currency, Double>>) oos.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public double getRate(LocalDate when, Currency targetCurrency) {
        Map<Currency, Double> dayRates = rates.get(when);
        if (dayRates == null) throw new IllegalArgumentException("Day rates not found");

        Double d = dayRates.get(targetCurrency);
        if (d == null) throw new IllegalArgumentException("Target currency not found");

        return d;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }
}
