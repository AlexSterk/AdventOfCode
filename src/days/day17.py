import re

from src.setup.day import Day
from src.util.solution import solution

class Day17(Day):
    @property
    def day(self):
        return 17

    @solution("7,3,1,3,6,3,6,0,2")
    def part1(self) -> object:
        registers, program = self.raw_input.split("\n\n")
        p = r"Register (.+): (\d+)"
        registers = {k: int(v) for k, v in re.findall(p, registers)}
        program = list(map(int, program.replace("Program: ", "").split(",")))
        print(registers, program)

        def val(v):
            if v <= 3:
                return v
            return registers[{4: 'A', 5: 'B', 6: 'C'}[v]]

        output = []

        def run(program):
            pc = 0
            while 0 <= pc < len(program):
                opcode = program[pc]
                lit = program[pc + 1]
                inp = val(lit)

                if opcode == 0:
                    registers['A'] = registers['A'] // (2 ** inp)
                elif opcode == 1:
                    registers['B'] = registers['B'] ^ lit
                elif opcode == 2:
                    registers['B'] = inp % 8
                elif opcode == 3:
                    if registers['A'] != 0:
                        pc = lit
                        continue
                elif opcode == 4:
                    registers['B'] = registers['B'] ^ registers['C']
                elif opcode == 5:
                    output.append(inp % 8)
                elif opcode == 6:
                    registers['B'] = registers['A'] // 2 ** inp
                elif opcode == 7:
                    registers['C'] = registers['A'] // 2 ** inp

                pc += 2
        run(program)

        return ",".join(map(str, output))

    @solution("")
    def part2(self) -> object:
        return None

# Day17("test").run()
Day17().run()