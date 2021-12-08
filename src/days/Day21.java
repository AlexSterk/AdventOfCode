package days;

import setup.Day;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

public class Day21 extends Day {
    private int ip;
    private List<Day16.Instruction> instructions;

    @Override
    public void processInput() {
        String[] lines = input.split("\n");
        ip = Character.getNumericValue(lines[0].charAt(4));
        instructions = Arrays.stream(lines).skip(1).map(Day16.Instruction::stringToInstruction).toList();
    }

    @Override
    public Object part1() {
        long[] registers = new long[6];
        registers[0] = 15615244;
        while (registers[ip] < instructions.size()) {
            Day16.Instruction inst = instructions.get((int) registers[ip]);
//            System.out.println(registers[ip] + " " + inst);
            Day16.Instruction.execute(inst, registers);
            registers[ip]++;
        }
        return registers[0];
    }

    // 15615244

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
}
