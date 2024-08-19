package visualizer;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Node{
    private VertexPanel vertexPanel = null;
    private Map<Node, Edge> edges = new HashMap<Node, Edge>();
    private String name = "";
    boolean bVisited = false;
    int weight = Integer.MAX_VALUE;

    public Node(VertexPanel vertexPanel, String name){
        this.vertexPanel = vertexPanel;
        this.name = name;
    }

    public PriorityQueue<Edge> getEdgesSortedByWeight(){
        PriorityQueue<Edge> pq = new PriorityQueue<>(
                Comparator.comparingInt(Edge::getWeight)
        );

        for(Edge e: edges.values()){
            pq.add(e);
        }
        return pq;
    }

    public void setVisited(boolean bVisited){
        this.bVisited = bVisited;
    }

    public void resetColor(){
        vertexPanel.setColor(Color.WHITE);
        for(Edge edge : edges.values()){
            edge.setColor(Color.WHITE);
        }
        bVisited = false;
        weight = Integer.MAX_VALUE;
    }

    public void addEdge(Node node, int weight) {
        Edge edge = edges.get(node);
        if(edge != null) return;

        edge = new Edge(this, node, weight);
        edges.put(node, edge);
        node.edges.put(this, edge);
    }

    public String getName(){
        return name;
    }

    public VertexPanel getVertexPanel() {
        return vertexPanel;
    }

    public void removeEdge(Node node) {
        edges.remove(node);
        node.edges.remove(this);
    }

    public void removeAllEdges(){
        while(edges.size() > 0){
            Edge edge = edges.get(edges.keySet().iterator().next());
            edge.delete();
        }
        edges.clear();
    }

    public int getWeight() {
        return weight;
    }
}
