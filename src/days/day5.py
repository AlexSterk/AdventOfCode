
from src.setup.day import Day
from src.util.solution import solution

class Day5(Day):
    @property
    def day(self):
        return 5

    @solution("6949")
    def part1(self) -> object:
        rules, pages = self.raw_input.split("\n\n")
        rules = rules.split("\n")
        pages = pages.strip().split("\n")

        rules = set((int(x),int(y)) for x,y in (rule.split("|") for rule in rules))

        total = 0
        for page in pages:
            l = [int(n) for n in page.split(",")]
            correct = self.check_pages(l, rules)

            if correct:
                total += l[len(l)//2]
        return total

    def check_pages(self, l, rules):
        for i in range(len(l)):
            for j in range(len(l)):
                if i == j:
                    continue
                x, y = l[i], l[j]
                if (x, y) in rules:
                    if i > j:
                        print(f"Found {l[i]} and {l[j]} in {l}")
                        return False
        return True

    @solution("")
    def part2(self) -> object:
        return None

# Day5("test").run()
Day5().run()