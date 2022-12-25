package days;

import setup.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day5 extends Day {

    private List<Stack<String>> towers;
    private List<Instruction> instructions;

    @Override
    public void processInput() {
        towers = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();

        Matcher matcher = Pattern.compile("^(\s|(\\d)+)+$", Pattern.MULTILINE).matcher(input);
        matcher.find();
        String towerDefinition = matcher.group();

        for (String s : towerDefinition.split("\s+")) {
            if (!s.isEmpty()) {
                towers.add(new Stack<>());
                map.put(towerDefinition.indexOf(s), Integer.parseInt(s));
            }
        }

        Pattern p = Pattern.compile("\\[([A-Z])]");
        for (String line : lines()) {
            p.matcher(line).results().forEach(m -> {
                int i = map.get(m.start(1)) - 1;
                towers.get(i).push(m.group(1));
            });
        }

        towers.forEach(Collections::reverse);

        instructions = lines().stream().filter(s -> s.startsWith("move")).map(Instruction::parse).toList();
    }

    @Override
    public Object part1() {
        for (Instruction ins : instructions) {
            for (int i = 0; i < ins.amount; i++) {
                towers.get(ins.target).push(towers.get(ins.source).pop());
            }
        }


        return towers.stream().map(Stack::peek).collect(Collectors.joining());
    }

    @Override
    public Object part2() {
        for (Instruction ins : instructions) {
            Stack<String> temp = new Stack<>();
            for (int i = 0; i < ins.amount; i++) {
                temp.push(towers.get(ins.source).pop());
            }

            for (int i = 0; i < ins.amount; i++) {
                towers.get(ins.target).push(temp.pop());
            }
        }

        return towers.stream().map(Stack::peek).collect(Collectors.joining());
    }

    @Override
    public int getDay() {
        return 5;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "QNNTGTPFN";
    }

    @Override
    public String partTwoSolution() {
        return "GGNPJBTTR";
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    private record Instruction(int amount, int source, int target) {
        public static Instruction parse(String s) {
            String[] split = s.split("\s+");
            return new Instruction(Integer.parseInt(split[1]), Integer.parseInt(split[3]) - 1, Integer.parseInt(split[5]) - 1);
        }
    }
}
