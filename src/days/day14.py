import hashlib
import re

from src.setup.day import Day
from src.util.solution import solution

class Day14(Day):
    @property
    def day(self):
        return 14

    @solution("15035")
    def part1(self) -> object:
        salt = self.raw_input
        i = 0
        keys = []
        while len(keys) < 64:
            key = salt + str(i)
            i += 1
            h = hashlib.md5(key.encode()).hexdigest()
            if m := re.search(r'(.)\1\1', h):
                m = m.group(1)
                for j in range(i, i + 1000):
                    key = salt + str(j)
                    h = hashlib.md5(key.encode()).hexdigest()
                    if re.search(rf'({m})\1\1\1\1', h):
                        keys.append(i - 1)
                        break
        return i - 1

    @solution("")
    def part2(self) -> object:
        return None

# Day14("test").run()
Day14().run()
