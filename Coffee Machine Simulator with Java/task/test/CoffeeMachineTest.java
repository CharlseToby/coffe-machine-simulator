import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;


class TestClue {
    String input;

    TestClue(String input) {
        this.input = input;
    }
}

public class CoffeeMachineTest extends StageTest<TestClue> {
    @Override
    public List<TestCase<TestClue>> generate() {
        return List.of(
            new TestCase<TestClue>()
                .setAttach(new TestClue("take\n"))
                .setInput("take\n"),

            new TestCase<TestClue>()
                .setAttach(new TestClue("buy\n1\n"))
                .setInput("buy\n1\n"),

            new TestCase<TestClue>()
                .setAttach(new TestClue("buy\n2\n"))
                .setInput("buy\n2\n"),

            new TestCase<TestClue>()
                .setAttach(new TestClue("buy\n3\n"))
                .setInput("buy\n3\n"),

            new TestCase<TestClue>()
                .setAttach(new TestClue("fill\n2001\n510\n101\n21\n"))
                .setInput("fill\n2001\n510\n101\n21\n")
        );
    }

    @Override
    public CheckResult check(String reply, TestClue clue) {
        String[] lines = reply.trim().split("\\n");

        // Ensure the program is printing something.
        if (lines.length <= 1) {
            return CheckResult.wrong("Looks like you didn't print anything!");
        }

        // Parsing action from the input clue.
        String[] clueLines = clue.input.trim().split("\\n");
        String action = clueLines[0].trim();

        // Extract machine state values from the output.
        List<Integer> milk = new ArrayList<>();
        List<Integer> water = new ArrayList<>();
        List<Integer> coffeeBeans = new ArrayList<>();
        List<Integer> cups = new ArrayList<>();
        List<Integer> money = new ArrayList<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] words = line.trim().split("\\s+");

            // Check if the line contains information about resources.
            String firstWord = words[0].replace("$", "");

            int amount;
            try {
                amount = Integer.parseInt(firstWord);
            } catch (Exception e) {
                continue;
            }

