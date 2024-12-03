from src.setup.day import Day
from src.util.solution import solution


def is_safe(levels: list[int]):
    deltas = [levels[i] - levels[i - 1] for i in range(1, len(levels))]

    return (all(d >= 0 for d in deltas) or all(d <= 0 for d in deltas)) and all(1 <= abs(d) <= 3 for d in deltas)


class Day2(Day):
    @property
    def day(self):
        return 2

    @solution("490")
    def part1(self) -> object:
        self.reports = [[int(i) for i in l.split()] for l in self.input]

        safe = 0
        for report in self.reports:
            if is_safe(report):
                safe += 1

        return safe

    @solution("536")
    def part2(self) -> object:
        safe = 0
        for report in self.reports:
            for i in range(-1, len(report)):
                if i == -1:
                    without_i = report[:]
                else:
                    without_i = report[:i] + report[i + 1:]
                if is_safe(without_i):
                    safe += 1
                    break
        return safe


# Day2("test").run()
Day2().run()
