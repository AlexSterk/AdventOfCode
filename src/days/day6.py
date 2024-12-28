import re

from src.setup.day import Day
from src.util.solution import solution

class Day6(Day):
    @property
    def day(self):
        return 6

    @solution("377891")
    def part1(self) -> object:
        grid = {(x, y): False for x in range(1000) for y in range(1000)}

        for line in self.input:
            nums = re.findall(r"\d+", line)
            x1, y1, x2, y2 = map(int, nums)
            action = re.search(r"on|off|toggle", line).group()
            for x in range(x1, x2 + 1):
                for y in range(y1, y2 + 1):
                    if action == "on":
                        grid[(x, y)] = True
                    elif action == "off":
                        grid[(x, y)] = False
                    else:
                        grid[(x, y)] = not grid[(x, y)]
        return sum(grid.values())


    @solution("14110788")
    def part2(self) -> object:
        grid = {(x, y): 0 for x in range(1000) for y in range(1000)}

        for line in self.input:
            nums = re.findall(r"\d+", line)
            x1, y1, x2, y2 = map(int, nums)
            action = re.search(r"on|off|toggle", line).group()
            for x in range(x1, x2 + 1):
                for y in range(y1, y2 + 1):
                    if action == "on":
                        grid[(x, y)] += 1
                    elif action == "off":
                        grid[(x, y)] = max(0, grid[(x, y)] - 1)
                    else:
                        grid[(x, y)] += 2

        return sum(grid.values())

# Day6("test").run()
Day6().run()
