package days;

import setup.Day;
import util.Grid;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Day8 extends Day {

    private Grid.NonEuclidianGrid<Character> screen;
    private List<Command> commands;

    @Override
    public void processInput() {
        screen = new Grid.NonEuclidianGrid<>(50, 6);
        screen.init(() -> '.', false);

        commands = Arrays.stream(input.split("\n")).map(Command::parse).toList();
    }

    @Override
    public Object part1() {
        for (Command command : commands) {
            if (command instanceof Command.Rect rect) {
                Grid<Character> subgrid = screen.subgrid(0, rect.A() - 1, 0, rect.B() - 1);
                subgrid.getAll().forEach(t -> screen.set(t, '#'));
            }
            else if (command instanceof Command.Rotate rotate) {
                if (rotate.row) screen.shiftRowRight(rotate.i(), rotate.n());
                else screen.shiftColumnDown(rotate.i(), rotate.n());
            }
        }

        return screen.getAll().stream().filter(t -> t.data() == '#').count();
    }

    @Override
    public String partOneSolution() {
        return "123";
    }

    @Override
    public Object part2() {
        System.out.println(screen);
        return null;
    }

    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    private sealed interface Command {
        static Command parse(String string) {
            if (string.startsWith("rect")) {
                String[] xes = string.substring(5).split("x");
                return new Rect(Integer.parseInt(xes[0]),Integer.parseInt(xes[1]));
            }
            boolean row = string.contains("row");
            if (row) {
                String[] split = string.replaceFirst("rotate row y=", "").split(" by ");
                return new Rotate(true, Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            } else {
                String[] split = string.replaceFirst("rotate column x=", "").split(" by ");
                return new Rotate(false, Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            }
        }

        record Rect(int A, int B) implements Command {

        }

        record Rotate(boolean row, int i, int n) implements Command {

        }
    }
}
