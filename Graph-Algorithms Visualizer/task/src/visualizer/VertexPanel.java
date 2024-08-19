package visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VertexPanel extends JPanel {
    private Node node = null;
    Color nodeColor = Color.WHITE;

    public void setColor(Color color) {
        nodeColor = color;
        GraphPanel.instance.setUIChanged();
        MainFrame.mainFrame.repaint();
        node.setVisited(true);
    }

    public VertexPanel(String labelName, int x, int y) {
        setName(String.format("Vertex %s", labelName));
        setBackground(nodeColor); // Set a background color to differentiate
        setBounds(x-25, y-25, 50, 50); // Position and size
        setLayout(new BorderLayout());
        //setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Create the vertex label
        JLabel vertexLabel = new JLabel(labelName, SwingConstants.CENTER);
        vertexLabel.setName(String.format("VertexLabel %s", labelName));
        vertexLabel.setForeground(Color.BLACK);

        // Add the label to the vertex panel
        add(vertexLabel, BorderLayout.CENTER);

        // create a node and add it to the list of nodes in GraphPanel
        node = new Node(this, labelName);
        GraphPanel.nodes.put(labelName, node);

        // Make the vertex panel round (circle shape)
        setOpaque(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OnMouseClick();
            }
        });
    }

    private void OnMouseClick() {
        switch(MainFrame.mode){
            case Vertex_MODE:
                break;
            case Edge_MODE:
                GraphPanel.historyClickedVertex.add(this);
                addEdge();
                break;
            case None_MODE:
                break;
            case RemoveVertex_MODE:
                removeNode();
                break;
            case Algorithm_BFS:
            case Algorithm_DFS:
            case Algorithm_Dijkstra:
            case Algorithm_Prime:
                if(node == null) return;
                Algorithm.getInstance().setStartNode(node);
                break;
        }
    }

    public void removeNode() {
        node.removeAllEdges();
        GraphPanel.nodes.remove(node.getName());
        GraphPanel.instance.remove(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(nodeColor);
        g.fillOval(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    private void addEdge() {
        if(GraphPanel.historyClickedVertex.size() < 2) return;
        Node node1 = GraphPanel.historyClickedVertex.getFirst().node;
        Node node2 = GraphPanel.historyClickedVertex.getLast().node;
        GraphPanel.historyClickedVertex.clear();
        if(node1 == node2) return;
        boolean bOk = false;
        while(!bOk) {
            String input = JOptionPane.showInputDialog(null,
                    "Enter Weight",
                    "Edge",
                    JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;

            try {
                int weight = Integer.parseInt(input);
                node1.addEdge(node2, weight);
                bOk = true;
            } catch (NumberFormatException e) {

            }
        }
    }

    public static void createVertex(JPanel graphPanel, String labelName, int x, int y) {
        // Create the vertex JPanel
        VertexPanel p = new VertexPanel(labelName, x, y);

        // Add the vertex panel to the graph panel
        graphPanel.add(p);
    }



}
