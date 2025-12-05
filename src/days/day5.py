from src.setup.day import Day
from src.util.solution import solution

class Day5(Day):
    @property
    def day(self):
        return 5

    @solution("623")
    def part1(self) -> object:
        fresh_ranges, ingredients = self.input
        fresh_ranges = [tuple(map(int, x.split("-"))) for x in fresh_ranges]
        fresh_ranges = sorted(fresh_ranges)
        ingredients = [int(x) for x in ingredients]
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

    @solution("")
    def part2(self) -> object:
        return None

    def read_input(self):
        super().read_input()
        return [x.split("\n") for x in self.raw_input.split("\n\n")]



# Day5("test").run()
Day5().run()