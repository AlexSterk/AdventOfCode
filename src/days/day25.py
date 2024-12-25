from itertools import product

from src.setup.day import Day
from src.util.solution import solution

class Day25(Day):
    @property
    def day(self):
        return 25

    @solution("3242")
    def part1(self) -> object:
        locks, keys = [], []
        for chunk in self.raw_input.split("\n\n"):
            lines = chunk.splitlines()
            if "#" in lines[0]:
                locks.append(chunk)
            else:
                keys.append(chunk)

        max_h = len(locks[0].splitlines()) - 1

        def get_heights_lock(lock):
            lines = lock.splitlines()
            heights: list[int|None] = [None] * len(lines[0])
            for y, line in enumerate(lines):
                for x, char in enumerate(line):
                    if char == "#":
                        heights[x] = y
            return tuple(heights)

        def get_heights_key(key):
            lines = key.splitlines()
            return get_heights_lock("\n".join(lines[::-1]))

        def invert_heights(heights):
            return tuple(max_h - n for n in heights)

        locks = [get_heights_lock(lock) for lock in locks]
        keys = [get_heights_key(key) for key in keys]

        return sum(1 for lock, key in product(locks, keys) if not any(x >= y for x, y in zip(lock, invert_heights(key))))

    @solution("")
    def part2(self) -> object:
        return None

# Day25("test").run()
Day25().run()
