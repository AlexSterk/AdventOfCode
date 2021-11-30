package setup;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

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

    /**
     * Main method to dynamically run each problem class.
     */
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String c = System.getProperty("sun.java.command");
        Class<?> C = ClassLoader.getSystemClassLoader().loadClass(c);
        Constructor<?> constructor = C.getConstructor();
        Day day = (Day) constructor.newInstance();

        day.processInput();
        Instant now = Instant.now();
        System.out.println("Solution to part 1: " + day.part1());
        Duration partOneTime = Duration.between(now, Instant.now());
        System.out.format("Solving part 1 took %02d.%04d seconds", partOneTime.getSeconds(), partOneTime.toMillis());

        if (day.resetForPartTwo()) day.processInput();
        now = Instant.now();
        System.out.println("Solution to part 2: " + day.part2());
        Duration partTwoTime = Duration.between(now, Instant.now());
        System.out.format("Solving part 2 took %02d.%04d seconds", partTwoTime.getSeconds(), partTwoTime.toMillis());
    }
}
