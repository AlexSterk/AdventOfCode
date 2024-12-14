from src.setup.day import Day
from src.util.solution import solution

class Day18(Day):
    @property
    def day(self):
        return 18

    @solution("1939")
    def part1(self) -> object:
        grid = {(x,y): c for y, row in enumerate(self.input) for x, c in enumerate(row)}
        w = len(grid)

        def next(x, y):
            p1 = grid.get((x-1, y-1), ".")
            p2 = grid.get((x, y-1), ".")
            p3 = grid.get((x+1, y-1), ".")

            if p1 == "^" and p2 == "^" and p3 == ".": return "^"
            if p1 == "." and p2 == "^" and p3 == "^": return "^"
            if p1 == "^" and p2 == "." and p3 == ".": return "^"
            if p1 == "." and p2 == "." and p3 == "^": return "^"
            return "."

        for y in range(1, 40):
            for x in range(w):
                grid[(x,y)] = next(x, y)

        return sum(1 for c in grid.values() if c == ".")

    # Takes about 30 seconds to run on my machine (macbook pro m1)
    # This works but is slow. We can speed it up by detecting a cycle, but I'm too lazy to do that.
    @solution("19999535")
    def part2(self) -> object:
        grid = {(x,y): c for y, row in enumerate(self.input) for x, c in enumerate(row)}
        w = len(grid)

        def next(x, y):
            p1 = grid.get((x-1, y-1), ".")
            p2 = grid.get((x, y-1), ".")
            p3 = grid.get((x+1, y-1), ".")

            if p1 == "^" and p2 == "^" and p3 == ".": return "^"
            if p1 == "." and p2 == "^" and p3 == "^": return "^"
            if p1 == "^" and p2 == "." and p3 == ".": return "^"
            if p1 == "." and p2 == "." and p3 == "^": return "^"
            return "."

        for y in range(1, 400000):
            for x in range(w):
                grid[(x,y)] = next(x, y)

        return sum(1 for c in grid.values() if c == ".")

# Day18("test").run()
Day18().run()
