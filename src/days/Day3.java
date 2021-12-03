package days;

import setup.Day;

import java.util.*;

public class Day3 extends Day {
    private String[] numbers;
    private Map<Integer, Integer> oneCounts;

    @Override
    public void processInput() {
        numbers = input.split("\n");
    }

    @Override
    public Object part1() {
        oneCounts = new HashMap<>();

        for (int i = 0; i < numbers[0].length(); i++) {
            int index = i;
            long count = Arrays.stream(numbers)
                    .map(s -> s.charAt(index))
                    .filter(c -> c == '1')
                    .count();
            oneCounts.put(i, (int) count);
        }

        StringBuilder gammaRateString = new StringBuilder();
        StringBuilder epsilonRateString = new StringBuilder();
        for (int i = 0; i < numbers[0].length(); i++) {
            if (oneCounts.get(i) > numbers.length / 2) {
                gammaRateString.append(1);
                epsilonRateString.append(0);
            } else {
                gammaRateString.append(0);
                epsilonRateString.append(1);
            }
        }

        int gammaRate, epsilonRate;

        gammaRate = Integer.parseInt(gammaRateString.toString(), 2);
        epsilonRate = Integer.parseInt(epsilonRateString.toString(), 2);

        return gammaRate * epsilonRate;
    }

    @Override
    public Object part2() {
        int oxygen = 0;
        int co2 = 0;

        Set<String> oxygenSet = new HashSet<>(Set.of(numbers));
        Set<String> co2Set = new HashSet<>(Set.of(numbers));

        for (int i = 0; i < numbers[0].length(); i++) {
            int finalI = i;
            int oxyOneCount = (int) oxygenSet.stream().filter(s -> s.charAt(finalI) == '1').count();
            int co2OneCount = (int) co2Set.stream().filter(s -> s.charAt(finalI) == '1').count();
            int oxygenSize = oxygenSet.size();
            if (oxygenSize > 1)
                oxygenSet.removeIf(s -> s.charAt(finalI) != (oxyOneCount >= oxygenSize / 2.0 ? '1' : '0'));
            int co2Size = co2Set.size();
            if (co2Size > 1) co2Set.removeIf(s -> s.charAt(finalI) == (co2OneCount >= co2Size / 2.0 ? '1' : '0'));
        }

        oxygen = Integer.parseInt(oxygenSet.iterator().next(), 2);
        co2 = Integer.parseInt(co2Set.iterator().next(), 2);

        return oxygen * co2;
    }

    @Override
    public int getDay() {
        return 3;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
