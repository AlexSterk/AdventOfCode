package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day16 extends Day {
    private List<? extends Move> moves;
    private List<String> programs;

    @Override
    public void processInput() {
        moves = Arrays.stream(input.trim().split(",")).map(s -> switch (s.charAt(0)) {
            case 's' -> new Spin(Integer.parseInt(s.substring(1)));
            case 'x' -> new Exchange(Integer.parseInt(s.substring(1, s.indexOf('/'))), Integer.parseInt(s.substring(s.indexOf('/') + 1)));
            case 'p' -> new Partner(s.substring(1, s.indexOf('/')), s.substring(s.indexOf('/') + 1));

            default -> throw new IllegalStateException("Unexpected value: " + s.charAt(0));
        }).toList();

        programs = new ArrayList<>();
        String ps = "abcdefghijklmnop";
        for (int i = 0; i < ps.length(); i++) {
            String substring = ps.substring(i, i + 1);
            programs.add(substring);
        }
    }

    @Override
    public String part1() {
        moves.forEach(m -> m.execute(programs));
        return String.join("", programs);
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public Object part2() {
        List<String> configs = new ArrayList<>();
        String s = String.join("", programs);
        int i = 0;
        do {
            i++;
            configs.add(s);
            s = part1();
        } while (!configs.contains(s));
        int startOfLoop = configs.indexOf(s);
        int loopSize = i - startOfLoop;

        return configs.get(1_000_000_000 % loopSize);
    }

    @Override
    public int getDay() {
        return 16;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "jcobhadfnmpkglie";
    }

    @Override
    public String partTwoSolution() {
        return "pclhmengojfdkaib";
    }

    private sealed interface Move {
        void execute(List<String> programs);
    }

    record Spin(int X) implements Move {
        @Override
        public void execute(List<String> programs) {
            List<String> end = new ArrayList<>(programs.subList(programs.size() - X, programs.size()));
            programs.removeAll(end);
            programs.addAll(0, end);
        }
    }

    record Exchange(int A, int B) implements Move {
        @Override
        public void execute(List<String> programs) {
            String temp = programs.get(A);
            programs.set(A, programs.get(B));
            programs.set(B, temp);
        }
    }

    record Partner(String A, String B) implements Move {
        @Override
        public void execute(List<String> programs) {
            new Exchange(programs.indexOf(A), programs.indexOf(B)).execute(programs);
        }
    }
}

