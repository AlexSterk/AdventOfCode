package util;

import java.util.*;

public final class Graph<T> {
    private final Map<T, Map<T, Integer>> nodes;

    public Graph() {
        nodes = new HashMap<>();
    }

    public Graph(Comparator<T> comparator) {
        nodes = new TreeMap<>(comparator);
    }

    public Set<T> getNeighbours(T n) {
        return Collections.unmodifiableSet(nodes.get(n).keySet());
    }

    public Set<T> nodes() {
        return Collections.unmodifiableSet(nodes.keySet());
    }

    public boolean containsNode(T node) {
        return nodes.containsKey(node);
    }

    public void addNode(T node) {
        nodes.putIfAbsent(node, new HashMap<>());
    }

    public void addEdge(T from, T to, int weight, boolean directional) {
        nodes.get(from).putIfAbsent(to, weight);
        if (!directional) addEdge(to, from, weight, true);
    }

    public void removeEdge(T from, T to, boolean directional) {
        nodes.get(from).remove(to);
        if (!directional) removeEdge(to, from, true);
    }

    public void addEdge(T from, T to, int weight) {
        addEdge(from, to, weight, false);
    }

    public int getWeight(T from, T to) {
        return nodes.get(from).get(to);
    }

    private void runDijkstra(T start, Map<T, Integer> dist, Map<T, T> pred) {
        dist.put(start, 0);
        PriorityQueue<T> Q = new PriorityQueue<>(Comparator.comparing(dist::get));

        for (T n : nodes()) {
            if (!n.equals(start)) {
                dist.put(n, Integer.MAX_VALUE);
                pred.put(n, null);
            }
        }
        Q.offer(start);
        while (!Q.isEmpty()) {
            T u = Q.poll();
            for (T v : getNeighbours(u)) {
                int alt = dist.get(u) + getWeight(u, v);
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    pred.put(v, u);
                    Q.offer(v);
                }
            }
        }
    }

    public int getDistance(T from, T to) {
        Map<T, Integer> dist = new HashMap<>();
        Map<T, T> pred = new HashMap<>();
        runDijkstra(from, dist, pred);

        return dist.get(to);
    }

    public Map<T, Integer> getDistance(T from) {
        Map<T, Integer> dist = new HashMap<>();
        Map<T, T> pred = new HashMap<>();
        runDijkstra(from, dist, pred);

        return dist;
    }

    public List<T> getPath(T from, T to) {
        Map<T, Integer> dist = new HashMap<>();
        Map<T, T> pred = new HashMap<>();
        runDijkstra(from, dist, pred);

        List<T> p = new ArrayList<>();
        T cur = to;
        while (cur != from) {
            p.add(cur);
            cur = pred.get(cur);
            if (cur == null) return null;
        }
        Collections.reverse(p);
        return p;
    }

    public Map<T, Integer> floodFill() {
        Map<T, Integer> labels = new HashMap<>();
        int i = 1;

        Optional<T> unlabeled;
        while ((unlabeled = nodes().stream().filter(n -> !labels.containsKey(n)).findAny()).isPresent()) {
            Queue<T> Q = new ArrayDeque<>();
            T node = unlabeled.get();
            Q.offer(node);
            while (!Q.isEmpty()) {
                T u = Q.poll();
                labels.put(u, i);
                for (T v : this.getNeighbours(u)) {
                    if (!labels.containsKey(v)) Q.offer(v);
                }
            }
            i++;
        }

        return labels;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "nodes=" + nodes +
                '}';
    }
}
