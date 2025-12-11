from _pyrepl.commands import end
from functools import cache

from src.setup.day import Day
from src.util.dijkstra import all_paths, count_paths
from src.util.solution import solution


class Day11(Day):
    @property
    def day(self):
        return 11

    @solution("615")
    def part1(self) -> object:
        devices = {}
        for line in self.input:
            device_name, outputs = line.split(": ")
            devices[device_name] = outputs.split(" ")

        if "you" not in devices:
            return None

        start = ("you",)
        end = "out"

        def is_end(cur):
            *_, cur = cur
            return cur == end

        def ns(cur):
            *past, cur = cur
            for n in devices[cur]:
                yield *past, cur, n

        return len(all_paths(start, is_end, ns))

    @solution("303012373210128")
    def part2(self) -> object:
        devices = {}
        for line in self.input:
            device_name, outputs = line.split(": ")
            devices[device_name] = outputs.split(" ")
        if "svr" not in devices:
            return None

        def ns(cur):
            for n in devices.get(cur, []):
                yield n

        def count(start, end):
            return count_paths(start, lambda cur: cur == end, ns)

        return count("svr", "fft") * count("fft", "dac") * count("dac", "out")


Day11("test").run()
# Day11("test2").run()
Day11().run()
