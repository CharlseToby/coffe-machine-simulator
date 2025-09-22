import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.List;

public class CoffeeMachineTest extends StageTest<String> {
    @Override
    public List<TestCase<String>> generate() {
        return List.of(
            new TestCase<String>()
                .setInput("25")
                .setAttach("25"),

            new TestCase<String>()
                .setInput("125")
                .setAttach("125"),

            new TestCase<String>()
                .setInput("1")
                .setAttach("1"),

            new TestCase<String>()
                .setInput("123")
                .setAttach("123")
        );
    }

    @Override
    public CheckResult check(String reply, String clue) {
        String[] lines = reply.split("\\n");
        if (lines.length < 4) {
            return CheckResult.wrong("The output contains " + lines.length + " lines, but it should contain 4.");
        }
        String[] last3Lines = {
                lines[lines.length - 3],
                lines[lines.length - 2],
                lines[lines.length - 1]
        };

        int cups = Integer.parseInt(clue);
        boolean hasWater = false, hasMilk = false, hasCoffeeBeans = false;

        for (String line : last3Lines) {
            line = line.toLowerCase();

            if (line.contains("water")) {
                hasWater = line.contains(Integer.toString(cups * 200));
                if (!hasWater) {
                    return CheckResult.wrong("For the input: " + clue + " cups of coffee\nYour program output: " + line + "\nBut the required amount of water is " + (cups * 200) + " ml");
                }
            } else if (line.contains("milk")) {
                hasMilk = line.contains(Integer.toString(cups * 50));
                if (!hasMilk) {
                    return CheckResult.wrong("For the input: " + clue + " cups of coffee\nYour program output: " + line + "\nBut the required amount of milk is " + (cups * 50) + " ml");
                }
            } else if (line.contains("coffee bean")) {
                hasCoffeeBeans = line.contains(Integer.toString(cups * 15));
                if (!hasCoffeeBeans) {
                    return CheckResult.wrong("For the input: " + clue + " cups of coffee\nYour program output: " + line + "\nBut the required amount of coffee beans is " + (cups * 15) + " g");
                }
            }
        }

        if (!hasWater) {
            return CheckResult.wrong("There is no line specifying the required amount of \"water\".");
        }
        if (!hasMilk) {
            return CheckResult.wrong("There is no line specifying the required amount of \"milk\".");
        }
        if (!hasCoffeeBeans) {
            return CheckResult.wrong("There is no line specifying the required amount of \"coffee beans\".");
        }

        return CheckResult.correct();
    }
}