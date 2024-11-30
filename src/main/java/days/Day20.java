package days;

import setup.Day;

import java.util.*;

import static util.Annotations.Solution;
import static util.Annotations.TestInput;

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
                var dest = modules.getOrDefault(destination, new TestModule(destination));

                if (dest instanceof ConjunctionModule conjunction) {
                    conjunction.lastPulseReceivedFrom.put(source, false);
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
            pushButton();
        }
        System.out.println("Low sent: " + lowSent);
        System.out.println("High sent: " + highSent);

        return (long) lowSent * highSent;
    }

    private void pushButton() {
        var pulse = new Pulse(button, false, modules.get("broadcaster"));
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
            Boolean p = destination.nextPulse();
            if (p != null) {
                for (Module dest : destination.destinations) {
                    queue.add(new Pulse(destination, p, dest));
                }
            }
        }
    }

    @Solution("")
    @Override
    public Object part2() {

        return null;
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

        protected Pulse lastReceivedPulse;

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

        public abstract Boolean nextPulse();
    }

    private static class FlipFlopModule extends Module {
        private boolean on = false;

        private FlipFlopModule(String name) {
            super(name);
        }

        @Override
        public void receivePulse(Pulse pulse) {
            lastReceivedPulse = pulse;

            if (!pulse.high) {
                on = !on;
            }
        }

        @Override
        public Boolean nextPulse() {
            if (lastReceivedPulse.high) return null;
            return on;
        }
    }

    private static class ConjunctionModule extends Module {
        private final Map<Module, Boolean> lastPulseReceivedFrom = new HashMap<>();

        private ConjunctionModule(String name) {
            super(name);
        }

        @Override
        public void receivePulse(Pulse pulse) {
            lastReceivedPulse = pulse;
            lastPulseReceivedFrom.put(pulse.source, pulse.high);
        }

        @Override
        public Boolean nextPulse() {
            return !lastPulseReceivedFrom.values().stream().allMatch(p -> p);
        }
    }

    private static class BroadcastModule extends Module {
        private BroadcastModule(String name) {
            super(name);
        }

        @Override
        public void receivePulse(Pulse pulse) {
            lastReceivedPulse = pulse;
        }

        @Override
        public Boolean nextPulse() {
            return lastReceivedPulse.high;
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
        public Boolean nextPulse() {
            return null;
        }
    }

    private record Pulse(Module source, boolean high, Module destination) {
        @Override
        public String toString() {
            return "%s -%s-> %s".formatted(source.name, high ? "high" : "low", destination.name);
        }
    }
}
