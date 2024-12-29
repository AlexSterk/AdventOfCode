from src.setup.day import Day
from src.util.solution import solution

class Day11(Day):
    @property
    def day(self):
        return 11

    @solution("hepxxyzz")
    def part1(self) -> object:
        alphabet = "abcdefghijklmnopqrstuvwxyz".replace("i", "").replace("o", "").replace("l", "")

        def increment(s: str) -> str:
            if s[-1] == alphabet[-1]:
                return increment(s[:-1]) + alphabet[0]
            return s[:-1] + alphabet[alphabet.index(s[-1]) + 1]

        def has_straight(s: str) -> bool:
            for i in range(len(s) - 2):
                if s[i:i+3] in alphabet:
                    return True
            return False

        def has_pairs(s: str) -> bool:
            pairs = 0
            i = 0
            while i < len(s) - 1:
                if s[i] == s[i + 1]:
                    pairs += 1
                    i += 1
                i += 1
            return pairs >= 2

        def is_valid(s: str) -> bool:
            return has_straight(s) and has_pairs(s)

        password = self.raw_input
        while not is_valid(password):
            password = increment(password)

        self.increment = increment
        self.is_valid = is_valid
        self.password = password
        return password

    @solution("heqaabcc")
    def part2(self) -> object:
        password = self.increment(self.password)
        while not self.is_valid(password):
            password = self.increment(password)

        return password

# Day11("test").run()
Day11().run()
