package digital.speight.shoppingcalculator;

import digital.speight.shoppingcalculator.data.InventoryRepository;
import digital.speight.shoppingcalculator.data.model.Item;
import digital.speight.shoppingcalculator.exception.InvalidItemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BasketManager {

    private static final Logger log = LoggerFactory.getLogger(BasketManager.class);

    private final InventoryRepository inventoryRepository;

    public BasketManager(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Item> createBasket(List<String> basketRequest) throws InvalidItemException {
        List<Item> basketItems = new ArrayList<>();
        for (var itemId : basketRequest) {
            var item = inventoryRepository.getOne(itemId);
            if (item.isPresent()) {
                basketItems.add(item.get());
            } else {
                var message = String.format("Item with ID '%s' does not exist in the inventory.", itemId);
                log.error(message);
                throw new InvalidItemException(message);
            }

        }
        return basketItems;
    }

}
