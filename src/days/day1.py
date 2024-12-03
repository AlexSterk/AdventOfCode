from src.setup.day import Day
from src.util.solution import solution

class Day1(Day):
    @property
    def day(self):
        return 1

    @solution("2742123")
    def part1(self) -> object:
        lists = [l.split() for l in self.input]
        self.left_list = [int(l[0]) for l in lists]
        self.right_list = [int(l[1]) for l in lists]
        left_list = self.left_list.copy()
        right_list = self.right_list.copy()

        left_list.sort()
        right_list.sort()

        d = 0

        while len(left_list) and len(right_list):
            l = left_list.pop()
            r = right_list.pop()

            d+= abs(l-r)

        return d

    @solution("21328497")
    def part2(self) -> object:
        score = 0

        for i in self.left_list:
            count = 0
            for j in self.right_list:
                if i == j:
                    count += 1
            score += i * count
        return score

# Day1("test").run()
Day1().run()