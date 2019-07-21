package digital.speight.shoppingcalculator.data;

import digital.speight.shoppingcalculator.data.model.Item;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {

    List<Item> getAll();

    Optional<Item> getOne(String id);

}
