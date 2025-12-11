from collections import defaultdict

from src.setup.day import Day
from src.util.dijkstra import count_paths
from src.util.solution import solution


class Day11(Day):
    devices = {}

    @property
    def day(self):
        return 11

    def ns(self, cur):
        for n in self.devices[cur]:
            yield n

    def count(self, start, end):
        return count_paths(start, lambda cur: cur == end, self.ns)

    @solution("615")
    def part1(self) -> object:
        self.devices = defaultdict(list)
        for line in self.input:
            device_name, outputs = line.split(": ")
            self.devices[device_name] = outputs.split(" ")

        return self.count("you", "out")

    @solution("303012373210128")
    def part2(self) -> object:
        return self.count("svr", "fft") * self.count("fft", "dac") * self.count("dac", "out")


# Day11("test").run()
# Day11("test2").run()
Day11().run()
