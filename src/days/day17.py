import hashlib

from src.setup.day import Day
from src.util.dijkstra import shortest_path
from src.util.solution import solution

grid = """#########
#S| | | #
#-#-#-#-#
# | | | #
#-#-#-#-#
# | | | #
#-#-#-#-#
# | | |  
####### V""".split("\n")


class Day17(Day):
    @property
    def day(self):
        return 17

    @solution("DUDRDLRRRD")
    def part1(self) -> object:
        passcode = self.raw_input

        def get_open_doors(path):
            return hashlib.md5((passcode + path).encode()).hexdigest()[:4]

        def neighbors(node):
            x, y, path = node
            u, d, l, r = get_open_doors(path)
            if u in "bcdef" and grid[y - 1][x] == "-":
                yield x, y - 2, path + "U"
            if d in "bcdef" and grid[y + 1][x] == "-":
                yield x, y + 2, path + "D"
            if l in "bcdef" and grid[y][x - 1] == "|":
                yield x - 2, y, path + "L"
            if r in "bcdef" and grid[y][x + 1] == "|":
                yield x + 2, y, path + "R"

        _, end, _ = shortest_path((1, 1, ""), lambda x: x[0] == 7 and x[1] == 7, neighbors, lambda a, b: 1)

        return end[2]

    @solution("")
    def part2(self) -> object:
        return None


# Day17("test").run()
Day17().run()
