package days;

import setup.Day;

import java.util.*;
import java.util.stream.IntStream;

public class Day12 extends Day {
    private String state;
    private Map<String, String> rules;

    @Override
    public void processInput() {
        List<String> split = List.of(input.trim().split("\n"));
        state = split.get(0).trim().replaceFirst("[^.#]+", "");

        rules = new HashMap<>();

        split.subList(2, split.size()).forEach(s -> {
            String[] ss = s.split(" => ");
            rules.put(ss[0], ss[1]);
        });
    }

    @Override
    public void part1() {
        List<String> pastStates = new ArrayList<>();

         for (int n = 0; n < 20; n ++){
             pastStates.add(state);
//             System.out.println(state);
             state = "....." + state + ".....";

            StringBuilder newState = new StringBuilder();
            List<String> substrings = new ArrayList<>();

            IntStream.range(0, state.length() - 4).mapToObj(i -> state.substring(i, i + 5)).forEachOrdered(substrings::add);

            for (String s : substrings) {
                newState.append(rules.getOrDefault(s, "."));
            }
            state = newState.toString().replaceAll("^\\.+|\\.+$", "");
        }
        pastStates.add(state);
        System.out.println(String.join("", pastStates).split("#", -1).length - 1);
    }

    @Override
    public void part2() {

    }

    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public boolean isTest() {
        return true;
    }
}
