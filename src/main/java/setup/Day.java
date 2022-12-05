package setup;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public abstract class Day {
    /**
     * Input
     */
    public final String input;

    /**
     * Reads the input into a single String and calls {@link Day#processInput()} to process the input.
     * Make sure to implement {@link Day#getDay()}
     */
    public Day() {
        boolean test = this.isTest();
        String i;
        try {
            i = Files.readString(Paths.get(String.format("data/day%d/%s.txt", getDay(), test ? "test" : "input")));
        } catch (IOException e) {
            i = "";
            e.printStackTrace();
            System.exit(1);
        }
        input = i;
    }

    public Day(String in) {
        String i;
        try {
            i = Files.readString(Paths.get(in));
        } catch (IOException e) {
            i = "";
            e.printStackTrace();
            System.exit(1);
        }
        input = i;
    }

    /**
     * Use {@link Day#input} to process the input, which is a single string, into a problem-specific input.
     * Store this input as a field in the inheriting class.
     */
    public abstract void processInput();

    /**
     * Implementation for part 1. Print the answer {@link System#out}
     */
    public abstract Object part1();

    /**
     * Implementation for part 2. Print the answer {@link System#out}
     */
    public abstract Object part2();

    /**
     * @return the day of the puzzle you are solving
     */
    public abstract int getDay();

    /**
     * @return Whether to use test input (test.txt) instead of regular input (input.txt)
     */
    public boolean isTest() {
        return false;
    }

    /**
     * @return Whether input should be parsed again for Part 2
     */
    public boolean resetForPartTwo() {
        return false;
    }

    public String partOneSolution() {
        return null;
    }

    public String partTwoSolution() {
        return null;
    }

    /**
     * Main method to dynamically run each problem class.
     */
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String c = System.getProperty("sun.java.command");
        Class<?> C = ClassLoader.getSystemClassLoader().loadClass(c);
        Constructor<?> constructor = C.getConstructor();
        Day day = (Day) constructor.newInstance();

        day.processInput();
        System.out.println("================ PART 1 ================");
        Instant now = Instant.now();
        Object part1 = day.part1();
        Duration partOneTime = Duration.between(now, Instant.now());

        if (day.resetForPartTwo()) day.processInput();
        System.out.println("================ PART 2 ================");
        now = Instant.now();
        Object part2 = day.part2();
        Duration partTwoTime = Duration.between(now, Instant.now());

        System.out.format("Solution to part 1: %s (%02d.%04ds)%n", part1, partOneTime.getSeconds(), partOneTime.toMillis());
        System.out.format("Solution to part 2: %s (%02d.%04ds)%n", part2, partTwoTime.getSeconds(), partTwoTime.toMillis());
    }

    public final List<String> lines() {
        return Arrays.asList(input.split("\r?\n"));
    }
}
