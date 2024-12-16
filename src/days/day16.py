from src.setup.day import Day
from src.util.dijkstra import shortest_path
from src.util.directions import cardinal
from src.util.solution import solution

class Day16(Day):
    @property
    def day(self):
        return 16

    @solution("105508")
    def part1(self) -> object:
        grid = {(x,y): c for y, row in enumerate(self.input) for x, c in enumerate(row)}
        start = next(k for k, v in grid.items() if v == 'S')
        end = next(k for k, v in grid.items() if v == 'E')

        start_state = (start, (cardinal['E']))
        def is_end(state):
            return state[0] == end

        def ns(state):
            (x,y), (dx,dy) = state
            if (n:=(x + dx, y + dy)) in grid and grid[n] != '#':
                yield n, (dx, dy)
            for d in cardinal.values():
                if d == (dx, dy) or d == (-dx, -dy):
                    continue
                yield (x, y), d


        def cost(state, n_state):
            return 1 if state[1] == n_state[1] else 1000


        return shortest_path(start_state, is_end, ns, cost)[2]

    @solution("")
    def part2(self) -> object:
        return None

# Day16("test").run()
Day16().run()
