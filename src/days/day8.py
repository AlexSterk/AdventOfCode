import itertools
from collections import deque

from src.setup.day import Day
from src.util.solution import solution


class Day8(Day):
    @property
    def day(self):
        return 8

    @solution("301")
    def part1(self) -> object:
        grid = {(x, y): cell for y, row in enumerate(self.input) for x, cell in enumerate(row)}
        # get unique values
        unique_values = set(grid.values())
        unique_values.remove(".")
        antinodes = set()
        for value in unique_values:
            # get all keys with that value
            keys = [key for key, val in grid.items() if val == value]
            for (x1, y1), (x2, y2) in itertools.combinations(keys, 2):
                dx, dy = x2 - x1, y2 - y1
                p3 = (x1 - dx, y1 - dy)
                p4 = (x2 + dx, y2 + dy)
                if p3 in grid:
                    antinodes.add(p3)
                if p4 in grid:
                    antinodes.add(p4)
        return len(antinodes)

    @solution("1019")
    def part2(self) -> object:
        grid = {(x, y): cell for y, row in enumerate(self.input) for x, cell in enumerate(row)}
        # get unique values
        unique_values = set(grid.values())
        unique_values.remove(".")
        antinodes = set()
        for value in unique_values:
            # get all keys with that value
            keys = [key for key, val in grid.items() if val == value]
            for p1, p2 in itertools.combinations(keys, 2):
                (x1, y1), (x2, y2) = p1, p2
                dx, dy = x2 - x1, y2 - y1
                q = deque()
                v = set()
                q.append((x1, y1, -1)) # search in both directions
                q.append((x2, y2, 1))
                while q:
                    x, y, d = q.pop()
                    if (x, y) in v:
                        continue
                    v.add((x, y))
                    p = x, y = (x + dx * d, y + dy * d)
                    if p in grid:
                        antinodes.add(p)
                        q.append((x, y, d))
                antinodes.add(p1)
                antinodes.add(p2)
        return len(antinodes)


# Day8("test").run()
Day8().run()
