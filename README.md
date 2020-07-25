# Shopping Basket Calculator

[![Build Status](https://dev.azure.com/murrayspeight/murrayspeight/_apis/build/status/MurraySpeight.shopping-basket-calculator-spring?branchName=master)](https://dev.azure.com/murrayspeight/murrayspeight/_build/latest?definitionId=2&branchName=master)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=digital.speight:shopping-calculator&metric=alert_status)](https://sonarcloud.io/dashboard?id=digital.speight:shopping-calculator)

This application prices a basket of goods and accounts for special offers.
The goods that can be purchased, which are all priced in GBP, are:
* Soup – 65p per tin
* Bread – 80p per loaf
* Milk – £1.30 per bottle
* Apples – £1.00 per bag

Current special offers are:

* Apples have 10% off their normal price this week
* Buy 2 tins of soup and get a loaf of bread for half price

The application accepts a list of items in the basket and outputs the subtotal, the special offer discounts and the final
price.

Input is by the command line in the form:  
`./shopping-calculator-LOCAL-SNAPSHOT.jar Soup Soup Bread Apples`

Output appears to the console, for example:
```
Subtotal: £3.10
Apples 10% off: -10p
Total: £3.00
```

If no special offers are applicable, the code outputs:
```
Subtotal: £1.30
(no offers available)
Total: £1.30
```

## Build
Java 11 is required to run this application.  
Maven is required to package the jar.

## Data
The data for the application is supplied by a JSON file at `src/main/resources/inventory.json`.
An altered version is at `src/test/resources/inventory.json` for test purposes. 
The `InventoryJsonRepository` class provides the implementation to parse this JSON 
and map it to the model using `jackson-databind`.

## Basket Management
The `BasketManager` class takes the user input and validates it. If any of the items do not exist in the JSON data
store then the an `InvalidItemException` is thrown the program exits with a message to the user.  
If all the items are valid then a list of items are returned

## Discount Calculation
The standard discounts are stored in the data store in their percentage off form, for example 10 is 10% off.
The multi buy discounts are also stored there and have their own object that consists of the amount of items
to be purchased to invoke the offer as well as which item the offer applies to, and the percentage to be taken off.
The `MultiBuyCalculator` class creates a queue of discounts for each item. This is stored largest first so
that when the total is being processed, the largest discount is always added first. The target item does not
need to be in the basket for the discount to be added to the queue.

## Checkout Calculation 
The `CheckoutCalculator` class orchestrates the final calculation of the subtotal, discounts, and total with the 
`BasketManager` and `MultiBuyCalculator` being injected as dependencies.
  
## Testing
_JUnit 5_ and _Mockito_ are used to perform testing. Classes are tested independently using mocks
with the CheckoutCalculator using `@SpringBootTest` to create the application context for integration testing. 
