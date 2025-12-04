from collections import defaultdict

from src.setup.day import Day
from src.util.solution import solution
import src.util.directions as dir

directions = [*dir.cardinal.values(), *dir.diagonal.values()]

def parse_grid(lines):
    grid = set()
    for y, line in enumerate(lines):
        for x, c in enumerate(line):
            if c == '@':
                grid.add((x, y))
    return grid

class Day4(Day):
    @property
    def day(self):
        return 4

    @solution("1370")
    def part1(self) -> object:
        grid = parse_grid(self.read_input())
        removed = self.round(grid)
        return len(removed)

    @solution("8437")
    def part2(self) -> object:
        grid = parse_grid(self.read_input())
        orig = len(grid)
        while True:
            removed = self.round(grid)
            grid = grid.difference(removed)
            if len(removed) == 0:
                return orig - len(grid)

    @staticmethod
    def round(grid):
        removed = set()
        for (x, y) in grid:
            ns = sum((x+dx, y+dy) in grid for (dx, dy) in directions)
            if ns < 4:
                removed.add((x, y))
        return removed


# Day4("test").run()
Day4().run()