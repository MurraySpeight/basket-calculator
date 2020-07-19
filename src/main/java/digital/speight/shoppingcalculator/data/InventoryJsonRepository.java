package digital.speight.shoppingcalculator.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import digital.speight.shoppingcalculator.data.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class InventoryJsonRepository implements InventoryRepository {

    private static final Logger log = LoggerFactory.getLogger(InventoryJsonRepository.class);

    private final Resource inventoryResource = new ClassPathResource("inventory.json");

    @Override
    public List<Item> getAll() {
        var items = getAllItemsFromJson();
        log.debug("All items: {}", items);
        return items;
    }

    @Override
    public Optional<Item> getOne(String id) {
        var items = getAllItemsFromJson();
        return items.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }

    private List<Item> getAllItemsFromJson() {
        var mapper = new ObjectMapper();
        List<Item> items;
        try {
            items = mapper.readValue(inventoryResource.getInputStream(), new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error("Inventory JSON file cannot be found.", e);
            return Collections.emptyList();
        }
        return items;
    }

}
