from src.setup.day import Day
from src.util.solution import solution

class Day6(Day):
    @property
    def day(self):
        return 6

    @solution("4758")
    def part1(self) -> object:
        grid = {(x, y): cell for y, row in enumerate(self.input) for x, cell in enumerate(row)}
        # print(grid)

        # find the ^ cell
        start = next(k for k, v in grid.items() if v == "^")
        visited = set()

        x, y = start
        dir = (0, -1) # up
        while True:
            visited.add((x,y))
            while True:
                nx, ny = x + dir[0], y + dir[1]
                cell = grid.get((nx, ny))
                if cell == "#":
                    # turn 90deg right
                    dir = (-dir[1], dir[0])
                else:
                    x, y = nx, ny
                    break
            if cell is None:
                break
        return len(visited)

    @solution("")
    def part2(self) -> object:
        return None

# Day6("test").run()
Day6().run()
