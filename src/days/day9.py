from heapq import heappush, heappop

from src.setup.day import Day
from src.util.solution import solution


def calc_checksum(empty, files):
    for (file_id, file_start, file_length) in files[::-1]:
        empty_available = min((k for k, v in empty.items() if k >= file_length and len(v) > 0), default=None,
                              key=lambda x: empty.get(x)[0])
        if empty_available is None:
            continue
        i = empty.get(empty_available)[0]
        if i >= file_start:
            continue
        i = heappop(empty.get(empty_available))
        files.remove((file_id, file_start, file_length))
        files.append((file_id, i, file_length))
        remaining = empty_available - file_length
        if remaining > 0:
            if empty.get(remaining) is None:
                empty[remaining] = []
            heappush(empty.get(remaining), i + file_length)
    total = 0
    for file_id, file_start, file_length in files:
        for i in range(file_length):
            total += file_id * (file_start + i)
    return total


def blocks_and_files(disk, max_length=None):
    disk.append(None)
    files = []
    empty = {}
    start_i = 0
    length = 0
    cur = disk[0]
    for i, file_id in enumerate(disk):
        if file_id == cur and (max_length is None or length < max_length):
            length += 1
        else:
            if cur is not None:
                files.append((cur, start_i, length))
            else:
                if empty.get(length) is None:
                    empty[length] = []
                heappush(empty.get(length), start_i)
            start_i = i
            length = 1
            cur = file_id
    return empty, files


class Day9(Day):
    @property
    def day(self):
        return 9

    @solution("6353658451014")
    def part1(self) -> object:
        s = self.raw_input

        file_id = 0
        disk = []
        for i, d in enumerate(s):
            if i % 2 == 0:
                for j in range(int(d)):
                    disk.append(file_id)
                file_id += 1
            else:
                for j in range(int(d)):
                    disk.append(None)
        self.disk = disk.copy()

        empty, files = blocks_and_files(disk, 1)

        return calc_checksum(empty, files)

    @solution("6382582136592")
    def part2(self) -> object:
        disk = self.disk.copy()
        empty, files = blocks_and_files(disk)

        return calc_checksum(empty, files)


# Day9("test").run()
Day9().run()
