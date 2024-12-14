from src.setup.day import Day
from src.util.solution import solution


def calc_checksum(a, l):
    def step():
        nonlocal a
        b = a
        # reverse b
        b = b[::-1]
        # replace 0 with 1 and 1 with 0
        b = b.replace("0", "2").replace("1", "0").replace("2", "1")
        a = a + "0" + b

    while len(a) < l:
        step()
    a = a[:l]
    checksum = "".join(["1" if a[i] == a[i + 1] else "0" for i in range(0, len(a), 2)])
    while len(checksum) % 2 == 0:
        checksum = "".join(["1" if checksum[i] == checksum[i + 1] else "0" for i in range(0, len(checksum), 2)])
    return checksum


class Day16(Day):
    @property
    def day(self):
        return 16

    @solution("10010101010011101")
    def part1(self) -> object:
        a = self.raw_input
        return calc_checksum(a, 272)

    @solution("01100111101101111")
    def part2(self) -> object:
        a = self.raw_input
        return calc_checksum(a, 35651584)


# Day16("test").run()
Day16().run()
