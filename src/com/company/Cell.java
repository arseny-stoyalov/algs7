package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Cell extends JComponent {

    private static int idPool;

    public final int id;

    private final int CELL_SIDE;

    private int distance;

    private boolean endpoint;

    private final Set<Cell> connected = new HashSet<>();

    private final Point location;

    public Cell(int cellSide, int distance, Point location) {
        id = idPool++;
        CELL_SIDE = cellSide;
        this.distance = distance;
        this.location = location;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(CELL_SIDE, CELL_SIDE);
    }

    @Override
    protected void paintComponent(Graphics g) {

        g.setColor(endpoint ? Color.CYAN : Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        String label = endpoint ? "" : String.valueOf(distance);
        Font f = new Font("Comic Sans", Font.PLAIN, CELL_SIDE / 2);
        FontMetrics metrics = g.getFontMetrics(f);
        int x = (getWidth() - metrics.stringWidth(label)) / 2;
        int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
        g.setFont(f);
        g.drawString(label, x, y);
    }

    public void setDistance(int distance) {
        if (!endpoint)
            this.distance = distance;
        repaint();
    }

    public void makeEndpoint() {
        endpoint = true;
        distance = 0;
        repaint();
    }

    public void fill(Color color) {

        Graphics g = getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        String label = String.valueOf(distance);
        Font f = new Font("Comic Sans", Font.PLAIN, CELL_SIDE / 2);
        FontMetrics metrics = g.getFontMetrics(f);
        int x = (getWidth() - metrics.stringWidth(label)) / 2;
        int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
        g.setFont(f);
        g.drawString(label, x, y);
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public Point getLocation() {
        return location;
    }

    public Set<Cell> getConnected() {
        return Set.copyOf(connected);
    }

    public void createConnection(Cell cell) {
        connected.add(cell);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "id=" + id +
                ", distance=" + distance +
                '}';
    }
}
