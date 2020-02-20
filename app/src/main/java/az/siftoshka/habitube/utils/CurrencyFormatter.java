package az.siftoshka.habitube.utils;

import java.text.DecimalFormat;

public class CurrencyFormatter {

    public static String format(long number) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,##0");
        return decimalFormat.format(Double.parseDouble(String.valueOf(number)));
    }
}
