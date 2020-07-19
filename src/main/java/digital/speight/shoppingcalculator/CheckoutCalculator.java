package digital.speight.shoppingcalculator;

import digital.speight.shoppingcalculator.data.model.Item;
import digital.speight.shoppingcalculator.exception.InvalidItemException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

@Service
public class CheckoutCalculator {

    private final MultiBuyCalculator multiBuyCalculator;
    private final BasketManager basketManager;

    private long total;

    private long subtotal;

    public CheckoutCalculator(MultiBuyCalculator multiBuyCalculator, BasketManager basketManager) {
        this.multiBuyCalculator = multiBuyCalculator;
        this.basketManager = basketManager;
    }

    private static void printDiscount(Item item, long finalPrice, int discount) {
        System.out.format(
                "%s %d%% off: -%s%n",
                item.getId(),
                discount,
                PriceUtil.formatLongToGBPCurrency(item.getPrice() - finalPrice)
        );
    }

    private static long calculateSubtotal(List<Item> basket) {
        var subtotal = 0L;
        for (var item : basket) {
            subtotal += item.getPrice();
        }
        System.out.format("Subtotal: %s%n", PriceUtil.formatLongToGBPCurrency(subtotal));
        return subtotal;
    }

    private static Optional<Integer> largestMultiBuyDiscountAvailable(Item item, Map<String, Queue<Integer>> queueOfMultiBuyDiscounts) {
        if (queueOfMultiBuyDiscounts.containsKey(item.getId())) {
            var itemMultiBuyDiscounts = queueOfMultiBuyDiscounts.get(item.getId());
            var lowestAvailableMultiBuyDiscount = itemMultiBuyDiscounts.poll();
            if (lowestAvailableMultiBuyDiscount != null) {
                return Optional.of(lowestAvailableMultiBuyDiscount);
            }
        }
        return Optional.empty();
    }

    public long getTotal() {
        return total;
    }

    public long getSubtotal() {
        return subtotal;
    }

    public void calculate(List<String> basketRequest) {
        try {
            var basket = basketManager.createBasket(basketRequest);
            subtotal = calculateSubtotal(basket);
            total = calculateTotal(basket);
        } catch (InvalidItemException e) {
            System.err.println(e.getMessage());
        }
    }

    private long calculateTotal(List<Item> basket) {
        boolean discountsApplied = false;
        long total = 0L;
        Map<String, Queue<Integer>> queueOfMultiBuyDiscounts = multiBuyCalculator.calculateQueueOfMultiBuyDiscounts(basket);
        for (var item : basket) {
            int multiBuyDiscount = largestMultiBuyDiscountAvailable(item, queueOfMultiBuyDiscounts).orElse(0);
            int standardDiscount = item.getDiscount() != null ? item.getDiscount() : 0;
            int highestDiscount = Math.max(multiBuyDiscount, standardDiscount);
            if (highestDiscount > 0) {
                discountsApplied = true;
                long finalPrice = PriceUtil.floorPercentageSubtractor(item.getPrice(), highestDiscount);
                printDiscount(item, finalPrice, highestDiscount);
                total += finalPrice;
            } else {
                total += item.getPrice();
            }
        }
        if (!discountsApplied) {
            System.out.println("(no offers available)");
        }
        System.out.format("Total: %s%n", PriceUtil.formatLongToGBPCurrency(total));
        return total;
    }

}
