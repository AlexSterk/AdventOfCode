from src.setup.day import Day
from src.util.solution import solution


class Day5(Day):
    @property
    def day(self):
        return 5

    @solution("623")
    def part1(self) -> object:
        fresh_ranges, ingredients = self.parsed()

        return sum(
            any(low <= ingredient <= high for low, high in fresh_ranges)
            for ingredient in ingredients
        )

    @solution("353507173555373")
    def part2(self) -> object:
        ranges, _ = self.parsed()

        merged = [ranges[0]]

        for low, high in ranges[1:]:
            last_low, last_high = merged[-1]
            if last_low <= low <= last_high:
                merged[-1] = (last_low, max(last_high, high))
            else:
                merged.append((low, high))

        return sum(high - low + 1 for (low, high) in merged)

    def parsed(self):
        fresh_ranges, ingredients = self.input
        fresh_ranges = [tuple(map(int, x.split("-"))) for x in fresh_ranges]
        fresh_ranges = sorted(fresh_ranges)
        ingredients = [int(x) for x in ingredients]
        return fresh_ranges, ingredients

    def read_input(self):
        super().read_input()
        return [x.split("\n") for x in self.raw_input.split("\n\n")]


# Day5("test").run()
Day5().run()
