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
    public Object part1() {
        while (recipes.length() < numRecipes + 10) {
            createRecipes();
        }
        return recipes.substring(numRecipes, numRecipes + 10);
    }

    private void createRecipes() {
        int sum = Character.getNumericValue(recipes.charAt(elves[0])) + Character.getNumericValue(recipes.charAt(elves[1]));
        recipes.append(sum);
        for (int i = 0; i < elves.length; i++) {
            int currentRecipeIndex = elves[i];
            int currentRecipeScore = Character.getNumericValue(recipes.charAt(currentRecipeIndex));
            elves[i] = (currentRecipeIndex + 1 + currentRecipeScore) % recipes.length();
        }
    }

    @Override
    public Object part2() {
        for (int i = 0; i < 30_000_000; i++) {
            createRecipes();
        }
        return recipes.indexOf(numRecipes.toString());
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
