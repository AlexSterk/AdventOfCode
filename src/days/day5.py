from src.setup.day import Day
from src.util.solution import solution

class Day5(Day):
    @property
    def day(self):
        return 5

    @solution("623")
    def part1(self) -> object:
        fresh_ranges, ingredients = self.parsed()
        # print(fresh_ranges, ingredients)

        fresh = 0
        for ingredient in ingredients:
            for low, high in fresh_ranges:
                if low > ingredient:
                    break
                if low <= ingredient <= high:
                    fresh += 1
                    break
        return fresh

    def parsed(self):
        fresh_ranges, ingredients = self.input
        fresh_ranges = [tuple(map(int, x.split("-"))) for x in fresh_ranges]
        fresh_ranges = sorted(fresh_ranges)
        ingredients = [int(x) for x in ingredients]
        return fresh_ranges, ingredients

    @solution("353507173555373")
    def part2(self) -> object:
        ranges, _ = self.parsed()
        print(ranges)

        while True:
            if self.loop(ranges):
                continue
            break

        print(ranges)

        return sum(high-low+1 for (low, high) in ranges)

    def loop(self, ranges):
        for i, cur_range in enumerate(ranges[:-1]):
            next_range = ranges[i + 1]
            if cur_range[0] <= next_range[0] <= cur_range[1]:
                ranges[i] = (cur_range[0], max(cur_range[1], next_range[1]))
                ranges.remove(next_range)
                return True
        return False

    def read_input(self):
        super().read_input()
        return [x.split("\n") for x in self.raw_input.split("\n\n")]



# Day5("test").run()
Day5().run()