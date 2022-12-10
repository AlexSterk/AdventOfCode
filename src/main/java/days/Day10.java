package days;

import setup.Day;
import util.Grid;

public class Day10 extends Day {


    @Override
    public void processInput() {

    }

    @Override
    public Object part1() {
        int X = 1;

        int signalStrength = 0;

        int cycles = 1;

        Integer value = null;

        for (int i = 0; i < lines().size(); i++, cycles++) {
            if ((cycles + 20) % 40 == 0) {
                signalStrength += cycles * X;
            }

            String[] tokens = lines().get(i).split(" ");

            if (value != null) {
                X += value;
                value = null;
            } else if (tokens[0].equals("addx")) {
                value = Integer.parseInt(tokens[1]);
                i--;
            }
        }

        return signalStrength;
    }

    @Override
    public Object part2() {
        Grid<Character> grid = new Grid<>(40, 6);
        grid.init(() -> '.', false);
        int cycles = 1;
        int X = 1;
        boolean wait = false;

        for (int i = 0; i < lines().size(); i++, cycles++) {
            int posX = (cycles - 1) % 40;
            int posY = (cycles - 1) / 40;

            if (posX == X || posX == X - 1 || posX == X + 1) {
                grid.set(posX, posY, '#');
            }

            String[] args = lines().get(i).split(" ");

            if (wait) {
                X += Integer.parseInt(args[1]);
                wait = false;
            } else if (args[0].equals("addx")) {
                wait = true;
                i--;
            }
        }


        return "\n" + grid;
    }

    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "12980";
    }

    @Override
    public String partTwoSolution() {
        return """
                        
                ###..###....##.#....####.#..#.#....###..
                #..#.#..#....#.#....#....#..#.#....#..#.
                ###..#..#....#.#....###..#..#.#....#..#.
                #..#.###.....#.#....#....#..#.#....###..
                #..#.#.#..#..#.#....#....#..#.#....#....
                ###..#..#..##..####.#.....##..####.#....""";
    }
}
