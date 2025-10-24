package machine;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoffeeMachine {
    public static void main(String[] args) {
        boolean keepRunning = true;
        while (keepRunning){
            String action = getAction();

            switch (action){
                case "fill":
                    handleFillAction();
                    break;
                case "buy":
                    handleBuyAction();
                    break;
                case "take":
                    handleTakeAction();
                    break;
                case "remaining":
                    displayState();
                    break;
                case "exit":
                    keepRunning = false;
                    break;
                case "clean":
                    handleCleanMachine();
                    break;
                default:
                    break;
            }
        }
    }

    //create a scanner to reuse
    public  static Scanner myScanner = new Scanner(System.in);

    //create static variables for balance, waterMl, milkMl, coffeeGram, disposableCups
    public static int balance = 550;
    public static int waterMl = 400;
    public static int milkMl = 540;
    public static int coffeeGram = 120;
    public static int disposableCups = 9;
    public static int servedCups = 0;

    //create enum for actions possible
    public enum actions {FILL, BUY, TAKE}

    //create enum for coffee types
    public enum CoffeeType {
        ESPRESSO(1, "espresso", 250, 0, 16, 4),
        LATTE(2, "latte", 350, 75, 20, 7),
        CAPPUCCINO(3, "cappuccino", 200, 100, 12, 6);
        final int code;
        final String name;
        final int water;
        final int milk;
        final int coffee;
        final int price;
        CoffeeType(int code, String name, int water, int milk, int coffee, int price){
            this.code = code;
            this.name = name;
            this.water = water;
            this.milk = milk;
            this.coffee = coffee;
            this.price = price;
        }

        String getName(){
            return name;
        }
        int getCode(){
            return code;
        }
        int getWater(){
            return water;
        }
        int getMilk(){
            return milk;
        }
        int getCoffee(){
            return coffee;
        }
        int getPrice(){
            return price;
        }

        private static final Map<Integer, CoffeeType> coffeeTypeMap = Stream.of(values()).collect(Collectors.toMap(CoffeeType::getCode, e -> e));

        public static CoffeeType getCoffeeType(int code){
            return coffeeTypeMap.get(code);
        }

        public static String getNameFromCode(int code){
            for(CoffeeType type: CoffeeType.values()){
                if(type.getCode() == code){
                    return type.getName();
                }
            }
            return null;
        }

    }


    //create a function to get action/order (buy, fill, take)
    public static String getAction(){
//        Scanner sc = new Scanner(System.in);
        System.out.println("Write action (buy, fill, take, clean, remaining, exit): ");
        return myScanner.nextLine();
    }

    //create a function to handle fill
    public static void handleFillAction(){
        System.out.println("Write how many ml of water you want to add: ");
        int moreWaterMl = myScanner.nextInt();
        System.out.println("Write how many ml of milk you want to add: ");
        int moreMilkMl = myScanner.nextInt();
        System.out.println("Write how many grams of coffee beans you want to add: ");
        int moreCoffeeGram = myScanner.nextInt();
        System.out.println("Write how many disposable cups you want to add: ");
        int moreDisposableCups = myScanner.nextInt();

        waterMl = waterMl + moreWaterMl;
        milkMl = milkMl + moreMilkMl;
        coffeeGram = coffeeGram + moreCoffeeGram;
        disposableCups = disposableCups + moreDisposableCups;
    }

    public static void handleCleanMachine(){
        servedCups = 0;
        System.out.println("I have been cleaned!");
    }

    //create a function to handle buy
    public static void handleBuyAction(){
        if(servedCups >= 10){
            System.out.println("I need cleaning!");
            return;
        }
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
        String buyAction = myScanner.nextLine();
        if(!"back".equals(buyAction)){
            int buyCode = Integer.parseInt(buyAction);
            if(buyCode != 1 && buyCode != 2 && buyCode != 3){
                System.out.println("Invalid input. Please try again.");
            } else {
                CoffeeType coffeeType = CoffeeType.getCoffeeType(buyCode);
                String[] inventoryResult = getDecision(getInventory(coffeeType, 1));
                if(inventoryResult[0].equals("true")){
                    System.out.println(inventoryResult[1]);
                    waterMl = waterMl - coffeeType.getWater();
                    coffeeGram = coffeeGram - coffeeType.getCoffee();
                    milkMl = milkMl - coffeeType.getMilk();
                    balance = balance + coffeeType.getPrice();
                    disposableCups -= 1;
                    servedCups++;
                } else if(inventoryResult[0].equals("false")){
                    System.out.println(inventoryResult[1]);
                }
            }
        }
    }

    //create a function to handle take
    public static void handleTakeAction(){
        System.out.println("I gave you $" + balance);
        balance = 0;
    }

    //create a function to display state current/updated
    public static void displayState(){
        System.out.println("The coffee machine has:");
        System.out.println(waterMl + " ml of water");
        System.out.println(milkMl + " ml of milk");
        System.out.println(coffeeGram + " g of coffee beans");
        System.out.println(disposableCups + " disposable cups");
        System.out.println("$" + balance + " of money");
    }

    // create a function to get inventory and calculate how many cups can be made from inventory given the coffeType
    public static Map<String, Integer> getInventory(CoffeeType coffeeType, int cupsRequired){
        //create a hashmap of items that needs refilling
        Map<String, Integer> refillMap = new HashMap<>();

        //check the coffee type and confirm inventory capacity
        int maxWater, maxMilk, maxCoffee;
        if(coffeeType.getWater() != 0){
            maxWater = waterMl / coffeeType.getWater();
            if(maxWater < cupsRequired){
                refillMap.put("water", maxWater);
            }
        }
        if(coffeeType.getMilk() != 0){
            maxMilk = milkMl / coffeeType.getMilk();
            if(maxMilk < cupsRequired){
                refillMap.put("milk", maxMilk);
            }
        }
        if(coffeeType.getCoffee() != 0){
            maxCoffee = coffeeGram / coffeeType.getCoffee();
            if(maxCoffee < cupsRequired){
                refillMap.put("coffee", maxCoffee);
            }
        }
        if(disposableCups < cupsRequired){
            refillMap.put("cups", disposableCups);
        }

        return refillMap;
    }

    // return decision as string
    public static String[] getDecision(Map<String, Integer> refillMap){
        StringBuilder decision = new StringBuilder();
        String isEnough;
        if(refillMap.isEmpty()){
            isEnough = "true";
            decision.append("I have enough resources, making you a coffee!");
        } else {
            isEnough = "false";
            refillMap.forEach((key, value) -> {
                if(decision.isEmpty()){
                    decision.append("Sorry, not enough ").append(key);
                } else {
                    decision.append(" and ").append(key);
                }
            });
            decision.append("!");
        }
        return new String[]{isEnough, decision.toString()};
    }
}