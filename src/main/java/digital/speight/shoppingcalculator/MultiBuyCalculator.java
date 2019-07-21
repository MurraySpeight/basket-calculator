package digital.speight.shoppingcalculator;

import digital.speight.shoppingcalculator.data.InventoryRepository;
import digital.speight.shoppingcalculator.data.model.Item;
import digital.speight.shoppingcalculator.data.model.MultiBuyDiscount;
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
        List<Item> availableItems = inventoryRepository.getAll();
        Set<Item> distinctItems = new HashSet<>(availableItems);
        for (Item item : distinctItems) {
            long quantityOfItemInBasket = itemBasket.stream().filter(i -> i.equals(item)).count();
            addEligibleDiscountsForItemToQueue(item, quantityOfItemInBasket);
        }
        return itemMultiBuyDiscounts;
    }

    private void addEligibleDiscountsForItemToQueue(Item item, long quantity) {
        if (item.getMultiBuyDiscount() != null) {
            MultiBuyDiscount multiBuyDiscount = item.getMultiBuyDiscount();
            long promoTargetItemCount = quantity / multiBuyDiscount.getRedemptionQuantity();
            if (promoTargetItemCount > 0) {
                if (!itemMultiBuyDiscounts.containsKey(multiBuyDiscount.getDiscountId())) {
                    itemMultiBuyDiscounts.put(multiBuyDiscount.getDiscountId(), new PriorityQueue<>(Collections.reverseOrder()));
                }
                Queue<Integer> itemDiscountQueue = itemMultiBuyDiscounts.get(multiBuyDiscount.getDiscountId());
                for (int i = 0; i < promoTargetItemCount; i++) {
                    itemDiscountQueue.add(multiBuyDiscount.getDiscount());
                }
            }
        }
    }

}
