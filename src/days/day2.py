import re

from src.setup.day import Day
from src.util.solution import solution

class Day2(Day):
    @property
    def day(self):
        return 2

    def parsed(self):
        return [list(map(int, x.split("-"))) for x in self.raw_input.split(",")]

    @solution("38437576669")
    def part1(self) -> object:
        pattern = re.compile(r"^(\d+)\1$")
        ranges = self.parsed()
        invalids = []
        for x, y in ranges:
            for i in range(x,y+1):
                if re.match(pattern, str(i)):
                    invalids.append(i)

        return sum(invalids)

    @solution("49046150754")
    def part2(self) -> object:
        pattern = re.compile(r"^(\d+)\1+$")
        ranges = self.parsed()
        invalids = []
        for x, y in ranges:
            for i in range(x,y+1):
                if re.match(pattern, str(i)):
                    invalids.append(i)

        return sum(invalids)

# Day2("test").run()
Day2().run()