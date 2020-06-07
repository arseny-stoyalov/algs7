package com.company.algorithms;

import com.company.Cell;

import java.util.*;

public class AStarFinder implements PathFinder {

    @Override
    public List<Cell> findPath(Cell start, Cell end, Map<Cell, Integer> graph) {

        long timeStart = System.nanoTime();
        Map<Cell, Cell> cameFrom = new HashMap<>();
        cameFrom.put(start, null);
        graph.put(start, 0);
        PriorityQueue<Cell> frontier = new PriorityQueue<>(Comparator.comparingInt(graph::get));
        frontier.add(start);

        while (!frontier.isEmpty()) {
            Cell current = frontier.remove();

            if (current == end) break;

            Set<Cell> neighbours = current.getConnected();
            for (Cell next : neighbours) {
                int newCost = graph.get(current) + next.getDistance();
                if (!cameFrom.containsKey(next) || newCost < graph.get(next)) {
                    graph.put(next, newCost + heuristic(next, end));
                    frontier.add(next);
                    cameFrom.put(next, current);
                }
            }
        }

        Cell current = cameFrom.get(end);
        List<Cell> path = new ArrayList<>();
        while (current != start) {
            path.add(current);
            current = cameFrom.get(current);
        }

        long timeEnd = System.nanoTime();
        System.out.println("Time taken: " + (timeEnd - timeStart));
        return path;
    }

}
