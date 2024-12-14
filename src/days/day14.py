import functools
import hashlib
import re

from src.setup.day import Day
from src.util.solution import solution

@functools.cache
def get_hash(salt, index, stretch = 0):
    key = salt + str(index)
    h = hashlib.md5(key.encode()).hexdigest()
    for _ in range(stretch):
        h = hashlib.md5(h.encode()).hexdigest()
    return h

class Day14(Day):
    @property
    def day(self):
        return 14

    @solution("15035")
    def part1(self, stretch = 0) -> object:
        salt = self.raw_input
        i = 0
        keys = []
        while len(keys) < 64:
            h = get_hash(salt, i, stretch)
            i += 1
            if m := re.search(r'(.)\1\1', h):
                m = m.group(1)
                for j in range(i, i + 1000):
                    h = get_hash(salt, j, stretch)
                    if re.search(rf'({m})\1\1\1\1', h):
                        keys.append(i - 1)
                        break
        return keys[-1]

    @solution("19968")
    def part2(self) -> object:
        return self.part1(2016, skip_test=True)

# Day14("test").run()
Day14().run()
