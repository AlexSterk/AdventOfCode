from src.setup.day import Day
from src.util.solution import solution


def simulate_guard(dir, g, total, visited, x, y):
    while True:
        if (x, y, dir) in visited:
            total += 1
            break
        visited.add((x, y, dir))
        while True:
            nx, ny = x + dir[0], y + dir[1]
            cell = g.get((nx, ny))
            if cell == "#":
                # turn 90deg right
                dir = (-dir[1], dir[0])
            else:
                x, y = nx, ny
                break
        if cell is None:
            break
    return total


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
        dir = (0, -1)  # up
        simulate_guard(dir, grid, 0, visited, x, y)
        visited = set([(x, y) for x, y, _ in visited])
        self.grid = grid
        self.possible_objects = visited
        self.start = start
        return len(visited)

    @solution("1670")
    def part2(self) -> object:
        grid, possible_objects, start = self.grid, self.possible_objects, self.start

        total = 0
        for x, y in possible_objects:
            g = grid.copy()
            g[(x, y)] = "#"

            visited = set()
            x, y = start
            dir = (0, -1)  # up

            total = simulate_guard(dir, g, total, visited, x, y)
        return total


# Day6("test").run()
Day6().run()