            if (line.contains("milk")) {
                milk.add(amount);
            } else if (line.contains("water")) {
                water.add(amount);
            } else if (line.contains("beans")) {
                coffeeBeans.add(amount);
            } else if (line.contains("cups")) {
                cups.add(amount);
            } else if (line.contains("money")) {
                money.add(amount);
            }
        }

        if (milk.size() != 2) {
            return CheckResult.wrong("Your program should display the coffee machine's state both before and after each action.\n" +
                    "So, there should be 2 lines showing the \"milk\" amount, but found " + milk.size() + " such line(s).");
        }
        if (water.size() != 2) {
            return CheckResult.wrong("Your program should display the coffee machine's state both before and after each action.\n" +
                    "So, there should be 2 lines showing the \"water\" amount, but found " + water.size()+ " such line(s).");
        }
        if (coffeeBeans.size() != 2) {
            return CheckResult.wrong("Your program should display the coffee machine's state both before and after each action.\n" +
                    "So, there should be 2 lines showing the \"coffee beans\" amount, but found " + coffeeBeans.size()+ " such line(s).");
        }
        if (cups.size() != 2) {
            return CheckResult.wrong("Your program should display the coffee machine's state both before and after each action.\n" +
                    "So, there should be 2 lines showing the \"cups\" amount, but found " + cups.size()+ " such line(s).");
        }
        if (money.size() != 2) {
            return CheckResult.wrong("Your program should display the coffee machine's state both before and after each action.\n" +
                    "So, there should be 2 lines showing the \"money\" amount, but found " + money.size()+ " such line(s).");
        }

        // Check that initial state values are correct
        if (water.get(0) != 400 || milk.get(0) != 540 || coffeeBeans.get(0) != 120
                || cups.get(0) != 9 || money.get(0) != 550) {
            String expectedState = "The coffee machine has:\n" +
                    "400 ml of water\n" +
                    "540 ml of milk\n" +
                    "120 g of coffee beans\n" +
                    "9 disposable cups\n" +
                    "$550 of money\n";

            String actualState = "The coffee machine has:\n" +
                    water.get(0) + " ml of water\n" +
                    milk.get(0) + " ml of milk\n" +
                    coffeeBeans.get(0) + " g of coffee beans\n" +
                    cups.get(0) + " disposable cups\n" +
                    "$" + money.get(0) + " of money\n";

            return CheckResult.wrong("Initial coffee machine state is incorrect!\n\n"
                    + "Expected state:\n" + expectedState + "\n"
                    + "Actual state:\n" + actualState + "\n"
            );
        }

        // Variables for difference calculations
        int milk0 = milk.get(0);
        int milk1 = milk.get(milk.size() - 1);

        int water0 = water.get(0);
        int water1 = water.get(water.size() - 1);

        int beans0 = coffeeBeans.get(0);
        int beans1 = coffeeBeans.get(coffeeBeans.size() - 1);

        int cups0 = cups.get(0);
        int cups1 = cups.get(cups.size() - 1);

        int money0 = money.get(0);
        int money1 = money.get(money.size() - 1);

        int diffWater = water1 - water0;
        int diffMilk = milk1 - milk0;
        int diffBeans = beans1 - beans0;
        int diffCups = cups1 - cups0;
        int diffMoney = money1 - money0;

        switch (action) {
            case "take" -> {  // Handle "take" action
                if (diffMilk != 0) {
                    return CheckResult.wrong("After \"take\" action, milk amount shouldn't be changed");
                }
                if (diffWater != 0) {
                    return CheckResult.wrong("After \"take\" action, water amount shouldn't be changed");
                }
                if (diffBeans != 0) {
                    return CheckResult.wrong("After \"take\" action, coffee beans " + "amount shouldn't be changed");
                }
                if (diffCups != 0) {
                    return CheckResult.wrong("After \"take\" action, the number of cups shouldn't be changed");
                }
                if (money1 != 0) {
                    return CheckResult.wrong("After \"take\" action, the total money in the coffee machine should become zero");
                }

                return CheckResult.correct();
            }
            case "buy" -> {  // Handle "buy" action
                String coffeeOption = clueLines[1].trim();

                switch (coffeeOption) {
                    case "1" -> { // Espresso
                        if (diffWater != -250) {
                            return CheckResult.wrong("After buying a cup of espresso, the water amount should be lowered by 250 ml");
                        }
                        if (diffMilk != 0) {
                            return CheckResult.wrong("After buying a cup of espresso, the milk amount shouldn't be changed");
                        }
                        if (diffBeans != -16) {
                            return CheckResult.wrong("After buying a cup of espresso, the coffee beans amount should be lowered by 16 g");
                        }
                        if (diffCups != -1) {
                            return CheckResult.wrong("After buying a cup of espresso, the number of cups should be lowered by 1");
                        }
                        if (diffMoney != 4) {
                            return CheckResult.wrong("After buying a cup of espresso, the total money collected should be increased by $4");
                        }

                        return CheckResult.correct();
                    }
                    case "2" -> { // Latte
                        if (diffWater != -350) {
                            return CheckResult.wrong("After buying a cup of latte, the water amount should be lowered by 350 ml");
                        }
                        if (diffMilk != -75) {
                            return CheckResult.wrong("After buying a cup of latte, the milk amount should be lowered by 75 ml");
                        }
                        if (diffBeans != -20) {
                            return CheckResult.wrong("After buying a cup of latte, the coffee beans amount should be lowered by 20 g");
                        }
                        if (diffCups != -1) {
                            return CheckResult.wrong("After buying a cup of latte, the number of cups should be lowered by 1");
                        }
                        if (diffMoney != 7) {
                            return CheckResult.wrong("After buying a cup of latte, the total money collected should be increased by $7");
                        }

                        return CheckResult.correct();
                    }
                    case "3" -> { // Cappuccino
                        if (diffWater != -200) {
                            return CheckResult.wrong("After buying a cup of cappuccino, the water amount should be lowered by 200 ml");
                        }
                        if (diffMilk != -100) {
                            return CheckResult.wrong("After buying a cup of cappuccino, the milk amount should be lowered by 100 ml");
                        }
                        if (diffBeans != -12) {
                            return CheckResult.wrong("After buying a cup of cappuccino, the coffee beans amount should be lowered by 12 g");
                        }
                        if (diffCups != -1) {
                            return CheckResult.wrong("After buying a cup of cappuccino, the number of cups should be lowered by 1");
                        }
                        if (diffMoney != 6) {
                            return CheckResult.wrong("After buying a cup of cappuccino, the total money collected should be increased by $6");
                        }

                        return CheckResult.correct();
                    }

                    default -> CheckResult.wrong("Invalid coffee option: " + coffeeOption);
                }
            }
            case "fill" -> {  // Handle "fill" action
                int addedWater = Integer.parseInt(clueLines[1]);
                int addedMilk = Integer.parseInt(clueLines[2]);
                int addedCoffeeBeans = Integer.parseInt(clueLines[3]);
                int addedCups = Integer.parseInt(clueLines[4]);

                if (diffMoney != 0) {
                    return CheckResult.wrong("After \"fill\" action, the money amount should not be changed");
                }
                if (diffWater != addedWater) {
                    return CheckResult.wrong("After \"fill\" action, the water amount was expected to be increased by " + addedWater + ", but was increased by " + diffWater);
                }
                if (diffMilk != addedMilk) {
                    return CheckResult.wrong("After \"fill\" action, the milk amount was expected to be increased by " + addedMilk + ", but was increased by " + diffMilk);
                }
                if (diffBeans != addedCoffeeBeans) {
                    return CheckResult.wrong("After \"fill\" action, the coffee beans amount was expected to be increased by " + addedCoffeeBeans + ", but was increased by " + diffBeans);
                }
                if (diffCups != addedCups) {
                    return CheckResult.wrong("After \"fill\" action, the number of cups was expected to be increased by " + addedCups + ", but was increased by " + diffCups);
                }

                return CheckResult.correct();
            }

            default -> CheckResult.wrong("Invalid coffee option: " + action);
        }

        return CheckResult.correct();
    }
}
