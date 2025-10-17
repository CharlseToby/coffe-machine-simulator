package machine;

import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) {
        displayState();
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
             default:
                 break;
         }

         displayState();
//        int[] inventory = getInventory();
//        int cupsPossible = getCupsPossible(inventory);
//        System.out.println(getDecision(cupsPossible, inventory[3]));
    }

    //create a scanner to reuse
    public  static Scanner myScanner = new Scanner(System.in);

    //create static variables for balance, waterMl, milkMl, coffeeGram, disposableCups
    public static int balance = 550;
    public static int waterMl = 400;
    public static int milkMl = 540;
    public static int coffeeGram = 120;
    public static int disposableCups = 9;

    //create enum for actions possible
    public enum actions {FILL, BUY, TAKE}

    //create enum for coffee types
    public enum CoffeeType {
        ESPRESSO(1, "espresso"), LATTE(2, "latte"), CAPPUCCINO(3, "cappuccino");
        final int code;
        final String name;
        CoffeeType(int code, String name){
            this.code = code;
            this.name = name;
        }

        String getName(){
            return name;
        }
        int getCode(){
            return code;
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
        System.out.println("Write action (buy, fill, take): ");
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

    //create a function to handle buy
    public static void handleBuyAction(){
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino: ");
        int buyCode = myScanner.nextInt();
        if("espresso".equals(CoffeeType.getNameFromCode(buyCode))){
            waterMl -= 250;
            coffeeGram -= 16;
            disposableCups -= 1;
            balance += 4;
        } else if("latte".equals(CoffeeType.getNameFromCode(buyCode))){
            waterMl -= 350;
            milkMl -= 75;
            coffeeGram -= 20;
            disposableCups -= 1;
            balance += 7;
        } else if("cappuccino".equals(CoffeeType.getNameFromCode(buyCode))){
            waterMl -= 200;
            milkMl -= 100;
            coffeeGram -= 12;
            disposableCups -= 1;
            balance += 6;
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

    // create a function to get inventory
    public static int[] getInventory(){
        System.out.println("write how many ml of water the coffee machine has: ");
        Scanner sc = new Scanner(System.in);
        int waterMl = sc.nextInt();
        System.out.println("Write how many ml of milk the coffee machine has: ");
        int milkMl = sc.nextInt();
        System.out.println("Write how many grams of coffee beans the coffee machine has: ");
        int coffeeGram = sc.nextInt();
        System.out.println("Write how many cups of coffee you will need: ");
        int cups = sc.nextInt();

        return new int[]{waterMl, milkMl, coffeeGram, cups};
    }

    // create a function to calculate how many cups can be made from inventory
    public static int getCupsPossible(int[] inventory){
        if(inventory[0] == 0 || inventory[1] == 0 || inventory[2] == 0) return 0;
        inventory[0] = inventory[0] / 200;
        inventory[1] = inventory[1] / 50;
        inventory[2] = inventory[2] / 15;

        return Math.min(Math.min(inventory[0], inventory[1]), inventory[2]);
    }

    // return decision as string
    public static String getDecision(int numberOfCupsPossible, int numberOfCupsNeeded){
        String decision;
        if(numberOfCupsNeeded > numberOfCupsPossible){
            decision = "No, I can make only " + numberOfCupsPossible + " cup(s) of coffee";
        } else if(numberOfCupsNeeded == numberOfCupsPossible){
            decision = "Yes, I can make that amount of coffee";
        } else {
            decision = "Yes, I can make that amount of coffee (and even " + (numberOfCupsPossible - numberOfCupsNeeded) + " more than that)";
        }
        return decision;
    }
}