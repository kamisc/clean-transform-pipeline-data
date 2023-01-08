package com.sewerynkamil.transform;

import com.sewerynkamil.fxscraping.Currency;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.stream.Stream;

import static com.sewerynkamil.clean.DataCleaning.*;

public class DataTransformationApp {
    public static void main(String[] args) {
        YearMonth ym = YearMonth.of(2020, 4);
        DataTransformation dt = new DataTransformation(ym, Currency.GBP);
        String fileName = "data/sales-global.dat";
        try (
            Stream<String> stream = Files.lines(Paths.get(fileName));
            PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get("data/sales-global-transformed.dat")))
        ) {
            stream
                    .map(applyRegex)
                    .filter(onlyMatchingLines)
                    .map(composeHashMap)
                    .map(properNulls)
                    .filter(discountAndOfferOnlyWithUserId)
                    .map(dt.standardizeDate)
                    .map(dt.fxConvertPrice)
                    .map(dt.enrichWithWeatherData)
                    .map(dt.minMaxScalingTemperature)
                    .map(dt.oneHotEncodeType)
                    .map(dt.oneHotEncodeSize)
                    .map(dt.composeSaleTransaction)
                    .forEach(pw::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
