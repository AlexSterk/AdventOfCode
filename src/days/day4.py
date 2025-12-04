from collections import defaultdict

from src.setup.day import Day
from src.util.solution import solution
import src.util.directions as dir

directions = [*dir.cardinal.values(), *dir.diagonal.values()]
print(directions)

def parse_grid(lines):
    grid = {}
    for y, line in enumerate(lines):
        for x, c in enumerate(line):
            grid[x, y] = c
    return grid

class Day4(Day):
    @property
    def day(self):
        return 4

    @solution("1370")
    def part1(self) -> object:
        c = 0
        grid = parse_grid(self.read_input())
        for (x,y), v in grid.items():
            if v == '@':
                ns = sum(grid.get((x+dx,y+dy)) == '@' for (dx, dy) in directions)
                if ns < 4:
                    c += 1
        return c

    @solution("8437")
    def part2(self) -> object:
        c = 0
        grid = parse_grid(self.read_input())
        while True:
            n_grid = grid.copy()
            can_be_removed = 0
            for (x,y), v in grid.items():
                if v == '@':
                    ns = sum(grid.get((x + dx, y + dy)) == '@' for (dx, dy) in directions)
                    if ns < 4:
                        can_be_removed += 1
                        n_grid[x, y] = None
            if can_be_removed == 0:
                break
            c += can_be_removed
            grid = n_grid
        return c

# Day4("test").run()
Day4().run()