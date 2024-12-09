import re
from heapq import heappush, heappop

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
        self.disk = disk.copy()

        i = 0
        l = len(disk) - 1
        while i < l:
            if disk[i] is not None:
                i += 1
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
        disk = self.disk.copy()
        disk.append(None)
        files = []
        empty = {}

        start_i = 0
        length = 0
        cur = disk[0]

        for empty_start, id in enumerate(disk):
            if id == cur:
                length += 1
            else:
                if cur is not None:
                    files.append((cur, start_i, length))
                else:
                    if empty.get(length) is None:
                        empty[length] = []
                    heappush(empty.get(length), start_i)
                start_i = empty_start
                length = 1
                cur = id
        # print(disk)
        # print(files)
        # print(empty)

        for (id, start_i, length) in files[::-1]:
            empty_available = min((k for k, v in empty.items() if k >= length and len(v) > 0), default=None)
            if empty_available is None:
                continue
            empty_start = empty.get(empty_available)[0]
            if empty_start >= start_i:
                continue
            empty_start = heappop(empty.get(empty_available))
            files.remove((id, start_i, length))
            files.append((id, empty_start, length))
            remaining = empty_available - length
            if remaining > 0:
                if empty.get(remaining) is None:
                    empty[remaining] = []
                heappush(empty.get(remaining), empty_start + length)
        total = 0
        for id, start_i, length in files:
            for i in range(length):
                total += id * (start_i + i)

        return total


# Day9("test").run()
Day9().run()
