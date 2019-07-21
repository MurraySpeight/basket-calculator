package digital.speight.shoppingcalculator.data;

import digital.speight.shoppingcalculator.data.model.Item;
import digital.speight.shoppingcalculator.data.model.MultiBuyDiscount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryJsonRepositoryTests {

    private InventoryRepository inventoryRepository;

    @BeforeEach
    void reloadJson() {
        inventoryRepository = new InventoryJsonRepository();
    }

    @DisplayName("getAll returns all of the 5 items from the JSON file")
    @Test
    void testGetAllReturnsAllItems() {
        assertEquals(5, inventoryRepository.getAll().size());
    }

    @DisplayName("getOne with an invalid ID returns an empty Optional")
    @Test
    void testGetOneWithInvalidIdReturnsEmptyOptional() {
        assertFalse(inventoryRepository.getOne("Pasta").isPresent());
    }

    @DisplayName("getOne with a valid ID returns an Optional containing an item with all of the properties that exist in the JSON, and null if they are not set")
    @Test
    void testGetOneWithValidIdReturnsOptionalWithItem() {
        Item jam = inventoryRepository.getOne("Jam").get();
        assertItemFullPopulated(jam);
        Item milk = inventoryRepository.getOne("Milk").get();
        assertFieldNullWhereDataMissing(milk);
    }

    private void assertItemFullPopulated(Item item) {
        assertEquals("Jam", item.getId());
        assertEquals(5, item.getDiscount());
        assertEquals(99L, item.getPrice());
        MultiBuyDiscount multiBuyDiscount = item.getMultiBuyDiscount();
        assertEquals("Bread", multiBuyDiscount.getDiscountId());
        assertEquals(25, multiBuyDiscount.getDiscount());
        assertEquals(2, multiBuyDiscount.getRedemptionQuantity());
    }

    private void assertFieldNullWhereDataMissing(Item item) {
        assertNull(item.getDiscount());
        assertNull(item.getMultiBuyDiscount());
    }

}
