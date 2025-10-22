import re

from src.setup.day import Day
from src.util.solution import solution

class Day25(Day):
    @property
    def day(self):
        return 25

    @solution("19980801")
    def part1(self) -> object:

        def determine_index(row, column):
            index = 1
            for i in range(1, row + column - 1):
                index += i
            index += column - 1
            return index

        def determine_code(index):
            code = 20151125
            for _ in range(1, index):
                code = code * 252533 % 33554393
            return code

        row, column = re.findall(r"(\d+)", self.raw_input)
        row, column = int(row), int(column)

        return determine_code(determine_index(row, column))

    @solution("")
    def part2(self) -> object:
        return None

# Day25("test").run()
Day25().run()
