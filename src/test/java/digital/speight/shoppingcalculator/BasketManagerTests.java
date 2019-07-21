package digital.speight.shoppingcalculator;

import digital.speight.shoppingcalculator.data.InventoryRepository;
import digital.speight.shoppingcalculator.data.model.Item;
import digital.speight.shoppingcalculator.exception.InvalidItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketManagerTests {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private BasketManager basketManager;

    private static Item createBreadItem() {
        Item bread = new Item();
        bread.setId("Bread");
        return bread;
    }

    @BeforeEach
    void mockInventoryFromRepository() {
        when(inventoryRepository.getOne(any())).thenReturn(Optional.of(createBreadItem()));
    }

    @DisplayName("Request to add a valid item from the repository will successfully add to the basket")
    @Test
    void validItemReturnsInBasket() throws InvalidItemException {
        when(inventoryRepository.getOne(any())).thenReturn(Optional.of(createBreadItem()));
        List<Item> basket = basketManager.createBasket(List.of("Bread"));
        verify(inventoryRepository).getOne(any());
        assertFalse(basket.isEmpty());
        assertEquals("Bread", basket.get(0).getId());
    }

    @DisplayName("Request to add an invalid item that is not in repository will throw an InvalidItemException")
    @Test
    void invalidItemThrowsInvalidItemException() {
        when(inventoryRepository.getOne(any())).thenReturn(Optional.empty());
        Exception exception = assertThrows(InvalidItemException.class, () ->
                basketManager.createBasket(List.of("Butter")));
        verify(inventoryRepository).getOne(any());
        assertEquals("Item with ID 'Butter' does not exist in the inventory.", exception.getMessage());

    }

}
