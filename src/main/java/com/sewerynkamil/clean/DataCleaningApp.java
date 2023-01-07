package com.sewerynkamil.clean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static com.sewerynkamil.clean.DataCleaning.*;

public class DataCleaningApp {
    public static void main(String[] args) {
        String fileName = "data/sales-global.dat";
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream
                .map(applyRegex)
                .filter(onlyMatchingLines)
                .map(composeHashMap)
                .map(properNulls)
                .filter(discountAndOfferOnlyWithUserId)
                .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
