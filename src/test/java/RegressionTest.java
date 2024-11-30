import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import setup.Day;

import static util.Annotations.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

public class RegressionTest {

    private static List<String> days;
    private static boolean CI;

    @BeforeAll
    public static void beforeAll() throws IOException {
        days = Files.list(Path.of("src/main/java/days"))
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(s -> s.endsWith(".java"))
                .map(s -> s.replaceFirst("\\.java", ""))
                .toList();
        CI = System.getenv("CI") != null;
        Day.testEnvironment = true;
    }

    public static Stream<String> daysToTest() {
        return days.stream();
    }

    private Day getDay(String d, int part) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> C = Class.forName("days." + d);
        Constructor<?> constructor = C.getConstructor();
        Day day = (Day) constructor.newInstance();
        if (part == 2) day.setPart2();

        if (CI) {
            SkipCI annotation = C.getAnnotation(SkipCI.class);
            if (annotation != null) {
                switch (part) {
                    case 1 -> assumeFalse(annotation.part1());
                    case 2 -> assumeFalse(annotation.part2());
                }
            }
        }
        return day;
    }

    @ParameterizedTest
    @MethodSource("daysToTest")
    public void testPartOne(String d) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Day day = getDay(d, 1);

        Solution annotation = day.getClass().getDeclaredMethod("part1").getDeclaredAnnotation(Solution.class);
        assumeFalse(annotation == null);
        assumeFalse(annotation.value().isEmpty());
        day.processInput();
        assertEquals(annotation.value(), day.part1().toString());
    }

    @ParameterizedTest
    @MethodSource("daysToTest")
    public void testPartTwo(String d) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Day day = getDay(d, 2);

        Solution annotation = day.getClass().getDeclaredMethod("part2").getDeclaredAnnotation(Solution.class);
        assumeFalse(annotation == null);
        assumeFalse(annotation.value().isEmpty());
        day.processInput();
        if (!day.resetForPartTwo()) day.part1();
        assertEquals(annotation.value(), day.part2().toString());
    }
}
