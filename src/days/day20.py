from src.setup.day import Day
from src.util.solution import solution


class Day20(Day):
    @property
    def day(self):
        return 20

    @solution("665280")
    def part1(self) -> object:
        presents = int(self.raw_input)
        houses = [0] * (presents // 10 + 1)
        for i in range(1, presents // 10 + 1):
            for j in range(i, presents // 10 + 1, i):
                houses[j] += i * 10
        return next((i for i, h in enumerate(houses) if h >= presents), None)



    @solution("705600")
    def part2(self) -> object:
        presents = int(self.raw_input)
        houses = [0] * (presents // 11 + 1)
        for i in range(1, presents // 11 + 1):
            for j in range(i, min(i * 50 + 1, presents // 11 + 1), i):
                houses[j] += i * 11
        return next((i for i, h in enumerate(houses) if h >= presents), None)

# Day20("test").run()
Day20().run()
