import re

from src.setup.day import Day
from src.util.solution import solution


class Day9(Day):
    @property
    def day(self):
        return 9

    @solution("6353658451014")
    def part1(self) -> object:
        s = self.raw_input

        id = 0
        disk = []
        for i, d in enumerate(s):
            if i % 2 == 0:
                for j in range(int(d)):
                    disk.append(id)
                id += 1
            else:
                for j in range(int(d)):
                    disk.append(None)

        i = 0
        l = len(disk) - 1
        while i < l:
            if disk[i] is not None:
                i+=1
                continue
            if disk[l] is None:
                l -= 1
                continue
            disk[i], disk[l] = disk[l], disk[i]
            i += 1
            l -= 1

        total = 0
        disk = disk[0:disk.index(None)]
        for i, n in enumerate(disk):
            total += n * i
        return total


    @solution("")
    def part2(self) -> object:
        return None


# Day9("test").run()
Day9().run()
