from src.setup.day import Day
from src.util.dijkstra import all_paths
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

        start = ("you",)
        end = "out"

        def is_end(cur):
            *past, cur = cur
            return cur == end

        def ns(cur):
            *past, cur = cur
            for n in devices[cur]:
                yield *past,cur,n

        return len(all_paths(start, is_end, ns))

    @solution("")
    def part2(self) -> object:
        return None

# Day11("test").run()
Day11().run()