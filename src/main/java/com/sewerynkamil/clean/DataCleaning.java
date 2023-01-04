package com.sewerynkamil.clean;

import java.util.regex.Pattern;

public interface DataCleaning {
    Pattern saleTransactionRegex = Pattern.compile(
    "SaleTransaction\\{uuid='?([A-Fa-f0-9\\-]+)'?,timestamp='(.*)',type='?(.*?)'?,size='?(.{1,3}?)'?,price='?(.*?)'?,offer='?(.*?)'?,discount='?(.*?)'?,userId=(\\d*),country='?(.*?)'?,city='?(.*?)'?}"
    );
}