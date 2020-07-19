package digital.speight.shoppingcalculator;

import java.text.NumberFormat;
import java.util.Locale;

final class PriceUtil {

    private PriceUtil() {
    }

    static long floorPercentageSubtractor(long number, int percentage) {
        float percentageAsADecimal = 1.0f - (float) (percentage / 100.0);
        return (long) (number * percentageAsADecimal);
    }

    static String formatLongToGBPCurrency(long currencyLong) {
        final int ONE_POUND = 100;
        if (currencyLong < ONE_POUND) {
            return currencyLong + "p";
        } else {
            var n = NumberFormat.getCurrencyInstance(Locale.UK);
            return n.format(currencyLong / 100.0);
        }
    }

}
