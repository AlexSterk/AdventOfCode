package days;

import setup.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day5 extends Day {
    List<Integer> instructions;
    
    @Override
    public void processInput() {
        instructions = Arrays.stream(input.split("\r?\n")).map(Integer::parseInt).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Object part1() {
        List<Integer> instructions = new ArrayList<>(this.instructions);
        
        int pc = 0;
        int steps = 0;

        while (pc < instructions.size()) {
            int old = pc;
            pc += instructions.get(pc);
            instructions.set(old, instructions.get(old) + 1);
            steps++;
        }
        return steps;
    }

    @Override
    public Object part2() {
        List<Integer> instructions = new ArrayList<>(this.instructions);
        
        int pc = 0;
        int steps = 0;
        
        while (pc < instructions.size()) {
            int old = pc;
            pc += instructions.get(pc);
            Integer oldInstr = instructions.get(old);
            instructions.set(old, oldInstr >= 3 ? oldInstr - 1 : oldInstr + 1);
            steps++;
        }
        return steps;
    }

    @Override
    public int getDay() {
        return 5;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "351282";
    }

    @Override
    public String partTwoSolution() {
        return "24568703";
    }
}
