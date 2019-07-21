package digital.speight.shoppingcalculator;

import digital.speight.shoppingcalculator.exception.InvalidItemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class ITCheckoutCalculatorTests {

    @Autowired
    CheckoutCalculator checkoutCalculator;
    @SpyBean
    private MultiBuyCalculator multiBuyCalculator;
    @SpyBean
    private BasketManager basketManager;

    @DisplayName("Simple non-discount items returning correct total and subtotal")
    @Test
    void nonDiscountItemsAddedCorrectly() throws InvalidItemException {
        List<String> basketRequest = List.of("Milk", "Milk");
        checkoutCalculator.calculate(basketRequest);
        verify(multiBuyCalculator).calculateQueueOfMultiBuyDiscounts(any());
        verify(basketManager).createBasket(any());
        assertEquals(260L, checkoutCalculator.getSubtotal());
        assertEquals(260L, checkoutCalculator.getTotal());
    }

    @DisplayName("Jam and soup multi-buys are applied to 2 of the breads separately, " +
            "and the last bread applies its own discount")
    @Test
    void multiBuyDiscountsAreCorrectlyApplied() throws InvalidItemException {
        List<String> basketRequest = List.of("Jam", "Jam", "Soup", "Soup", "Bread", "Bread", "Bread");
        checkoutCalculator.calculate(basketRequest);
        verify(multiBuyCalculator).calculateQueueOfMultiBuyDiscounts(any());
        verify(basketManager).createBasket(any());
        assertEquals(568L, checkoutCalculator.getSubtotal());
        assertEquals(490L, checkoutCalculator.getTotal());
    }

}
