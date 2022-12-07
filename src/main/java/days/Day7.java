package days;

import setup.Day;

import java.util.HashMap;
import java.util.TreeMap;

public class Day7 extends Day {

    private final FileSystemNode root = new FileSystemNode("/", FileSystemNode.Type.FOLDER, null);

    @Override
    public void processInput() {
        parseFileSystem();
        calculateSize(root);
    }

    private void calculateSize(FileSystemNode node) {
        if (node.type == FileSystemNode.Type.FILE) {
            return;
        }
        node.size = node.values().stream().peek(this::calculateSize).mapToLong(n -> n.size).sum();

    }

    private void parseFileSystem() {
        FileSystemNode current = root;
        for (String line : lines()) {
            if (line.equals("$ cd /")) {
                current = root;
                continue;
            }
            if (line.equals("$ cd ..")) {
                current = current.parent;
                continue;
            }
            if (line.startsWith("$ cd ")) {
                String folder = line.substring(5);
                current = current.get(folder);
                continue;
            }
            if (line.startsWith("dir ")) {
                String folder = line.substring(4);
                current.put(folder, new FileSystemNode(folder, FileSystemNode.Type.FOLDER, current));
                continue;
            }
            if (line.matches("\\d.*")) {
                String[] split = line.split(" ");
                String name = split[1];
                long size = Long.parseLong(split[0]);
                current.put(name, new FileSystemNode(name, FileSystemNode.Type.FILE, current, size));
                continue;
            }

            if (line.equals("$ ls")) {
                continue;
            }

            throw new RuntimeException("Unknown line: " + line);
        }
    }

    @Override
    public Object part1() {
        // flatten the tree
        HashMap<String, FileSystemNode> map = new HashMap<>();
        flatten(root, map, "");

        return map.values().stream().filter(n -> n.type == FileSystemNode.Type.FOLDER && n.size <= 100000).peek(n -> System.out.println(n.name + " " + n.size)).mapToLong(n -> n.size).sum();
    }

    private void flatten(FileSystemNode node, HashMap<String, FileSystemNode> map, String path) {
        String name = path + "/" + node.name;
        map.put(name, node);
        for (FileSystemNode child : node.values()) {
            flatten(child, map, name);
        }
    }

    @Override
    public Object part2() {
        return null;
    }

    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public boolean isTest() {
        return false;
    }

    @Override
    public String partOneSolution() {
        return "1543140";
    }

    private static class FileSystemNode extends TreeMap<String, FileSystemNode> {
        private static enum Type {
            FILE, FOLDER
        }

        private Long size;

        private final Type type;

        private final String name;

        private final FileSystemNode parent;

        private FileSystemNode(String name, Type type, FileSystemNode parent) {
            this.name = name;
            this.type = type;
            this.parent = parent;
        }

        private FileSystemNode(String name, Type type, FileSystemNode parent, long size) {
            this.name = name;
            this.type = type;
            this.parent = parent;
            this.size = size;
        }

        public String toString(int indent) {
            String s = "- %s (%s, size=%d)".formatted(name, type, size);
            if (type == Type.FOLDER) {
                for (FileSystemNode value : this.values()) {
                    s += "\n%s".formatted(value.toString(indent)).replace("\n", "\n" + "\t".repeat(indent+1));
                }
            }
            return s;
        }

        @Override
        public String toString() {
            return toString(0);
        }
    }
}
