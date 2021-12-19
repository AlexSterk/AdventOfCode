package days;

import setup.Day;

import java.util.Arrays;
import java.util.List;

public class Day23 extends Day {

    private List<Day18.DuetCPU.Instruction> instructions;

    @Override
    public void processInput() {
        instructions = Arrays.stream(input.split("\n")).map(Day18.DuetCPU.Instruction::Instruction).toList();
    }

    @Override
    public Object part1() {
        Day18.DuetCPU cpu = new Day18.DuetCPU(instructions, null);
        cpu.run();
        return cpu.mulCount;
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 23;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "8281";
    }
}
