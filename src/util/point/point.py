from typing import NamedTuple

class Point(NamedTuple):
    x: int
    y: int

    def __add__(self, other: "Point") -> "Point":
        return Point(self.x + other.x, self.y + other.y)

    def distance(self, other: "Point"):
        return abs(self.x - other.x) + abs(self.y - other.y)

