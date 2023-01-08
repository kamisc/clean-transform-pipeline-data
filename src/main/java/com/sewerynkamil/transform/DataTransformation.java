package com.sewerynkamil.transform;

import com.sewerynkamil.fxscraping.FXHelper;
import com.sewerynkamil.weather.WeatherHelper;
import com.sewerynkamil.fxscraping.Currency;
import com.sewerynkamil.model.SaleTransaction;
import com.sewerynkamil.weather.WeatherLocation;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataTransformation {
    private final FXHelper fxHelper;
    private final WeatherHelper weatherHelper;
    private final double tempDiff;

    public DataTransformation(YearMonth ym, Currency baseCurrency) {
        fxHelper = new FXHelper(ym, baseCurrency);
        weatherHelper = new WeatherHelper(ym);
        tempDiff = getWeatherHelper().getMaxTemp() - getWeatherHelper().getMinTemp();
    }

    public final Function<Map<String, Object>, Map<String, Object>> standardizeDate = transactionMap -> {
        DateTimeFormatter formatter = switch (SaleTransaction.Country.valueOf(transactionMap.get("country").toString())) {
            case UK -> DateTimeFormatter.ofPattern("d['st']['nd']['rd']['th'] MMM yyyy HH:mm:ss", Locale.UK);
            case ITALY -> DateTimeFormatter.ofPattern("d/M/yyyy HH:mm:ss", Locale.ITALY);
            case JAPAN -> DateTimeFormatter.ofPattern("yyyy年M月d日 HH時mm分ss秒", Locale.JAPAN);
            case CANADA -> DateTimeFormatter.ofPattern("M/d/yyyy HH:mm:ss", Locale.CANADA);
        };
        LocalDateTime dt = LocalDateTime.parse(transactionMap.get("timestamp").toString(), formatter);
        transactionMap.put("timestamp", dt);
        return transactionMap;
    };

    public final Function<Map<String, Object>, Map<String, Object>> fxConvertPrice = transactionMap -> {
      String regex = null;
      LocalDate fxDate = ((LocalDateTime) transactionMap.get("timestamp")).toLocalDate();
      double fxRate = 0.0;
      switch (SaleTransaction.Country.valueOf(transactionMap.get("country").toString())) {
          case UK -> {
              regex = "£(\\d+\\.\\d{2})";
              fxRate = 1.0;
          }
          case ITALY -> {
              regex = "€(\\d+(\\.\\d{1,2})?)";
              fxRate = getFxHelper().getRate(fxDate, Currency.EUR);
          }
          case JAPAN -> {
              regex = "¥(\\d+)";
              fxRate = getFxHelper().getRate(fxDate, Currency.JPY);
          }
          case CANADA -> {
              regex = "CAD\\$(\\d+\\.\\d{2})";
              fxRate = getFxHelper().getRate(fxDate, Currency.CAD);
          }
      };
      Matcher matcher = Pattern.compile(regex).matcher(transactionMap.get("price").toString());
      if (!matcher.matches()) {
          throw new IllegalArgumentException("Invalid price value");
      }
      double d = Double.parseDouble(matcher.group(1));
      transactionMap.put("price", d * fxRate);
      return transactionMap;
    };

    public final Function<Map<String, Object>, Map<String, Object>> enrichWithWeatherData = transactionMap -> {
        LocalDate weatherDate = ((LocalDateTime) transactionMap.get("timestamp")).toLocalDate();
        String city = transactionMap.get("city").toString().toUpperCase();
        city = Normalizer.normalize(city, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        String countryShort = switch (city) {
            case "TURIN" -> "IT";
            case "TOKYO" -> "JP";
            case "LONDON" -> "GB";
            case "MONTREAL" -> "CA";
            default -> "";
        };

        WeatherLocation location = WeatherLocation.valueOf(countryShort);
        double temp = getWeatherHelper().getWeather(weatherDate, location).getTemperature();
        transactionMap.put("temperature", temp);
        return transactionMap;
    };

    public final Function<Map<String, Object>, Map<String, Object>> minMaxScalingTemperature = transactionMap -> {
        double temp = (double) transactionMap.get("temperature");
        temp = (temp - getWeatherHelper().getMinTemp()) / getTempDiff();
        transactionMap.put("temperature", temp);
        return transactionMap;
    };

    public final Function<Map<String, Object>, Map<String, Object>> oneHotEncodeType = transactionMap -> {
        int[] typeVector = {0, 0, 0, 0};
        int idx = switch (transactionMap.get("type").toString()) {
            case "italian", "espresso", "イタリア" -> 0;
            case "brazilian", "ブラジル" -> 1;
            case "colombian", "コロンビア" -> 2;
            case "blend", "ブレンド" -> 3;
            default -> throw new IllegalArgumentException("Invalid coffe type");
        };
        typeVector[idx] = 1;
        transactionMap.put("type", typeVector);
        return transactionMap;
    };

    public final Function<Map<String, Object>, Map<String, Object>> oneHotEncodeSize = transactionMap -> {
      int[] sizeVector = {0, 0, 0, 0};
      int idx = switch (transactionMap.get("size").toString()) {
          case "GG", "XL", "超大" -> 0;
          case "G", "L", "大" -> 1;
          case "M", "中" -> 2;
          case "P", "S", "小" -> 3;
          default -> throw new IllegalStateException("Invalid coffee size");
      };
      sizeVector[idx] = 1;
      transactionMap.put("size", sizeVector);
      return transactionMap;
    };

    public final Function<Map<String, Object>, SaleTransaction> composeSaleTransaction = map ->
      new SaleTransaction()
              .withUuid(map.get("uuid").toString())
              .withTimestamp((LocalDateTime)  map.get("timestamp"))
              .withType((int[]) map.get("type"))
              .withSize((int[]) map.get("size"))
              .withPrice((double) map.get("price"))
              .withOffer(map.get("offer") == null ? null : map.get("offer").toString())
              .withDiscount(map.get("discount") == null ? null : map.get("discount").toString())
              .withUserId(Long.parseLong(map.get("userId").toString()))
              .withCity(map.get("city").toString())
              .withCountry(SaleTransaction.Country.valueOf(map.get("country").toString()));

    public FXHelper getFxHelper() {
        return fxHelper;
    }

    public WeatherHelper getWeatherHelper() {
        return weatherHelper;
    }

    public double getTempDiff() {
        return tempDiff;
    }
}
