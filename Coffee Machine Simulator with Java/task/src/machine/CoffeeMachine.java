package machine;

import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter how many cups of coffee you will need:\n");
        int numberOfCofferCups = scanner.nextInt();

        System.out.printf("For %d cups of coffee you will need:\n", numberOfCofferCups);
        System.out.printf("%d ml oof water\n", numberOfCofferCups * 200);
        System.out.printf("%d ml of milk\n", numberOfCofferCups * 50);
        System.out.printf("%d g of coffee beans\n", numberOfCofferCups * 15);

    }
}