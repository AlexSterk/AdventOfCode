import re

from src.setup.day import Day
from src.util.solution import solution


class Day9(Day):
    @property
    def day(self):
        return 9

    @solution("")
    def part1(self) -> object:
        s = self.raw_input

        o = ""
        file = True
        id = 0
        for d in s:
            d = int(d)
            if file:
                o += str(id) * d
                id += 1
            else:
                o += str(".") * d
            file = not file

        print(o)

        o = list(o)


        print(len(o))

        for i in range(len(o) - 1, -1, -1):
            print(i)
            d = o[i]
            if d == ".":
                continue
            else:
                j = o.index(".")
                if j < i:
                    o[i] = "."
                    o[j] = d
        o = "".join(o)
        print(o)

        total = 0
        for i, d in enumerate(o):
            if d == ".":
                continue
            else:
                n = int(d)
                total += n * i
        return total

    @solution("")
    def part2(self) -> object:
        return None


# Day9("test").run()
Day9().run()
