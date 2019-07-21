package digital.speight.shoppingcalculator;

import digital.speight.shoppingcalculator.data.InventoryRepository;
import digital.speight.shoppingcalculator.data.model.Item;
import digital.speight.shoppingcalculator.data.model.MultiBuyDiscount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MultiBuyCalculatorTests {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private MultiBuyCalculator multiBuyCalculator;

    private Map<String, Queue<Integer>> queueOfMultiBuyDiscounts;

    @BeforeEach
    void mockInventoryFromRepository() {
        when(inventoryRepository.getAll()).thenReturn(List.of(createBreadItem(), createSoupItem(), createJamItem()));
    }

    @DisplayName("Basket with 1 soup does not add a discount for bread")
    @Test
    void multiBuyCriteriaNotMet() {
        queueOfMultiBuyDiscounts = multiBuyCalculator.calculateQueueOfMultiBuyDiscounts(List.of(createSoupItem()));
        verify(inventoryRepository).getAll();
        assertFalse(queueOfMultiBuyDiscounts.containsKey("Bread"));
    }

    @DisplayName("Basket with 4 soup adds a 50 percent off discount for 2 breads")
    @Test
    void multiBuyAddedTwice() {
        queueOfMultiBuyDiscounts = multiBuyCalculator.calculateQueueOfMultiBuyDiscounts(List.of(createSoupItem(), createSoupItem(), createSoupItem(), createSoupItem()));
        verify(inventoryRepository).getAll();
        assertFalse(queueOfMultiBuyDiscounts.containsKey("Soup"));
        assertTrue(queueOfMultiBuyDiscounts.containsKey("Bread"));
        Queue breadDiscountQueue = queueOfMultiBuyDiscounts.get("Bread");
        assertEquals(50, breadDiscountQueue.poll());
        assertEquals(50, breadDiscountQueue.poll());
        assertNull(breadDiscountQueue.poll());
    }

    @DisplayName("Basket with 2 multi-buy discounts active on the same item will apply the best discount first")
    @Test
    void multipleMultiBuysAddedWithBestFirst() {
        queueOfMultiBuyDiscounts = multiBuyCalculator.calculateQueueOfMultiBuyDiscounts(List.of(createSoupItem(), createSoupItem(), createJamItem()));
        verify(inventoryRepository).getAll();
        Queue breadDiscountQueue = queueOfMultiBuyDiscounts.get("Bread");
        assertEquals(60, breadDiscountQueue.poll());
        assertEquals(50, breadDiscountQueue.poll());
    }

    private Item createSoupItem() {
        Item soup = new Item();
        soup.setId("Soup");
        MultiBuyDiscount multiBuyDiscount = new MultiBuyDiscount();
        multiBuyDiscount.setRedemptionQuantity(2);
        multiBuyDiscount.setDiscountId("Bread");
        multiBuyDiscount.setDiscount(50);
        soup.setMultiBuyDiscount(multiBuyDiscount);
        return soup;
    }

    private Item createJamItem() {
        Item jam = new Item();
        jam.setId("Jam");
        MultiBuyDiscount multiBuyDiscount = new MultiBuyDiscount();
        multiBuyDiscount.setRedemptionQuantity(1);
        multiBuyDiscount.setDiscountId("Bread");
        multiBuyDiscount.setDiscount(60);
        jam.setMultiBuyDiscount(multiBuyDiscount);
        return jam;
    }

    private Item createBreadItem() {
        Item bread = new Item();
        bread.setId("Bread");
        return bread;
    }

}
