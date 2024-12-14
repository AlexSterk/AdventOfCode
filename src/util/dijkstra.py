from queue import PriorityQueue
from typing import TypeVar, Iterable, Callable

T = TypeVar('T')


def shortest_path(
        start: T,
        end: Callable[[T], bool],
        neighbours: Callable[[T], Iterable[T]],
        cost: Callable[[T, T], int]
):
    dist = {start: 0}
    visited = set()
    queue: PriorityQueue[tuple[int, T]] = PriorityQueue()
    queue.put((0, start))
    while not queue.empty():
        d, node = queue.get()
        if end(node):
            return dist, node, d
        if node in visited:
            continue
        visited.add(node)
        for neighbour in neighbours(node):
            if neighbour in visited:
                continue
            new_dist = d + cost(node, neighbour)
            if neighbour not in dist or new_dist < dist[neighbour]:
                dist[neighbour] = new_dist
                queue.put((new_dist, neighbour))
    return dist, None, None


def shortest_paths(
        start: T,
        neighbours: Callable[[T], Iterable[T]],
        cost: Callable[[T, T], int]
):
    return shortest_path(start, lambda _: False, neighbours, cost)
