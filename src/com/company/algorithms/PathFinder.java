package com.company.algorithms;

import com.company.Cell;

import java.awt.*;
import java.util.List;
import java.util.Map;

public interface PathFinder {

    List<Cell> findPath(Cell start, Cell end, Map<Cell, Integer> graph);

    default int heuristic(Cell start, Cell end){
        return Math.abs(end.getLocation().x - start.getLocation().x)
                + Math.abs(end.getLocation().y - start.getLocation().y);
    }

}
