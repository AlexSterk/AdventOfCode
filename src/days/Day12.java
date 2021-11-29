package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day12 extends Day {
    private Map<String, String> rules;
    private Map<Integer, String> state;

    @Override
    public void processInput() {
        List<String> split = List.of(input.trim().split("\n"));

        state = new HashMap<>();
        char[] chars = split.get(0).trim().replaceAll("[^.#]", "").toCharArray();
        for (int i = 0; i < chars.length; i++) {
            state.put(i, String.valueOf(chars[i]));
        }

        rules = new HashMap<>();

        split.subList(2, split.size()).forEach(s -> {
            String[] ss = s.split(" => ");
            rules.put(ss[0], ss[1]);
        });
    }

    @Override
    public void part1() {
        for (int n = 0; n < 20; n++) {
            Map<Integer, String> oldState = state;
            state = new HashMap<>();
            int minPlant = oldState.entrySet().stream().filter(x -> x.getValue().equals("#")).mapToInt(Map.Entry::getKey).min().getAsInt();
            int maxPlant = oldState.entrySet().stream().filter(x -> x.getValue().equals("#")).mapToInt(Map.Entry::getKey).max().getAsInt();

            minPlant -= 2;
            maxPlant += 2;
            IntStream.rangeClosed(minPlant, maxPlant).forEach(i -> {
                String collect = IntStream.rangeClosed(i - 2, i + 2).mapToObj(x -> oldState.getOrDefault(x, ".")).collect(Collectors.joining());
                state.put(i, rules.getOrDefault(collect, "."));
            });
        }

        System.out.println(state.entrySet().stream().filter(e -> Objects.equals(e.getValue(), "#")).mapToInt(Map.Entry::getKey).sum());
    }

    @Override
    public void part2() {
        processInput();
        List<Integer> sums = new ArrayList<>();

        for (int n = 0; n < 2000; n++) {
            sums.add(state.entrySet().stream().filter(e -> Objects.equals(e.getValue(), "#")).mapToInt(Map.Entry::getKey).sum());
            Map<Integer, String> oldState = state;
            state = new HashMap<>();
            int minPlant = oldState.entrySet().stream().filter(x -> x.getValue().equals("#")).mapToInt(Map.Entry::getKey).min().getAsInt();
            int maxPlant = oldState.entrySet().stream().filter(x -> x.getValue().equals("#")).mapToInt(Map.Entry::getKey).max().getAsInt();

            minPlant -= 2;
            maxPlant += 2;
            IntStream.rangeClosed(minPlant, maxPlant).forEach(i -> {
                String collect = IntStream.rangeClosed(i - 2, i + 2).mapToObj(x -> oldState.getOrDefault(x, ".")).collect(Collectors.joining());
                state.put(i, rules.getOrDefault(collect, "."));
            });
        }

        for (int i = 1; i < sums.size(); i++) {
            System.out.println(sums.get(i) - sums.get(i - 1));
        }

        long l = sums.get(1999) + 51 * (50000000000L - 1999);
        System.out.println(l);
    }

    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
