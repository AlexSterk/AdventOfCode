from collections import deque

from src.setup.day import Day
from src.util.solution import solution

class Day4(Day):
    @property
    def day(self):
        return 4

    @solution("2654")
    def part1(self) -> object:
        grid = [list(l.strip()) for l in self.input]
        print(grid)

        dirs = [(x, y) for x in range(-1, 2) for y in range(-1, 2) if x !=0 or y != 0]
        queue = deque()
        seen = set()
        for y, row in enumerate(grid):
            for x, cell in enumerate(row):
                if cell == "X":
                    for d in dirs:
                        queue.append((x, y, cell, d))

        total = 0
        while queue:
            x, y, s, d = queue.popleft()
            if (x, y, d) in seen:
                continue
            seen.add((x, y, d))
            if s == "XMAS":
                total += 1
                continue

            nx, ny = x+d[0], y+d[1]
            if nx < 0 or nx >= len(grid[0]) or ny < 0 or ny >= len(grid):
                continue
            ns = s + grid[ny][nx]
            if ns in "XMAS":
                queue.append((nx, ny, ns, d))

        return total

    @solution("")
    def part2(self) -> object:
        return None

# Day4("test").run()
Day4().run()