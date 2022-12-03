package days;

import setup.Day;
import util.Pair;

import java.util.List;

public class Day3 extends Day {

    private List<Pair<String, String>> rucksacks;

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public void processInput() {
        rucksacks = lines().stream().map(s -> {
            int l = s.length();
            return new Pair<String, String>(s.substring(0, l / 2), s.substring(l / 2));
        }).toList();
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
        return null;
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
}
