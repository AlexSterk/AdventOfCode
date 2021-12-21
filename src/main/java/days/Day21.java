package days;

import setup.Day;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Day21 extends Day {
    private int playerOnePosition;
    private int playerTwoPosition;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        playerOnePosition = Integer.parseInt(lines[0].substring(28));
        playerTwoPosition = Integer.parseInt(lines[1].substring(28));
    }

    @Override
    public Object part1() {
        int playerOneScore = 0;
        int playerTwoScore = 0;
        Die die = new Die();

        while (true) {
            int r = die.next() + die.next() + die.next();
            playerOnePosition = (playerOnePosition - 1 + r) % 10 + 1;
            playerOneScore += playerOnePosition;
            if (playerOneScore >= 1000) {
                return die.rolls * playerTwoScore;
            }


            r = die.next() + die.next() + die.next();
            playerTwoPosition = (playerTwoPosition - 1 + r) % 10 + 1;
            playerTwoScore += playerTwoPosition;
            if (playerTwoScore >= 1000) return die.rolls * playerOneScore;
        }
    }

    @Override
    public boolean resetForPartTwo() {
        return true;
    }

    @Override
    public Object part2() {
        Map<Integer, Integer> diceCounts = new HashMap<>();
        for (int i = 1; i <= 3; i++)
            for (int k = 1; k <= 3; k++)
                for (int j = 1; j <= 3; j++) {
                    diceCounts.merge(i + k + j, 1, Integer::sum);
                }

        Map<Game, BigDecimal> games = new HashMap<>();
        games.put(new Game(playerOnePosition, 0, playerTwoPosition, 0, 1), BigDecimal.ONE);

        while (games.keySet().stream().anyMatch(g -> !g.gameOver())) {
            Map<Game, BigDecimal> newGames = new HashMap<>();
            games.forEach((g, cur) -> {
                if (g.gameOver()) {
                    newGames.merge(g, cur, BigDecimal::add);
                } else
                    diceCounts.forEach((d, c) -> newGames.merge(g.turn(d), cur.multiply(BigDecimal.valueOf(c)), BigDecimal::add));
            });
            games = newGames;
        }

        return Long.max(
                games.keySet().stream().filter(g -> g.p1Score >= 21).map(games::get).mapToLong(BigDecimal::longValue).sum(),
                games.keySet().stream().filter(g -> g.p2Score >= 21).map(games::get).mapToLong(BigDecimal::longValue).sum()
        );
    }

    @Override
    public int getDay() {
        return 21;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "503478";
    }

    @Override
    public String partTwoSolution() {
        return "716241959649754";
    }

    private static class Die {
        private int prev = 99;
        private int rolls;

        public int next() {
            rolls++;
            prev = (prev + 1) % 100;
            return prev + 1;
        }
    }

    private record Game(int p1Pos, int p1Score, int p2Pos, int p2Score, int whoseTurn) {
        private boolean gameOver() {
            return p1Score >= 21 || p2Score >= 21;
        }

        private Game turn(int dice) {
            if (whoseTurn == 1) {
                int pos = (this.p1Pos - 1 + dice) % 10 + 1;
                return new Game(pos, p1Score + pos, p2Pos, p2Score, 2);
            } else {
                int pos = (this.p2Pos - 1 + dice) % 10 + 1;
                return new Game(p1Pos, p1Score, pos, p2Score + pos, 1);
            }
        }
    }
}
