from collections import defaultdict
from multiprocessing.reduction import register

from src.setup.day import Day
from src.util.solution import solution

class Day23(Day):
    @property
    def day(self):
        return 23

    @solution("184")
    def part1(self) -> object:
        registers = defaultdict(int)
        return self.run_cpu(registers)

    def run_cpu(self, registers):
        pc = 0
        while pc < len(self.input):
            instr = self.input[pc]
            op, args = instr.split(" ", 1)
            args = args.split(", ")

            if op == "hlf":
                registers[args[0]] //= 2
            elif op == "tpl":
                registers[args[0]] *= 3
            elif op == "inc":
                registers[args[0]] += 1
            elif op == "jmp":
                pc += int(args[0])
                continue
            elif op == "jie":
                if registers[args[0]] % 2 == 0:
                    pc += int(args[1])
                    continue
            elif op == "jio":
                if registers[args[0]] == 1:
                    pc += int(args[1])
                    continue
            pc += 1
        return registers["b"]

    @solution("231")
    def part2(self) -> object:
        registers = defaultdict(int)
        registers["a"] = 1
        return self.run_cpu(registers)


# Day23("test").run()
Day23().run()
