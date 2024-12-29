from src.setup.day import Day
from src.util.solution import solution


def look_and_say(s):
    r = ""
    i = 0
    while i < len(s):
        c = s[i]
        j = i + 1
        while j < len(s) and s[j] == c:
            j += 1
        r += str(j - i) + c
        i = j
    return r


class Day10(Day):
    @property
    def day(self):
        return 10

    @solution("360154")
    def part1(self) -> object:
        s = self.raw_input
        for _ in range(40):
            s = look_and_say(s)
        return len(s)

    @solution("5103798")
    def part2(self) -> object:
        s = self.raw_input
        for _ in range(50):
            s = look_and_say(s)
        return len(s)


# Day10("test").run()
Day10().run()
