from heapq import heappush, heappop

from src.setup.day import Day
from src.util.solution import solution


def calc_checksum(empty, files):
    total = 0

    for (file_id, file_start, file_length) in files[::-1]:
        # find the block closest to the start of the disk, that can fit the file
        empty_available = min((k for k, v in empty.items() if k >= file_length and len(v) > 0), default=None,
                              key=lambda x: empty.get(x)[0])
        # if that block does not exist, essentially the file should not move, and be processed in place
        if empty_available is None or empty.get(empty_available)[0] >= file_start:
            i = file_start
            empty_available = file_length
        else:
            # the block exists, we can take up the space
            i = heappop(empty.get(empty_available))
        # add the file to the checksum
        for k in range(file_length):
            total += file_id * (i + k)
        # If there are remaining empty blocks, add them back to the heap
        remaining = empty_available - file_length
        if remaining > 0:
            if empty.get(remaining) is None:
                empty[remaining] = []
            heappush(empty.get(remaining), i + file_length)

    return total


def blocks_and_files(blocks, max_length=None):
    blocks = blocks + [None]  # add a None to the end to make sure the last block is processed
    files = []  # (file_id, file_start, file_length)
    empty = {}  # various lengths of empty blocks in heaps to ensure we get the lowest index first
    file_start = 0  # start index of the current block
    blocks_length = 0  # length of the current block
    cur = blocks[0]  # current file_id
    for block_i, file_id in enumerate(blocks):
        # if the next block is the same as the current block, and we haven't reached the max length
        if file_id == cur and (max_length is None or blocks_length < max_length):
            blocks_length += 1
        else:
            # if current block is not the same as the previous block, add the previous block to the files or empty
            if cur is not None:
                files.append((cur, file_start, blocks_length))
            else:
                if empty.get(blocks_length) is None:
                    empty[blocks_length] = []
                heappush(empty.get(blocks_length), file_start)

            # reset the current block
            file_start = block_i
            blocks_length = 1
            cur = file_id
    return empty, files


class Day9(Day):
    @property
    def day(self):
        return 9

    @solution("6353658451014")
    def part1(self) -> object:
        s = self.raw_input

        file_id = 0
        disk = []
        for i, d in enumerate(s):
            if i % 2 == 0:
                for j in range(int(d)):
                    disk.append(file_id)
                file_id += 1
            else:
                for j in range(int(d)):
                    disk.append(None)
        self.disk = disk

        empty, files = blocks_and_files(disk, 1)

        return calc_checksum(empty, files)

    @solution("6382582136592")
    def part2(self) -> object:
        empty, files = blocks_and_files(self.disk)
        return calc_checksum(empty, files)


# Day9("test").run()
Day9().run()
