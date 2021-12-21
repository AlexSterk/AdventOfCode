package days;

import setup.Day;

public class Day21 extends Day {
    private int playerOnePosition, playerTwoPosition, playerOneScore, playerTwoScore;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        playerOnePosition = Integer.parseInt(lines[0].substring(28));
        playerTwoPosition = Integer.parseInt(lines[1].substring(28));

        playerOneScore = 0; playerTwoScore = 0;
    }

    @Override
    public Object part1() {
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
    public Object part2() {
        return null;
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

    private static class Die {
        private int prev = 99;
        private int rolls;

        public int next() {
            rolls++;
            prev = (prev + 1) % 100;
            return prev + 1;
        }
    }
}
