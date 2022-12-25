package days;

import setup.Day;

import java.util.ArrayList;
import java.util.List;

public class Day19 extends Day {

    private List<Blueprint> blueprints;

    private static List<State> simulate(int minutes, Blueprint blueprint) {
        List<State> queue = new ArrayList<>();
        queue.add(new State(new Tuple(0, 0, 0, 0), new Tuple(1, 0, 0, 0)));
        for (int i = 0; i < minutes; i++) {
            List<State> newQueue = new ArrayList<>();
            for (State state : queue) {
                var have = state.have;
                var make = state.make;
                if (have.add(blueprint.oreCost.negation()).valid()) {
                    var state1 = new State(have.add(make).add(blueprint.oreCost.negation()), make.add(new Tuple(1, 0, 0, 0)));
                    newQueue.add(state1);
                }
                if (have.add(blueprint.clayCost.negation()).valid()) {
                    var state1 = new State(have.add(make).add(blueprint.clayCost.negation()), make.add(new Tuple(0, 1, 0, 0)));
                    newQueue.add(state1);
                }
                if (have.add(blueprint.obsidianCost.negation()).valid()) {
                    var state1 = new State(have.add(make).add(blueprint.obsidianCost.negation()), make.add(new Tuple(0, 0, 1, 0)));
                    newQueue.add(state1);
                }
                if (have.add(blueprint.geodeCost.negation()).valid()) {
                    var state1 = new State(have.add(make).add(blueprint.geodeCost.negation()), make.add(new Tuple(0, 0, 0, 1)));
                    newQueue.add(state1);
                }
                var state1 = new State(have.add(make), make);
                newQueue.add(state1);

            }
            queue = newQueue.stream().sorted((s1, s2) -> {
                var _b = s1.have.add(s1.make);
                var _a = s2.have.add(s2.make);

                var a = List.of(_a.geode, _a.obsidian, _a.clay, _a.ore, s1.make.geode, s1.make.obsidian, s1.make.clay, s1.make.ore);
                var b = List.of(_b.geode, _b.obsidian, _b.clay, _b.ore, s2.make.geode, s2.make.obsidian, s2.make.clay, s2.make.ore);

                for (int j = 0; j < a.size(); j++) {
                    if (!a.get(j).equals(b.get(j))) {
                        return Integer.compare(a.get(j), b.get(j));
                    }
                }
                return 0;
            }).limit(1000).toList();
        }
        return queue;
    }

    @Override
    public void processInput() {
        blueprints = lines().stream().map(Blueprint::parse).toList();
    }

    @Override
    public Object part1() {
        int sum = 0;
        int minutes = 24;

        for (Blueprint blueprint : blueprints) {
            List<State> queue = simulate(minutes, blueprint);
            sum += queue.stream().mapToInt(s -> s.have.geode).max().getAsInt() * blueprint.id;
        }

        return sum;
    }

    @Override
    public Object part2() {
        int product = 1;
        int minutes = 32;

        for (Blueprint blueprint : blueprints.subList(0, 3)) {
            List<State> queue = simulate(minutes, blueprint);
            product *= queue.stream().mapToInt(s -> s.have.geode).max().getAsInt();
        }

        return product;
    }

    @Override
    public int getDay() {
        return 19;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "1703";
    }

    @Override
    public String partTwoSolution() {
        return "5301";
    }

    private record Tuple(int ore, int clay, int obsidian, int geode) {
        private Tuple add(Tuple other) {
            return new Tuple(ore + other.ore, clay + other.clay, obsidian + other.obsidian, geode + other.geode);
        }

        private Tuple negation() {
            return new Tuple(-ore, -clay, -obsidian, -geode);
        }

        private boolean valid() {
            return ore >= 0 && clay >= 0 && obsidian >= 0 && geode >= 0;
        }
    }

    private record State(Tuple have, Tuple make) {

    }

    private record Blueprint(int id, Tuple oreCost, Tuple clayCost, Tuple obsidianCost, Tuple geodeCost) {
        private static Blueprint parse(String s) {
            // get all numbers from string
            String s1 = s.replaceAll("[^0-9]", " ");
            var numbers = s1.trim().split(" +");

            var oreCost = new Tuple(Integer.parseInt(numbers[1]), 0, 0, 0);
            var clayCost = new Tuple(Integer.parseInt(numbers[2]), 0, 0, 0);
            var obsidianCost = new Tuple(Integer.parseInt(numbers[3]), Integer.parseInt(numbers[4]), 0, 0);
            var geodeCost = new Tuple(Integer.parseInt(numbers[5]), 0, Integer.parseInt(numbers[6]), 0);

            return new Blueprint(Integer.parseInt(numbers[0]), oreCost, clayCost, obsidianCost, geodeCost);
        }
    }


}
