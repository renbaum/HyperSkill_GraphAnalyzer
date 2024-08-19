package visualizer;

import java.util.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

enum StrategyEnum{
    BFS,
    DFS,
    None,
    Dijkstra,
    Prime;
}

public class Algorithm{
    private StrategyAlgorithm strategyAlgorithm = null;
    public static Algorithm instance;

    public static Algorithm getInstance() {
        if (instance == null) {
            instance = new Algorithm();
        }
        return instance;
    }

    public void setStrategyAlgorithm(StrategyEnum strategy){
        switch (strategy){
            case BFS:
                strategyAlgorithm = new StrategyBreathFirst();
                break;
            case DFS:
                strategyAlgorithm = new StrategyDeepSearch();
                break;
            case Dijkstra:
                strategyAlgorithm = new StrategyDijkstra();
                break;
            case Prime:
                strategyAlgorithm = new StrategyPrime();
                break;
            case None:
                strategyAlgorithm = null;
                break;
            default:
                return;
        }
        MainFrame.mainFrame.setDisplayText("Please choose a starting vertex");
    }

    public void setStartNode(Node startNode){
        GraphPanel.instance.resetColor();
        if(strategyAlgorithm != null){
            strategyAlgorithm.startNode(startNode);
        }
    }
}

abstract class StrategyAlgorithm{
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    protected void waitTimer(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void setWaitingText(){
        MainFrame.mainFrame.setDisplayText("Please wait...");
    }

    abstract public void startNode(Node node);
}

class StrategyDeepSearch extends StrategyAlgorithm{
    java.util.List<String> nodeTxt = new ArrayList<String>();

    @Override
    public void startNode(Node node) {
        if(node == null) return;
        setWaitingText();
        nodeTxt.clear();
        executorService.execute(() -> checkNode(node, 0));
        //checkNode(node);
    }

    private void checkNode(Node node, int level){
        if(node == null) return;
        if(node.bVisited) return;

        nodeTxt.add(node.getName());
        node.getVertexPanel().setColor(Color.YELLOW);
        MainFrame.mainFrame.repaint();
        PriorityQueue<Edge> edges = node.getEdgesSortedByWeight();
        while(edges.size() > 0){
//        for (Edge edge : edges) {
            Edge edge = edges.poll();
            Node nextNode = edge.getOpositeNode(node);
            if (!nextNode.bVisited) {
                waitTimer();
                edge.setColor(Color.YELLOW);
                checkNode(nextNode, level+1);
            }
        }
        if(level == 0){
            String joinedString = String.join(" -> ", nodeTxt);
            MainFrame.mainFrame.setDisplayText(String.format("DFS : %s", joinedString));
        }
    }
}

class StrategyBreathFirst extends StrategyAlgorithm{
    java.util.List<String> nodeTxt = new ArrayList<String>();
    Deque<Node> nodes = new ArrayDeque<>();

    @Override
    public void startNode(Node node) {
        if (node == null) return;
        setWaitingText();
        nodeTxt.clear();
        nodes.add(node);
        executorService.execute(() -> checkNode(node));
    }

    private void checkNode(Node node){
        if (node == null || node.bVisited) return;
        waitTimer();
        while(!nodes.isEmpty()){
            Node currentNode = nodes.removeFirst();
            currentNode.getVertexPanel().setColor(Color.YELLOW);
            nodeTxt.add(currentNode.getName());
            MainFrame.mainFrame.repaint();
            PriorityQueue<Edge> edges = currentNode.getEdgesSortedByWeight();
            while(edges.size() > 0){
                Edge edge = edges.poll();
                Node oppositeNode = edge.getOpositeNode(currentNode);
                if(!oppositeNode.bVisited){
                    edge.setColor(Color.YELLOW);
                    oppositeNode.getVertexPanel().setColor(Color.YELLOW);
                    nodes.add(oppositeNode);
                    waitTimer();
                }
            }
        }
        String joinedString = String.join(" -> ", nodeTxt);
        MainFrame.mainFrame.setDisplayText(String.format("BFS : %s", joinedString));

    }
}

class StrategyDijkstra extends StrategyAlgorithm{
    java.util.List<String> nodeTxt = new ArrayList<String>();
    PriorityQueue<Node> pq = null;

    public StrategyDijkstra(){
        pq = new PriorityQueue<>(Comparator.comparingInt(Node::getWeight));
    }

    @Override
    public void startNode(Node node) {
        if (node == null) return;
        setWaitingText();
        nodeTxt.clear();
        node.weight = 0;
        pq.add(node);
        node.getVertexPanel().setColor(Color.YELLOW);
        executorService.execute(() -> checkNode(node));
    }

    private void checkNode(Node node){
        waitTimer();
        while(!pq.isEmpty()){
            Node currentNode = pq.poll();
            for (Edge edge : currentNode.getEdgesSortedByWeight()) {
                Node adjacentNode = edge.getOpositeNode(currentNode);
                int weight = edge.getWeight() + currentNode.getWeight();
                if (adjacentNode.getWeight() > weight) {
                    adjacentNode.weight = weight;
                    pq.add(adjacentNode);
                }
            }
        }
        List<Node> nodes = GraphPanel.instance.nodes.values().stream().toList();
        for(Node n : nodes){
            if(n == node) continue;
            nodeTxt.add(String.format("%s=%d", n.getName(), n.weight));
        }
        String joinedString = String.join(", ", nodeTxt);
        MainFrame.mainFrame.setDisplayText(String.format("%s", joinedString));
    }

}

class StrategyPrime extends StrategyAlgorithm{
    java.util.List<String> nodeTxt = new ArrayList<String>();
    PriorityQueue<Edge> pq = null;

    public StrategyPrime(){
        pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
    }

    @Override
    public void startNode(Node node) {
        if (node == null) return;
        setWaitingText();
        nodeTxt.clear();
        node.getVertexPanel().setColor(Color.YELLOW);
        executorService.execute(() -> checkNode(node));
    }

    private void checkNode(Node node){
        waitTimer();
        pq.addAll(node.getEdgesSortedByWeight());
        while(!pq.isEmpty()){
            Edge edge = pq.poll();
            Node currentNode = edge.getNotVisitedNode();
            if(currentNode == null) continue;

            nodeTxt.add(String.format("%s=%s", currentNode.getName(), edge.getOpositeNode(currentNode).getName()));
            edge.setColor(Color.YELLOW);
            currentNode.getVertexPanel().setColor(Color.YELLOW);
            pq.addAll(currentNode.getEdgesSortedByWeight());
            waitTimer();
        }
        String joinedString = String.join(", ", nodeTxt);
        MainFrame.mainFrame.setDisplayText(String.format("%s", joinedString));
    }

}