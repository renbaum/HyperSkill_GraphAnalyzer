package visualizer;

import javax.swing.*;
import java.awt.*;

public class Edge{
    private int weight = 0;
    private Node a = null;
    private Node b = null;
    LineComponent line1 = null;
    LineComponent line2 = null;
    JLabel label = null;
    JLabel label2 = null;

    public Edge (Node a, Node b, int weight){
        this.a = a;
        this.b = b;
        this.weight = weight;

        line1 = new LineComponent(a.getVertexPanel().getLocation(),
                b.getVertexPanel().getLocation(),
                String.format("Edge <%s -> %s>", a.getName(), b.getName()));
        line2 = new LineComponent(b.getVertexPanel().getLocation(),
                a.getVertexPanel().getLocation(),
                String.format("Edge <%s -> %s>", b.getName(), a.getName()));
        label = new JLabel(Integer.toString(weight), SwingConstants.CENTER);
        label.setName(String.format("EdgeLabel <%s -> %s>", a.getName(), b.getName()));
        label.setBounds(line1.getLabelBounds());
        label.setForeground(Color.WHITE);
        line1.setOpaque(false);
        line2.setOpaque(false);
        GraphPanel.instance.add(line1);
        GraphPanel.instance.add(line2);
        GraphPanel.instance.add(label);
        //GraphPanel.instance.repaint();
        //GraphPanel.instance.revalidate();
        MainFrame.mainFrame.repaint();
        MainFrame.mainFrame.revalidate();
        GraphPanel.instance.edges.add(this);
    }

    public void delete(){
        GraphPanel.instance.remove(line1);
        GraphPanel.instance.remove(line2);
        GraphPanel.instance.remove(label);
        GraphPanel.instance.repaint();
        GraphPanel.instance.revalidate();
        GraphPanel.instance.edges.remove(this);
        a.removeEdge(b);
        b.removeEdge(a);
    }

    public int getWeight(){
        return this.weight;
    }

    public boolean isPointOnEdge(Point p){
        boolean b = isPointOnLine(p.x, p.y, 
                            line1.source.x, line1.source.y, line1.destination.x, line1.destination.y,
                            10);
        return b;
    }
    
    /**
     * Method to check if a point (px, py) is near the line segment between (x1, y1) and (x2, y2)
     */
    private boolean isPointOnLine(int px, int py, int x1, int y1, int x2, int y2, int threshold) {
        // Calculate the distance from the point to the line
        double distance = pointToLineDistance(px, py, x1, y1, x2, y2);
        return distance <= threshold;
    }

    /**
     * Calculate the perpendicular distance from a point to a line
     */
    private double pointToLineDistance(int px, int py, int x1, int y1, int x2, int y2) {
        double normalLength = Math.hypot(x2 - x1, y2 - y1);
        return Math.abs((px - x1) * (y2 - y1) - (py - y1) * (x2 - x1)) / normalLength;
    }

    public void setColor(Color color) {
        line1.setColor(color);
        line2.setColor(color);
    }

    public Node getOpositeNode(Node node){
        if(node == a) return b;
        if(node == b) return a;
        return null;
    }

    public Node getNotVisitedNode(){
        if(!a.bVisited) return a;
        if(!b.bVisited) return b;
        return null;
    }

}
