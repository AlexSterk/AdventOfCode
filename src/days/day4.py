from collections import defaultdict

from src.setup.day import Day
from src.util.solution import solution
import src.util.directions as dir

directions = [*dir.cardinal.values(), *dir.diagonal.values()]

def parse_grid(lines):
    rolls = set()
    for y, line in enumerate(lines):
        for x, c in enumerate(line):
            if c == '@':
                rolls.add((x, y))
    return rolls

class Day4(Day):
    @property
    def day(self):
        return 4

    @solution("1370")
    def part1(self) -> object:
        rolls = parse_grid(self.read_input())
        removed = self.round(rolls)
        return len(removed)

    @solution("8437")
    def part2(self) -> object:
        rolls = parse_grid(self.read_input())
        orig = len(rolls)
        while True:
            removed = self.round(rolls)
            rolls = rolls.difference(removed)
            if len(removed) == 0:
                return orig - len(rolls)

    @staticmethod
    def round(rolls):
        removed = set()
        for (x, y) in rolls:
            ns = sum((x+dx, y+dy) in rolls for (dx, dy) in directions)
            if ns < 4:
                removed.add((x, y))
        return removed


# Day4("test").run()
Day4().run()