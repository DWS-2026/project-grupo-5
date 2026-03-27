package com.canaryshop.canaryshop.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberFormatter {
    public static String getFormattedNumber(Double number){
        BigDecimal format = BigDecimal.valueOf(number);
        return format.setScale(2, RoundingMode.HALF_UP).toString();
    }
    public static String getFormattedNumber(Float number){
        return BigDecimal.valueOf(number).setScale(2, RoundingMode.HALF_UP).toString();
    }
}
