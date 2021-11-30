package days;

import setup.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day14 extends Day {

    private List<Integer> elves;
    private List<Integer> recipes;

    private int numRecipes;

    @Override
    public void processInput() {
        recipes = new ArrayList<>(List.of(3, 7));
        elves = new ArrayList<>(List.of(0, 1));
        numRecipes = Integer.parseInt(input.trim());
    }

    @Override
    public void part1() {
        while (recipes.size() < numRecipes + 10) {
            int sum = recipes.get(elves.get(0)) + recipes.get(elves.get(1));
            List<Integer> digits = Arrays.stream(Integer.toString(sum).split("")).map(Integer::parseInt).collect(Collectors.toList());
            recipes.addAll(digits);
            for (int i = 0; i < elves.size(); i++) {
                Integer currentRecipeIndex = elves.get(i);
                Integer currentRecipeScore = recipes.get(currentRecipeIndex);
                elves.set(i, (currentRecipeIndex + 1 + currentRecipeScore) % recipes.size());
            }
        }
        recipes.subList(numRecipes, numRecipes + 10).forEach(System.out::print);
        System.out.println();
    }

    @Override
    public void part2() {

    }

    @Override
    public int getDay() {
        return 14;
    }

    @Override
    public boolean isTest() {
        return false;
    }
}
