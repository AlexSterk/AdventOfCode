package days;

import setup.Day;

import java.util.HashSet;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day21 extends Day {

    private Map<String, Monkey> monkeys;

    @Override
    public void processInput() {
        monkeys = lines().stream().map(Monkey::parse).collect(Collectors.toMap(m -> m.name, m -> m));
    }

    @Override
    public Object part1() {
        var toCheck = new HashSet<>(monkeys.values());
        while (!monkeys.get("root").number) {
            run(toCheck);
        }

        return monkeys.get("root").equation;
    }

    private void run(HashSet<Monkey> toCheck) {
        var toRemove = new HashSet<Monkey>();
        for (Monkey monkey : toCheck) {
            if (monkey.number) {
                toRemove.add(monkey);
                continue;
            }
            String[] s = monkey.equation.split(" ");
            if (s[0].matches("[a-z]+") && monkeys.containsKey(s[0]) && monkeys.get(s[0]).number) {
                monkey.equation = monkey.equation.replace(s[0], monkeys.get(s[0]).equation);
            }

            if (s[2].matches("[a-z]+") && monkeys.containsKey(s[2]) && monkeys.get(s[2]).number) {
                monkey.equation = monkey.equation.replace(s[2], monkeys.get(s[2]).equation);
            }

            monkey.solve();
            if (monkey.number) {
                toRemove.add(monkey);
            }
        }
        toCheck.removeAll(toRemove);
    }

    @Override
    public Object part2() {
        Monkey root = monkeys.get("root");
        root.equation = root.equation.replaceAll("[-+*/]", "=");
        monkeys.remove("humn");

        var toCheck = new HashSet<>(monkeys.values());
        int toCheckSize = toCheck.size();

        while (true) {
            run(toCheck);
            if (toCheck.size() == toCheckSize) {
                break;
            }
            toCheckSize = toCheck.size();
        }

        // substitute equations, starting with root
        var test = substitute(root.equation).replaceAll("humn", "x");
        System.out.println("Paste the following equation into https://www.mathpapa.com/equation-solver/");
        System.out.println(test);

        return null;
    }

    private String substitute(String equation) {
        Pattern pattern = Pattern.compile("[a-z]+");

        for (MatchResult m : pattern.matcher(equation).results().toList()) {
            String s = m.group();
            if (monkeys.containsKey(s)) {
                equation = equation.replace(s,"(%s)".formatted(substitute(monkeys.get(s).equation)));
            }
        }

        return equation;
    }

    @Override
    public int getDay() {
        return 21;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "66174565793494";
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    private static class Monkey {
        public final String name;
        public String equation;

        public boolean number;

        public Monkey(String name, String equation) {
            this.name = name;
            this.equation = equation;
        }

        private static Monkey parse(String s) {
            String[] split = s.split(": ");
            String name = split[0];
            String equation = split[1];
            Monkey monkey = new Monkey(name, equation);

            if (equation.matches("\\d+")) {
                monkey.number = true;
            }

            return monkey;
        }

        private void solve() {
            if (equation.matches("\\d+ [-+*/=] \\d+")) {
                String[] split = equation.split(" ");
                long a = Long.parseLong(split[0]);
                long b = Long.parseLong(split[2]);
                String op = split[1];
                long result = switch (op) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> a / b;
                    case "=" -> a == b ? 1 : 0;
                    default -> 0;
                };
                equation = String.valueOf(result);
                number = true;
            }
        }

        @Override
        public String toString() {
            return equation;
        }
    }
}
