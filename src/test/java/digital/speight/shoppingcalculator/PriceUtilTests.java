package digital.speight.shoppingcalculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceUtilTests {

    @DisplayName("Percentage is taken off number and answers returned are the mathematical floor of the decimal answer")
    @Test
    void floorPercentageSubtractorReturnsCorrectValues() {
        assertEquals(90, PriceUtil.floorPercentageSubtractor(100L, 10)); // decimal answer 90.0
        assertEquals(13, PriceUtil.floorPercentageSubtractor(33L, 60)); // decimal answer 13.2
        assertEquals(17, PriceUtil.floorPercentageSubtractor(22L, 19)); // decimal answer 17.82
    }

    @DisplayName("Long is formatted as GBP currency. The single 'p' denoting pence is shown when under 1 GBP.")
    @Test
    void formatLongToGBPCurrencyFormatsCorrectly() {
        assertEquals("£26.70", PriceUtil.formatLongToGBPCurrency(2670L));
        assertEquals("12p", PriceUtil.formatLongToGBPCurrency(12L));
    }

}
