package com.company;

import com.company.algorithms.AStarFinder;
import com.company.algorithms.PathFinder;
import com.company.algorithms.RayFinder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

    private final int CELLS_IN_SIDE;

    private final Cell[][] grid;

    private final int MAP_SIDE;

    private final JTextField txtDistance = new JTextField();

    private final PathFinder finder;

    public App(int cellsInSide, int mapSide, PathFinder finder) {

        if (cellsInSide < 1)
            throw new IllegalArgumentException("Number of cells in a map side" +
                    " must be grater than 0, got: " + cellsInSide);
        if (mapSide < 1)
            throw new IllegalArgumentException("Length of map's side" +
                    " must be grater than 0, got: " + mapSide);
        this.finder = finder;
        CELLS_IN_SIDE = cellsInSide;
        MAP_SIDE = mapSide;
        grid = new Cell[CELLS_IN_SIDE][CELLS_IN_SIDE];
        txtDistance.setText("1");
        txtDistance.setColumns(3);
    }

    private void createView() {

        JFrame frame = new JFrame("Path finder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(new BorderLayout());

        GridBagLayout gbLayout = new GridBagLayout();
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.weightx = 1;
        gbConstraints.weighty = 1;
        gbConstraints.insets.set(0, 0, 1, 1);

        JPanel mapPanel = new JPanel(gbLayout);
        mapPanel.setBackground(Color.BLUE);

        CellHandler cellHandler = new CellHandler();

        for (int y = 0; y < CELLS_IN_SIDE; y++) {
            for (int x = 0; x < CELLS_IN_SIDE; x++) {
                grid[x][y] = new Cell(MAP_SIDE / CELLS_IN_SIDE,
                        (int) (Math.random() * 20) + 1,
                        new Point(x, y));
                gbConstraints.gridx = x;
                gbConstraints.gridy = y;

                gbLayout.setConstraints(grid[x][y], gbConstraints);

                mapPanel.add(grid[x][y]);
                grid[x][y].addMouseListener(cellHandler);
            }
        }
        grid[0][0].makeEndpoint();
        grid[CELLS_IN_SIDE - 1][CELLS_IN_SIDE - 1].makeEndpoint();
        connectCells(grid);

        frame.add(mapPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel();

        JButton btnFindPath = new JButton("Find Path");
        btnFindPath.addActionListener(e -> showPath());

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> {
            for (Cell[] line : grid) {
                for (Cell cell : line) {
                    cell.repaint();
                }
            }
        });

        bottom.add(btnFindPath);
        bottom.add(txtDistance);
        bottom.add(btnReset);

        frame.add(bottom, BorderLayout.SOUTH);

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void start() {
        SwingUtilities.invokeLater(this::createView);
    }

    private class CellHandler implements MouseListener {

        private boolean modifying;

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            modifying = true;
            Cell cell = (Cell) e.getSource();
            cell.setDistance(Integer.parseInt(txtDistance.getText()));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            modifying = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (modifying) {
                Cell cell = (Cell) e.getSource();
                cell.setDistance(Integer.parseInt(txtDistance.getText()));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    private void connectCells(Cell[][] grid) {

        for (int x = 0; x < CELLS_IN_SIDE; x++) {
            for (int y = 0; y < CELLS_IN_SIDE; y++) {
                List<Cell> neighbours = new ArrayList<>(3);
                boolean xAvailable, yAvailable;
                if (xAvailable = x < CELLS_IN_SIDE - 1) neighbours.add(grid[x + 1][y]);
                if (yAvailable = y < CELLS_IN_SIDE - 1) neighbours.add(grid[x][y + 1]);
                if (xAvailable && yAvailable) neighbours.add(grid[x + 1][y + 1]);
                for (Cell neighbour : neighbours)
                    grid[x][y].createConnection(neighbour);
            }
        }
    }

    private void showPath() {
        Cell start = grid[0][0];
        Cell end = grid[CELLS_IN_SIDE - 1][CELLS_IN_SIDE - 1];
        finder.findPath(start, end, getGraph(grid)).forEach(c -> c.fill(Color.ORANGE));
    }

    private Map<Cell, Integer> getGraph(Cell[][] grid) {

        Map<Cell, Integer> graph = new HashMap<>();
        for (Cell[] line : grid) {
            for (Cell cell : line) {
                graph.put(cell, Integer.MAX_VALUE);
            }
        }
        return graph;
    }

    public static void main(String[] args) {
        if (args.length < 1) return;
        PathFinder finder;
        switch (args[0]) {
            case "-a":
                finder = new AStarFinder();
                break;
            case "-r":
                finder = new RayFinder();
                break;
            default:
                return;
        }
        App app = new App(20, 700, finder);
        app.start();
    }
}
