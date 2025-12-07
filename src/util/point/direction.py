import src.util.directions as directions
from .point import Point

up = Point(0, -1)
down = Point(0, 1)
left = Point(-1, 0)
right = Point(1, 0)

cardinal = [Point(dx,dy) for (dx,dy) in directions.cardinal.values()]
diagonal = [Point(dx,dy) for (dx,dy) in directions.diagonal.values()]
all = [*cardinal, *diagonal]