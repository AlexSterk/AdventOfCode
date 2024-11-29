import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.Direction;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectionTest {

    @ParameterizedTest
    @CsvSource({
            "E, N",
            "N, W",
            "W, S",
            "S, E",
    })
    public void testTurnLeft(Direction d, Direction expected) {
        assertEquals(expected, d.left());

    }

    @ParameterizedTest
    @CsvSource({
            "E, S",
            "S, W",
            "W, N",
            "N, E",
    })
    public void testTurnRight(Direction d, Direction expected) {
        assertEquals(expected, d.right());
    }
}
