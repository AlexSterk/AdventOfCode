import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import setup.Day;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class RegressionTest {

    private static List<String> days;

    @BeforeAll
    public static void beforeAll() throws IOException {
        days = Files.walk(Path.of("src/main/java/days"))
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(s -> s.endsWith(".java"))
                .map(s -> s.replaceFirst("\\.java", ""))
                .toList();
    }

    public static Stream<Arguments> daysToTest() {
        return days.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("daysToTest")
    public void testPartOne(String d) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Day day = getDay(d);
        Assumptions.assumeTrue(day.partOneSolution() != null);
        day.processInput();
        Assertions.assertEquals(day.partOneSolution(), day.part1().toString());
    }

    private Day getDay(String d) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> C = ClassLoader.getSystemClassLoader().loadClass("days." + d);
        Constructor<?> constructor = C.getConstructor();
        Day day = (Day) constructor.newInstance();
        return new Day() {
            @Override
            public void processInput() {
                day.processInput();
            }

            @Override
            public Object part1() {
                return day.part1();
            }

            @Override
            public Object part2() {
                return day.part2();
            }

            @Override
            public int getDay() {
                return day.getDay();
            }

            @Override
            public boolean isTest() {
                return false;
            }

            @Override
            public boolean resetForPartTwo() {
                return day.resetForPartTwo();
            }

            @Override
            public String partOneSolution() {
                return day.partOneSolution();
            }

            @Override
            public String partTwoSolution() {
                return day.partTwoSolution();
            }
        };
    }

    @ParameterizedTest
    @MethodSource("daysToTest")
    public void testPartTwo(String d) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Day day = getDay(d);
        Assumptions.assumeTrue(day.partTwoSolution() != null);
        day.processInput();
        if (!day.resetForPartTwo()) day.part1();
        Assertions.assertEquals(day.partTwoSolution(), day.part2().toString());
    }
}
