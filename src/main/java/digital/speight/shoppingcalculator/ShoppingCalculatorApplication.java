package digital.speight.shoppingcalculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.List;

@SpringBootApplication
public class ShoppingCalculatorApplication {

    private static final Logger log = LoggerFactory.getLogger(ShoppingCalculatorApplication.class);

    private final CheckoutCalculator checkoutCalculator;

    public ShoppingCalculatorApplication(CheckoutCalculator checkoutCalculator) {
        this.checkoutCalculator = checkoutCalculator;
    }

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCalculatorApplication.class, args);
    }

    @Bean
    @Profile("!test")
    CommandLineRunner runner() {
        return args -> {
            log.info("User requested the following to be added to the basket:");
            for (int i = 0; i < args.length; i++) {
                log.info("{}. {}", i + 1, args[i]);
            }
            checkoutCalculator.calculate(List.of(args));
        };
    }

}
