package days;

import setup.Day;
import util.Maths;

import java.util.*;
import java.util.stream.Collectors;

import static util.Annotations.Solution;

//@TestInput("test2")
public class Day20 extends Day {
    private Map<String, Module> modules;
    private BroadcastModule button;
    private int lowSent = 0;
    private int highSent = 0;

    @Override
    public void processInput() {
        modules = new HashMap<>();

        for (String line : lines()) {
            var split = line.split(" ");
            var name = split[0];
            var module = Module.fromString(name);
            modules.put(module.name, module);
        }

        for (String line : lines()) {
            var split = line.split(" -> ");
            var source = modules.get(split[0].replaceAll("[%&]", ""));
            split = split[1].split(", ");
            for (String destination : split) {
                var dest = modules.computeIfAbsent(destination, TestModule::new);

                if (dest instanceof ConjunctionModule conjunction) {
                    conjunction.inputs.put(source, false);
                }

                source.addDestination(dest);
            }
        }

        button = new BroadcastModule("button");
        modules.put("button", button);
        button.addDestination(modules.get("broadcaster"));
    }

    @Solution("898557000")
    @Override
    public Object part1() {

        lowSent = 0;
        highSent = 0;
        for (int i = 0; i < 1000; i++) {
            pushButton(i);
        }
        System.out.println("Low sent: " + lowSent);
        System.out.println("High sent: " + highSent);

        return (long) lowSent * highSent;
    }

    private void pushButton(int i) {
        var pulse = new Pulse(button, false, modules.get("broadcaster"), i);
        var queue = new LinkedList<Pulse>();
        queue.add(pulse);

        while (!queue.isEmpty()) {
            var current = queue.poll();
            var destination = current.destination;
            var high = current.high;

            if (high) {
                highSent++;
            } else {
                lowSent++;
            }

            destination.receivePulse(current);
            for (Module d : destination.destinations) {
                var p = destination.nextPulse(current, d);
                if (p != null) {
                    queue.add(p);
                }
            }
        }
    }

    @Solution("238420328103151")
    @Override
    public Object part2() {
        // Find all modules that have rx as destination
        var ms = new ArrayList<>(List.of(modules.get("rx")));
        while (ms.size() == 1) {
            var c = ms.getFirst();
            ms = modules.values().stream()
                    .filter(m -> m.destinations.contains(c))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        int i = 1; // Start at 1 because we count button presses
        while (ms.stream().anyMatch(m -> m instanceof ConjunctionModule c && c.pressed_needed_for_high == null)) {
            pushButton(i++);
        }

        long lcm = 1;
        for (ConjunctionModule m : ms.stream().map(m -> (ConjunctionModule) m).toList()) {
            lcm = Maths.lcm(lcm, m.pressed_needed_for_high);
        }

        return lcm;
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public int getDay() {
        return 20;
    }

    private static abstract class Module {
        public final String name;
        private final Set<Module> destinations = new HashSet<>();

        private Module(String name) {
            this.name = name;
        }

        public static Module fromString(String s) {
            if (s.equals("broadcaster")) return new BroadcastModule(s);
            if (s.startsWith("%")) return new FlipFlopModule(s.substring(1));
            if (s.startsWith("&")) return new ConjunctionModule(s.substring(1));
            throw new IllegalArgumentException("Invalid module type: " + s);
        }

        public void addDestination(Module destination) {
            destinations.add(destination);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Module module = (Module) o;
            return Objects.equals(name, module.name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }

        @Override
        public String toString() {
            return this.name;
        }

        public abstract void receivePulse(Pulse pulse);

        public abstract Pulse nextPulse(Pulse pulse, Module destination);
    }

    private static class FlipFlopModule extends Module {
        private boolean on = false;

        private FlipFlopModule(String name) {
            super(name);
        }

        @Override
        public void receivePulse(Pulse pulse) {
            if (!pulse.high) {
                on = !on;
            }
        }

        @Override
        public Pulse nextPulse(Pulse pulse, Module destination) {
            if (pulse.high) return null;
            return new Pulse(this, on, destination, pulse.button_presses);
        }
    }

    private static class ConjunctionModule extends Module {
        private final Map<Module, Boolean> inputs = new HashMap<>();

        private Integer pressed_needed_for_high = null;

        private ConjunctionModule(String name) {
            super(name);
        }

        @Override
        public void receivePulse(Pulse pulse) {
            inputs.put(pulse.source, pulse.high);
        }

        @Override
        public Pulse nextPulse(Pulse pulse, Module destination) {
            Pulse pl = new Pulse(this, !inputs.values().stream().allMatch(p -> p), destination, pulse.button_presses);
            if (pl.high && pressed_needed_for_high == null) {
                pressed_needed_for_high = pulse.button_presses;
            }
            return pl;
        }
    }

    private static class BroadcastModule extends Module {
        private BroadcastModule(String name) {
            super(name);
        }

        @Override
        public void receivePulse(Pulse pulse) {

        }

        @Override
        public Pulse nextPulse(Pulse pulse, Module destination) {
            return new Pulse(this, pulse.high, destination, pulse.button_presses);
        }
    }

    private static class TestModule extends Module {
        private TestModule(String name) {
            super(name);
        }

        @Override
        public void receivePulse(Pulse pulse) {

        }

        @Override
        public Pulse nextPulse(Pulse pulse, Module destination) {
            return null;
        }
    }

    private record Pulse(Module source, boolean high, Module destination, int button_presses) {
        @Override
        public String toString() {
            return "%s -%s-> %s".formatted(source.name, high ? "high" : "low", destination.name);
        }
    }
}
