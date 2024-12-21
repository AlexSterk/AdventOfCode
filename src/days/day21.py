import re

from src.setup.day import Day
from src.util.dijkstra import shortest_path
from src.util.solution import solution

actions = {
    "<": (-1, 0),
    ">": (1, 0),
    "^": (0, -1),
    "v": (0, 1),
    "A": (0, 0)
}

opposite = {
    "<": ">",
    ">": "<",
    "^": "v",
    "v": "^",
}

def program(code, keypad):
    start = next((x,y) for (x,y), c in keypad.items() if c == "A")

    def end(node):
        x,y,c1,c2 = node
        return c2 == code

    def neighbors(node):
        x, y, c1,c2 = node
        for d, (dx, dy) in actions.items():
            if len(c1) and opposite.get(d) == c1[-1]:
                continue
            nx, ny = x + dx, y + dy
            if (nx, ny) in keypad:
                if d == "A":
                    if code.startswith(c2 + keypad[nx, ny]):
                        yield nx, ny, c1 + d, c2 + keypad[nx, ny]
                else:
                    yield nx, ny, c1 + d, c2

    cur = (*start, "", "")
    for i, c in enumerate(code):
        def end(node):
            x,y,c1,c2 = node
            return c2 == code[:i+1]

        cur = shortest_path(cur, end, neighbors)[1]
    return cur[2]

class Day21(Day):
    @property
    def day(self):
        return 21

    @solution("")
    def part1(self) -> object:
        keypad_grid = {(x,y): c for y, row in enumerate("789\n546\n123\n 0A".splitlines()) for x, c in enumerate(row)}
        robot_grid = {(x,y): c for y, row in enumerate(" ^A\n<v>".splitlines()) for x, c in enumerate(row)}

        # remove " "
        keypad_grid = {k: v for k, v in keypad_grid.items() if v != " "}
        robot_grid = {k: v for k, v in robot_grid.items() if v != " "}

        print(keypad_grid)
        print(robot_grid)

        total = 0
        for code in self.input:
            t = program(code, keypad_grid)
            print(t)
            t = program(t, robot_grid)
            print(t)
            t = program(t, robot_grid)
            print(t)

            l = len(t)
            n = int(re.sub(r"\D", "", code))
            print(l, n)
            total += l * n
        return total

        # _1 = program(self.input[0], keypad_grid)
        # print(_1)
        # _2 = program(_1, robot_grid)
        # print(_2)
        # _3 = program(_2, robot_grid)
        # print(_3)
        # _4 = program(_3, robot_grid)
        #
        # l = len(_4)

        return _4

    @solution("")
    def part2(self) -> object:
        return None

Day21("test").run()
# Day21().run()
