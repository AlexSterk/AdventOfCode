package days;

import setup.Day;
import util.Grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Day4 extends Day {
    private List<Integer> numbers;
    private List<Grid<BingoNumber>> boards;

    @Override
    public void processInput() {
        String[] parts = input.split("\n\n");
        numbers = Arrays.stream(parts[0].split(",")).map(Integer::parseInt).toList();
        boards = Arrays.stream(parts)
                .skip(1)
                .map(s -> {
                    Grid<BingoNumber> b = new Grid<>(5, 5);
                    String[] lines = s.split("\n");
                    for (int y = 0; y < lines.length; y++) {
                        String[] line = lines[y].trim().split(" +");
                        for (int x = 0; x < line.length; x++) {
                            b.set(x, y, new BingoNumber(Integer.parseInt(line[x])));
                        }
                    }
                    return b;
                })
                .toList();
    }

    @Override
    public Object part1() {
        for (Integer number : numbers) {
            for (Grid<BingoNumber> board : boards) {
                Optional<Grid.Tile<BingoNumber>> first = board.getAll().stream().filter(t -> t.data().number.equals(number)).findFirst();
                first.ifPresent(t -> board.set(t, new BingoNumber(number, true)));
                if (winningBoard(board)) {
                    System.out.println(board);
                    int unmarkedSum = board.getAll().stream().filter(t -> !t.data().marked()).mapToInt(t -> t.data().number).sum();
                    return unmarkedSum * number;
                }
            }
        }
        throw new RuntimeException("No winning board found");
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public Object part2() {
        List<Grid<BingoNumber>> winningBoards = new ArrayList<>();

        for (Integer number : numbers) {
            for (Grid<BingoNumber> board : boards) {
                Optional<Grid.Tile<BingoNumber>> first = board.getAll().stream().filter(t -> t.data().number.equals(number)).findFirst();
                first.ifPresent(t -> board.set(t, new BingoNumber(number, true)));
                if (winningBoard(board) && !winningBoards.contains(board)) {
                    winningBoards.add(board);
                }
            }
            if (winningBoards.size() == boards.size()) {
                var lastWinning = winningBoards.get(winningBoards.size() - 1);
                System.out.println(lastWinning);
                int unmarkedSum = lastWinning.getAll().stream().filter(t -> !t.data().marked()).mapToInt(t -> t.data().number).sum();
                return unmarkedSum * number;
            }
        }
        throw new RuntimeException("Not every board wins");
    }

    private static boolean winningBoard(Grid<BingoNumber> board) {
        return Stream.concat(board.getRows().stream(), board.getColumns().stream())
                .anyMatch(r -> r.stream().allMatch(t -> t.data().marked));
    }

    @Override
    public int getDay() {
        return 4;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "11536";
    }

    @Override
    public String partTwoSolution() {
        return "1284";
    }

    private record BingoNumber(Integer number, boolean marked) {
        public BingoNumber(Integer number) {
            this(number, false);
        }

        @Override
        public String toString() {
            String n = (number < 10 ? " " : "") + number + " ";

            if (marked) return "\033[31m" + n + "\033[0m";
            else return n;
        }
    }
}
