package com.sewerynkamil.clean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataCleaningApp {
    public static void main(String[] args) {
        String s = "SaleTransaction{uuid='7D30065D-6318-4809-5260-A6C90AB574DD',timestamp='10/04/2022 04:26:03',type='colombian',size='GG',price='€3',offer='',discount='',userId=3096,country=ITALY,city='Turin'}";

        Matcher m = Pattern.compile(
                "SaleTransaction\\{uuid='?([A-Fa-f0-9\\-]+)'?,timestamp='(.*)',type='?(.*?)'?,size='?(.{1,3}?)'?,price='?(.*?)'?,offer='?(.*?)'?,discount='?(.*?)'?,userId=(\\d*),country='?(.*?)'?,city='?(.*?)'?}"
        ).matcher(s);

        System.out.println(m.matches());
    }
}
