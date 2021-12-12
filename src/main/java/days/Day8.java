package days;

import setup.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8 extends Day {
    private Map<String, Integer> registers;
    private List<Instruction> instructions;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        instructions = Arrays.stream(lines).map(Instruction::Instruction).toList();
        registers = new HashMap<>();
        instructions.forEach(ins -> {
            registers.put(ins.register, 0);
            registers.put(ins.conditionRegister, 0);
        });
    }

    @Override
    public Object part1() {
        instructions.forEach(ins -> ins.execute(registers));

        return Collections.max(registers.values());
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "7296";
    }

    private record Instruction(String register, boolean increment, int value, String conditionRegister, String op, int compare) {
        private static final Pattern PATTERN = Pattern.compile("(\\w+) (\\w+) (-?\\d+) if (\\w+) (.+) (-?\\d+)");
        private static Instruction Instruction(String s) {
            Matcher matcher = PATTERN.matcher(s);
            matcher.matches();
            return new Instruction(
                    matcher.group(1),
                    matcher.group(2).equals("inc"),
                    Integer.parseInt(matcher.group(3)),
                    matcher.group(4),
                    matcher.group(5),
                    Integer.parseInt(matcher.group(6))
            );
        }

        private void execute(Map<String, Integer> registers) {
            int v = registers.get(conditionRegister);
            if (conditionHolds(v)) {
                int old = registers.get(register);
                registers.put(register, increment ? old + value : old - value);
            }
        }

        private boolean conditionHolds(int value) {
            return switch (op) {
                case "==" -> value == compare;
                case "!=" -> value != compare;
                case ">" -> value > compare;
                case "<" -> value < compare;
                case "<=" -> value <= compare;
                case ">=" -> value >= compare;
                default -> throw new IllegalStateException(op);
            };
        }
    }
}
