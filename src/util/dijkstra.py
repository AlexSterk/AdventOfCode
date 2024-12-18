from queue import PriorityQueue
from typing import TypeVar, Iterable, Callable

T = TypeVar('T')


def shortest_path(
        start: T,
        end: Callable[[T], bool],
        neighbours: Callable[[T], Iterable[T]],
        cost: Callable[[T, T], int] = lambda a, b: 1,
        prev={}
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
            if new_dist < dist.get(neighbour, float('inf')):
                dist[neighbour] = new_dist
                queue.put((new_dist, neighbour))
                prev[neighbour] = node
    return dist, None, None


def shortest_paths(
        start: T,
        neighbours: Callable[[T], Iterable[T]],
        cost: Callable[[T, T], int] = lambda a, b: 1
):
    return shortest_path(start, lambda _: False, neighbours, cost)


def all_paths(
        start: T,
        end: Callable[[T], bool],
        neighbours: Callable[[T], Iterable[T]],
        cost: Callable[[T, T], int] = lambda a, b: 1
):
    def ns(node):
        if end(node):
            return
        for n in neighbours(node):
            yield n

    paths, _, _ = shortest_paths(start, ns, cost)
    return [p for p in paths if end(p)]


def reconstruct_path(
        start: T,
        end: Callable[[T], bool],
        neighbours: Callable[[T], Iterable[T]],
        cost: Callable[[T, T], int] = lambda a, b: 1,
):
    prev = {}
    dist, end, d = shortest_path(start, end, neighbours, cost, prev)
    path = []
    cur = end
    while cur != start:
        path.append(cur)
        cur = prev[cur]
    path.append(start)
    path.reverse()
    return dist, end, d, path
