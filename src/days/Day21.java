package days;

import setup.Day;

import java.util.ArrayList;
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
            Day16.Instruction.execute(inst, registers);
            registers[ip]++;
        }
        return registers[0];
    }

    // 15615244

    @Override
    public Object part2() {
        long[] registers = new long[6];
        registers[0] = -1;
        List<Long> valuesOfR5 = new ArrayList<>();
        while (registers[ip] < instructions.size()) {
            if (registers[ip] == 28) {
                long R5 = registers[5];
                if (valuesOfR5.contains(R5))
                    return valuesOfR5.get(valuesOfR5.size() - 1);
                else valuesOfR5.add(R5);
            }
            Day16.Instruction inst = instructions.get((int) registers[ip]);
            Day16.Instruction.execute(inst, registers);
            registers[ip]++;
        }
        return registers[0];
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
