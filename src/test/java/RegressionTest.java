import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import setup.Day;
import util.SkipCI;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class RegressionTest {

    private static List<String> days;
    private static boolean CI;


    @BeforeAll
    public static void beforeAll() throws IOException {
        days = Files.walk(Path.of("src/main/java/days"))
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(s -> s.endsWith(".java"))
                .map(s -> s.replaceFirst("\\.java", ""))
                .toList();
        CI = System.getenv("CI") != null;
    }

    public static Stream<Arguments> daysToTest() {
        return Stream.concat(
                days.stream().map(Arguments::of),
                Stream.of(Arguments.of(""))
        );
    }

    @ParameterizedTest
    @MethodSource("daysToTest")
    public void testPartOne(String d) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        assumeFalse(d.isEmpty());
        Day day = getDay(d, 1);
        assumeTrue(day.partOneSolution() != null);
        day.processInput();
        assertEquals(day.partOneSolution(), day.part1().toString());
    }

    private Day getDay(String d, int part) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> C = ClassLoader.getSystemClassLoader().loadClass("days." + d);
        Constructor<?> constructor = C.getConstructor();
        Day day = (Day) constructor.newInstance();
        if (CI) {
            SkipCI annotation = C.getAnnotation(SkipCI.class);
            if (annotation != null) {
                switch (part) {
                    case 1 -> assumeFalse(annotation.part1());
                    case 2 -> assumeFalse(annotation.part2());
                }
            }
        }
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
        assumeFalse(d.isEmpty());
        Day day = getDay(d, 2);
        assumeTrue(day.partTwoSolution() != null);
        day.processInput();
        if (!day.resetForPartTwo()) day.part1();
        assertEquals(day.partTwoSolution(), day.part2().toString());
    }
}
