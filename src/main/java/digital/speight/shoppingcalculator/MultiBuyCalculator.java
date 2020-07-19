package digital.speight.shoppingcalculator;

import digital.speight.shoppingcalculator.data.InventoryRepository;
import digital.speight.shoppingcalculator.data.model.Item;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MultiBuyCalculator {

    private final InventoryRepository inventoryRepository;

    private final Map<String, Queue<Integer>> itemMultiBuyDiscounts = new HashMap<>();

    public MultiBuyCalculator(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Map<String, Queue<Integer>> calculateQueueOfMultiBuyDiscounts(List<Item> itemBasket) {
        var availableItems = inventoryRepository.getAll();
        var distinctItems = new HashSet<>(availableItems);
        for (var item : distinctItems) {
            long quantityOfItemInBasket = itemBasket.stream().filter(i -> i.equals(item)).count();
            addEligibleDiscountsForItemToQueue(item, quantityOfItemInBasket);
        }
        return itemMultiBuyDiscounts;
    }

    private void addEligibleDiscountsForItemToQueue(Item item, long quantity) {
        if (item.getMultiBuyDiscount() != null) {
            var multiBuyDiscount = item.getMultiBuyDiscount();
            long promoTargetItemCount = quantity / multiBuyDiscount.getRedemptionQuantity();
            if (promoTargetItemCount > 0) {
                if (!itemMultiBuyDiscounts.containsKey(multiBuyDiscount.getDiscountId())) {
                    itemMultiBuyDiscounts.put(multiBuyDiscount.getDiscountId(), new PriorityQueue<>(Collections.reverseOrder()));
                }
                var itemDiscountQueue = itemMultiBuyDiscounts.get(multiBuyDiscount.getDiscountId());
                for (int i = 0; i < promoTargetItemCount; i++) {
                    itemDiscountQueue.add(multiBuyDiscount.getDiscount());
                }
            }
        }
    }

}
