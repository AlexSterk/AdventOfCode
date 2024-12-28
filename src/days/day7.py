from collections import defaultdict

from src.setup.day import Day
from src.util.solution import solution

class Day7(Day):
    wires = {}

    @property
    def day(self):
        return 7

    @solution("16076")
    def part1(self) -> object:
        instructions = [line.split() for line in self.input]
        instructions = {line[-1]: line[:-2] for line in instructions}

        def eval(wire):
            if wire.isnumeric():
                return int(wire)
            if wire in self.wires:
                return self.wires[wire]
            if wire in instructions:
                instruction = instructions[wire]
                if len(instruction) == 1:
                    value = eval(instruction[0])
                elif instruction[0] == "NOT":
                    value = ~eval(instruction[1])
                elif instruction[1] == "AND":
                    value = eval(instruction[0]) & eval(instruction[2])
                elif instruction[1] == "OR":
                    value = eval(instruction[0]) | eval(instruction[2])
                elif instruction[1] == "LSHIFT":
                    value = eval(instruction[0]) << eval(instruction[2])
                elif instruction[1] == "RSHIFT":
                    value = eval(instruction[0]) >> eval(instruction[2])
                self.wires[wire] = value
                return value


        self.eval = eval
        return eval("a")

    @solution("2797")
    def part2(self) -> object:
        self.wires = {"b": self.wires["a"]}
        return self.eval("a")


# Day7("test").run()
Day7().run()
