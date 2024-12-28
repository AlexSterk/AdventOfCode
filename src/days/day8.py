from src.setup.day import Day
from src.util.solution import solution

class Day8(Day):
    @property
    def day(self):
        return 8

    @solution("1342")
    def part1(self) -> object:

        total = 0
        for line in self.input:
            total += len(line) - len(eval(line))

        return total

    @solution("2074")
    def part2(self) -> object:
        def encode(string):
            return '"' + string.replace("\\", "\\\\").replace('"', '\\"') + '"'

        total = 0

        for line in self.input:
            total += len(encode(line)) - len(line)

        return total

# Day8("test").run()
Day8().run()
