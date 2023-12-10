package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9 extends Day {
    private List<Extrapolation> extrapolations;

    @Override
    public void processInput() {
        extrapolations = lines().stream().map(Extrapolation::parse).toList();
    }

    @Override
    public Object part1() {
        long sum = 0;

        for (Extrapolation extrapolation : extrapolations) {
            sum += extrapolation.extrapolate();
        }

        return sum;
    }

    @Override
    public Object part2() {
        long sum = 0;

        for (Extrapolation extrapolation : extrapolations) {
            sum += extrapolation.extrapolateBackwards();
        }

        return sum;
    }

    @Override
    public int getDay() {
        return 9;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "1884768153";
    }

    @Override
    public String partTwoSolution() {
        return "1031";
    }

    private static class Extrapolation {
        private final List<Integer> values;
        private Extrapolation inner;

        private Extrapolation(List<Integer> values) {
            this.values = values;
        }

        private static Extrapolation parse(String s) {
            return new Extrapolation(Arrays.stream(s.split(" ")).map(Integer::parseInt).toList());
        }

        private void computeInner() {
            List<Integer> differences = new ArrayList<>();
            for (int i = 0; i < values.size() - 1; i++) {
                differences.add(values.get(i + 1) - values.get(i));
            }
            inner = new Extrapolation(differences);
        }

        private Integer extrapolate() {
            boolean allZero = values.stream().allMatch(i -> i == 0);

            if (allZero) return 0;

            if (inner == null) {
                computeInner();
            }

            Integer last = values.get(values.size() - 1);

            return last + inner.extrapolate();
        }

        private Integer extrapolateBackwards() {
            boolean allZero = values.stream().allMatch(i -> i == 0);

            if (allZero) return 0;

            if (inner == null) {
                computeInner();
            }

            Integer first = values.get(0);

            return first - inner.extrapolateBackwards();
        }

        @Override
        public String toString() {
            return values.toString();
        }
    }
}
