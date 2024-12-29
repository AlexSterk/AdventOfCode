from src.setup.day import Day
from src.util.solution import solution

class Day18(Day):
    @property
    def day(self):
        return 18

    @solution("814")
    def part1(self) -> object:
        grid = {(x,y): c for y, line in enumerate(self.input) for x, c in enumerate(line)}

        def neighbours(x, y):
            for p in [(x+1, y), (x-1, y), (x, y+1), (x, y-1), (x+1, y+1), (x-1, y-1), (x+1, y-1), (x-1, y+1)]:
                if p in grid:
                    yield p

        def step():
            n_grid = {}
            for pos, c in grid.items():
                n = sum(grid[p] == "#" for p in neighbours(*pos))
                if c == "#" and n not in [2, 3]:
                    n_grid[pos] = "."
                elif c == "." and n == 3:
                    n_grid[pos] = "#"
                else:
                    n_grid[pos] = c
            return n_grid

        for _ in range(100):
            grid = step()

        return sum(c == "#" for c in grid.values())

    @solution("924")
    def part2(self) -> object:
        grid = {(x,y): c for y, line in enumerate(self.input) for x, c in enumerate(line)}
        grid.update({(0,0): "#", (0,99): "#", (99,0): "#", (99,99): "#"})

        def neighbours(x, y):
            for p in [(x+1, y), (x-1, y), (x, y+1), (x, y-1), (x+1, y+1), (x-1, y-1), (x+1, y-1), (x-1, y+1)]:
                if p in grid:
                    yield p

        def step():
            n_grid = {}
            for pos, c in grid.items():
                if pos in [(0,0), (0,99), (99,0), (99,99)]:
                    n_grid[pos] = "#"
                    continue
                n = sum(grid[p] == "#" for p in neighbours(*pos))
                if c == "#" and n not in [2, 3]:
                    n_grid[pos] = "."
                elif c == "." and n == 3:
                    n_grid[pos] = "#"
                else:
                    n_grid[pos] = c
            return n_grid

        for _ in range(100):
            grid = step()

        return sum(c == "#" for c in grid.values())

# Day18("test").run()
Day18().run()
