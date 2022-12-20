package days;

import setup.Day;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16 extends Day {

    private HashMap<String, Valve> valves;
    private LinkedHashMap<ValveStep, Long> cache;

    @Override
    public void processInput() {
        List<Valve> valvesList = lines().stream().map(Valve::parse).toList();
        valves = new HashMap<>();
        valvesList.forEach(valve -> valves.put(valve.name, valve));
    }

    @Override
    public Object part1() {
        cache = new LinkedHashMap<>();
        var cur = new ValveStep(valves.get("AA"), Set.of(), 30);
        return simulatePart1(cur);
    }

    private Long simulatePart1(ValveStep step) {
        if (cache.containsKey(step)) {
            return cache.get(step);
        }

        var cur = step.current;
        var opened = step.opened;
        var minutes = step.minutes;

        if (minutes <= 0) return 0L;
        long best = 0;


        long val = (long) (minutes - 1) * cur.flowRate;
        Set<Valve> curOpened = new HashSet<>(opened);
        curOpened.add(cur);
        for (String tunnel : cur.tunnels) {
            if (!opened.contains(cur) && val > 0)
                best = Math.max(best, val + simulatePart1(new ValveStep(valves.get(tunnel), curOpened, minutes - 2)));
            best = Math.max(best, simulatePart1(new ValveStep(valves.get(tunnel), opened, minutes - 1)));
        }

        cache.put(step, best);

        return best;
    }

    @Override
    public Object part2() {
        var cache = new LinkedHashMap<ValveStep, Long>();
        var cur = new ValveStep(valves.get("AA"), Set.of(), 26);

        return simulatePart2(cur, cache);
    }

    private Long simulatePart2(ValveStep step, LinkedHashMap<ValveStep, Long> cache) {
        if (cache.containsKey(step)) {
            return cache.get(step);
        }

        var cur = step.current;
        var opened = step.opened;
        var minutes = step.minutes;

        if (minutes <= 0) {
            // We don't have to simulate the elephant and you at the same time
            // We can simulate them sequentially, using the previous run with the opened valves (and cache of course)
            return simulatePart1(new ValveStep(valves.get("AA"), opened, 26));
        }
        long best = 0;

        long val = (long) (minutes - 1) * cur.flowRate;
        Set<Valve> curOpened = new HashSet<>(opened);
        curOpened.add(cur);
        for (String tunnel : cur.tunnels) {
            if (!opened.contains(cur) && val > 0)
                best = Math.max(best, val + simulatePart2(new ValveStep(valves.get(tunnel), curOpened, minutes - 2), cache));
            best = Math.max(best, simulatePart2(new ValveStep(valves.get(tunnel), opened, minutes - 1), cache));
        }

        cache.put(step, best);

        return best;
    }

    @Override
    public int getDay() {
        return 16;
    }

    @Override
    public String partOneSolution() {
        return "2359";
    }

// CI runner lacks heap space to run this test
//    @Override
//    public String partTwoSolution() {
//        return "2999";
//    }

    @Override
    public boolean isTest() {
        return false;
    }

    private record Valve(String name, int flowRate, List<String> tunnels) {
        private static final Pattern pattern = Pattern.compile("Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? ((?:\\w+(?:, )?)+)");

        private static Valve parse(String input) {
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String name = matcher.group(1);
                int flowRate = Integer.parseInt(matcher.group(2));
                String[] tunnels = matcher.group(3).split(", ");
                return new Valve(name, flowRate, List.of(tunnels));
            } else {
                throw new IllegalArgumentException("Invalid input: " + input);
            }
        }
    }

    private record ValveStep(Valve current, Set<Valve> opened, int minutes) {

    }
}
