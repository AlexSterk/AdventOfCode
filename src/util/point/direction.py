import src.util.directions as directions
from .point import Point

cardinal = [Point(dx,dy) for (dx,dy) in directions.cardinal.values()]
diagonal = [Point(dx,dy) for (dx,dy) in directions.diagonal.values()]
all = [*cardinal, *diagonal]