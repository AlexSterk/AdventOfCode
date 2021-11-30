package days;

import setup.Day;

public class Day14 extends Day {

    private int[] elves;
    private StringBuilder recipes;

    private Integer numRecipes;

    @Override
    public void processInput() {
        recipes = new StringBuilder("37");
        elves = new int[]{0, 1};
        numRecipes = Integer.parseInt(input.trim());
    }

    @Override
    public void part1() {
        while (recipes.length() < numRecipes + 10) {
            createRecipes();
        }
        System.out.println(recipes.substring(numRecipes, numRecipes + 10));
    }

    private void createRecipes() {
        int sum = Character.getNumericValue(recipes.charAt(elves[0])) + Character.getNumericValue(recipes.charAt(elves[1]));
        recipes.append(sum);
        for (int i = 0; i < elves.length; i++) {
            Integer currentRecipeIndex = elves[i];
            Integer currentRecipeScore = Character.getNumericValue(recipes.charAt(currentRecipeIndex));
            elves[i] = (currentRecipeIndex + 1 + currentRecipeScore) % recipes.length();
        }
    }

    @Override
    public void part2() {
        for (int i = 0; i < 30_000_000; i++) {
            createRecipes();
        }
        System.out.println(recipes.indexOf(numRecipes.toString()));
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
