package days;

import setup.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static days.Day18.*;
import static days.Day18.DuetCPU.*;

public class Day23 extends Day {

    private List<Instruction> instructions;

    @Override
    public void processInput() {
        instructions = Arrays.stream(input.split("\n")).map(Instruction::Instruction).toList();
    }

    @Override
    public Object part1() {
        DuetCPU cpu = new DuetCPU(instructions);
        cpu.run();
        return cpu.mulCount;
    }

    @Override
    public Object part2() {
        /*   LOOP one
        set g d
        mul g e
        sub g b
        jnz g 2
        set f 0
        sub e -1
        set g e
        sub g b
        jnz g -8

        so:

        do:
            g = d                   g = d * e - b
            g *= e
            g -= b
            if g == 0:              if g == 0:
                f = 0                   f = 0
            e -= 1                  e += 1
            g = e                   g = e - b                  loop breaks if e == b so needs to run  b - e times to get there
            g -= b
        while g == 0



        Loop two:

        ...
        sub d -1
        set g d
        sub g b
        jnz g -13

        ...
        d += 1
        g = d
        g -= b             g = d - b                       d - b must be 0 so d must be b
        while g != 0


        d should be 0 so it should run the loop long enough to overflow --> Long.MAX_VALUE * 2 - d times




         f should be 0 so d * e - b should be 0
         so d * e should be "b"
         */
        DuetCPU cpu = new DuetCPU(instructions);
        cpu.registers.put('a', 1L);
        cpu.runUntil(c -> c.ir == 11);
        cpu.registers.put('e', cpu.registers.get('b') - 1);
        cpu.runUntil(c -> c.ir == 21);
        cpu.registers.put('d', cpu.registers.get('b'));
        cpu.run();
        return cpu.registers.get('h');
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
