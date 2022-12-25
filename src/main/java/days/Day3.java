package days;

import setup.Day;
import util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Day3 extends Day {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private List<Pair<String, String>> rucksacks;
    private List<List<Pair<String, String>>> groups;

    @Override
    public void processInput() {
        rucksacks = lines().stream().map(s -> {
            int l = s.length();
            return new Pair<String, String>(s.substring(0, l / 2), s.substring(l / 2));
        }).toList();

        // divide rucksacks into groups of 3
        groups = new ArrayList<>();
        for (int i = 0; i < rucksacks.size(); i += 3) {
            List<Pair<String, String>> group = new ArrayList<>();
            group.add(rucksacks.get(i));
            group.add(rucksacks.get(i + 1));
            group.add(rucksacks.get(i + 2));
            groups.add(group);
        }
    }

    @Override
    public Object part1() {
        int priority = 0;
        for (Pair<String, String> rucksack : rucksacks) {
            // see if any character is in both strings
            for (char c : rucksack.a().toCharArray()) {
                if (rucksack.b().indexOf(c) != -1) {
                    int value = ALPHABET.indexOf(c) + 1;
                    priority += value;
                    break;
                }
            }
        }

        return priority;
    }

    @Override
    public Object part2() {
        int priority = 0;

        for (List<Pair<String, String>> group : groups) {
            List<String> strings = group.stream().map(p -> p.a() + p.b()).toList();
            for (char c : strings.get(0).toCharArray()) {
                if (strings.get(1).indexOf(c) != -1 && strings.get(2).indexOf(c) != -1) {
                    int value = ALPHABET.indexOf(c) + 1;
                    priority += value;
                    break;
                }
            }
        }

        return priority;
    }

    @Override
    public int getDay() {
        return 3;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "8185";
    }

    @Override
    public String partTwoSolution() {
        return "2817";
    }
}
